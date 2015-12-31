package com.framgia.elsytem;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.framgia.elsytem.jsonResponse.CategoryResponse;
import com.framgia.elsytem.jsonResponse.WordResponse;
import com.framgia.elsytem.mypackage.Constants;
import com.framgia.elsytem.mypackage.SessionManager;
import com.framgia.elsytem.mypackage.URL;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LearnedActivity extends AppCompatActivity {
    ListView list;
    String url = "";
    String category_id = "14";
    String option = "all_word";
    String page = "1";
    Request request;
    Response response = null;
    String responseData = null;
    WordResponse wa;
    String newURL = "";
    int totalpage = 0;
    Gson gson;
    private com.framgia.elsytem.mypackage.Constants mConstant;
    OkHttpClient okHttpClient;
    String token;
    SessionManager sessionManager;
    HashMap<String, String> user;
    ArrayList<WordResponse.WordsEntity> wordName = new ArrayList<>();
    ArrayList<WordReturnByCategory> item = new ArrayList<>();
    Spinner spinnerCategory;
    ArrayList<CategoryResponse.CategoriesEntity> categoriesName = new ArrayList<CategoryResponse.CategoriesEntity>();
    ArrayList<CategoriesReturnFromPages> catitem=new ArrayList<>();
    int cattotalpage=0;
    Response catresponse = null;
    String catresponseData = null;
    ArrayList<String> catal=new ArrayList<>();
    String catpage="1";
    String catnewURL="";
    String curl="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learned);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        spinnerCategory= (Spinner) findViewById(R.id.spinnerCategory);
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails();
        token = user.get(mConstant.KEY_AUTH_TOKEN);
        list = (ListView) findViewById(R.id.learned);
        okHttpClient = new OkHttpClient();
        URL ur = new URL();
        url = ur.getWordfetchurl().toString();
        try {
            String newURL = wordURLBody(page);
            new HttpAsyncWord().execute(newURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        curl=ur.getCategoryFetchURL().toString();
        try {
            String catnewURL=catURLBody(catpage);
            new HttpAsyncCategory().execute(catnewURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this,android.R.layout
                .simple_spinner_item,catal);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(dataAdapter);
    }

    public String  catURLBody(String page) throws Exception {
        LinkedHashMap<String, String> catpara = new LinkedHashMap<>();
        catpara.put(Constants.AUTH_TOKEN, token);
        catpara.put(Constants.PAGE, page);
        catnewURL = makeUrlWithParams(curl, catpara);
        return catnewURL;

    }

    private class HttpAsyncCategory extends AsyncTask<String, Void,Integer> {
        private ProgressDialog mDialog;

        @Override
        protected Integer doInBackground(String... urls) {
            try {
                Request request = new Request.Builder().url(urls[0]).get().build();
                Log.e("token ", token);
                catresponse = okHttpClient.newCall(request).execute();
                catresponseData = catresponse.body().string();
                Log.e("response ", catresponseData);
                gson = new Gson();
                CategoryResponse ca = gson.fromJson(catresponseData, CategoryResponse.class);
                cattotalpage=ca.getTotal_pages();
                Log.e("totalpage",""+cattotalpage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return cattotalpage;
        }

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(LearnedActivity.this);
            mDialog.setTitle(R.string.categoriesactivity_settitle);
            mDialog.setMessage("please wait");
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(true);
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Integer i) {
            mDialog.dismiss();
            while(i>=1)
            {
                String catrl=null;
                try{
                    catrl=catURLBody(String.valueOf(i));
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                new HttpAsyncCategoryshow().execute(catrl);
                i--;
            }
        }
    }
    private class HttpAsyncCategoryshow extends AsyncTask<String, Void,ArrayList<CategoryResponse
            .CategoriesEntity>> {
        private ProgressDialog mDialog;

        @Override
        protected ArrayList<CategoryResponse.CategoriesEntity> doInBackground(String... urls) {
            try {
                Request request = new Request.Builder().url(urls[0]).get().build();
                catresponse = okHttpClient.newCall(request).execute();
                catresponseData = catresponse.body().string();
                gson = new Gson();
                CategoryResponse ca = gson.fromJson(catresponseData, CategoryResponse.class);
                categoriesName= (ArrayList<CategoryResponse.CategoriesEntity>) ca
                        .getCategories();
                for(int j=0;j<categoriesName.size();j++)
                    Log.e("value",""+categoriesName.get(j).getName());
                cattotalpage=ca.getTotal_pages();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return categoriesName;
        }

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(LearnedActivity.this);
            mDialog.setTitle(R.string.categoriesactivity_settitle);
            mDialog.setMessage("please wait");
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(true);
            mDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<CategoryResponse.CategoriesEntity> categoriesName) {
            mDialog.dismiss();
            for(int i=0;i<categoriesName.size();i++)
            {
                String key=categoriesName.get(i).getName();
                Log.e("key", key);
                Integer value=categoriesName.get(i).getId();
                catitem.add(new CategoriesReturnFromPages(key,value));
                catal.add(key);
            }

        }
    }

    public String wordURLBody(String page) throws Exception {
        LinkedHashMap<String, String> para = new LinkedHashMap<>();
        para.put(Constants.CCATEGORY_ID, category_id);
        para.put(Constants.OPTION, option);
        para.put(Constants.PAGE, page);
        para.put(Constants.AUTH_TOKEN, token);
        newURL = makeUrlWithParams(url, para);
        return newURL;
    }

    public static String makeUrlWithParams(String url, LinkedHashMap<String, String> params)
            throws Exception {
        if (TextUtils.isEmpty(url) || params == null || params.isEmpty())
            return url;
        String newUrl = new String(url);
        for (Map.Entry entry : params.entrySet()) {
            if (TextUtils.isEmpty(entry.getValue().toString()))
                continue;
            newUrl = concat_url(newUrl, entry.getKey().toString(), entry.getValue().toString());
        }
        return newUrl;
    }

    private static String concat_url(String url, String k, String p)
            throws UnsupportedEncodingException {
        String new_url = new String(url);
        if (url.indexOf("?") == -1) {
            new_url =
                    new_url.concat("?").concat(k).concat("=").concat(URLEncoder.encode(p,
                            "UTF-8"));
        } else if (url.endsWith("?") || url.endsWith("&")) {
            new_url = new_url.concat(k).concat("=").concat(URLEncoder.encode(p,
                    "UTF-8"));
        } else {
            new_url =
                    new_url.concat("&").concat(k).concat("=").concat(URLEncoder.encode(p,
                            "UTF-8"));
        }
        return new_url;
    }

    private class HttpAsyncWord extends AsyncTask<String, Void, Integer> {
        private ProgressDialog mDialog;

        @Override
        protected Integer doInBackground(String... urls) {
            try {
                request = new Request.Builder().url(urls[0]).get().build();
                Log.e("token ", token);
                response = okHttpClient.newCall(request).execute();
                responseData = response.body().string();
                Log.e("response ", responseData);
                gson = new Gson();
                wa = gson.fromJson(responseData, WordResponse.class);
                totalpage = wa.getTotal_pages();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return totalpage;
        }

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(LearnedActivity.this);
            mDialog.setTitle("Learned word loading");
            mDialog.setMessage("please wait");
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(true);
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Integer i) {
            mDialog.dismiss();
            while (i >= 1) {
                String newURL = null;
                try {
                    newURL = wordURLBody(String.valueOf(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new HttpAsyncWordShow().execute(newURL);
                i--;
            }
        }
    }

    private class HttpAsyncWordShow extends AsyncTask<String, Void, ArrayList<WordResponse
            .WordsEntity>> {
        private ProgressDialog mDialog;

        @Override
        protected ArrayList<WordResponse.WordsEntity> doInBackground(String... urls) {

            try {
                request = new Request.Builder().url(urls[0]).get().build();
                response = okHttpClient.newCall(request).execute();
                responseData = response.body().string();
                gson = new Gson();
                wa = gson.fromJson(responseData, WordResponse.class);
                wordName = (ArrayList<WordResponse.WordsEntity>) wa.getWords();
                for (int j = 0; j < wordName.size(); j++) {
                    Log.e("value", "" + wordName.get(j).getContent());
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return wordName;
        }

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(LearnedActivity.this);
            mDialog.setTitle("Learned word loading");
            mDialog.setMessage("please wait");
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(true);
            mDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<WordResponse.WordsEntity> wordName) {
            mDialog.dismiss();
            for (int i = 0; i < wordName.size(); i++) {
                String key = wordName.get(i).getContent();
                Log.e("content ", key);
                Integer value = wordName.get(i).getId();
                item.add(new WordReturnByCategory(key, value));
            }
            WordAdapter cad = new WordAdapter(getApplication(), item);
            list.setAdapter(cad);
            cad.notifyDataSetChanged();
        }
    }
}