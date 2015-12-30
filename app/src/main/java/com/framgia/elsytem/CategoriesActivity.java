package com.framgia.elsytem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.framgia.elsytem.jsonResponse.CategoryResponse;
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

public class CategoriesActivity extends AppCompatActivity {
    ListView list;
    String url = "";
    String newURL="";
    Gson gson;
    TextView category;
    String page="1";
    private com.framgia.elsytem.mypackage.Constants mConstant;
    OkHttpClient okHttpClient;
    String token;
    SessionManager sessionManager;
    HashMap<String, String> user;
    ArrayList<CategoryResponse.CategoriesEntity> categoriesName = new ArrayList<CategoryResponse.CategoriesEntity>();
    ArrayList<CategoriesReturnFromPages> item=new ArrayList<>();
    int totalpage=0;
    Response response = null;
    String responseData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails();
        token = user.get(mConstant.KEY_AUTH_TOKEN);
        category = (TextView) findViewById(R.id.textHeader);
        list = (ListView) findViewById(R.id.listCategory);
        okHttpClient = new OkHttpClient();
        URL ur=new URL();
        url=ur.getCategoryFetchURL().toString();
        try {
            String newURL=shawon(page);
            new HttpAsyncCategory().execute(newURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int category_id = categoriesName.get(position).getId();
                Intent intent = new Intent(getApplication(), QuestionActivity.class);
                intent.putExtra(String.valueOf(Constants.CATEGORY_ID), category_id);
                startActivity(intent);
            }
        });
    }

    public String  shawon(String page) throws Exception {
        LinkedHashMap<String, String> para = new LinkedHashMap<>();
        para.put("auth_token", token);
        para.put("page", page);
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

    private class HttpAsyncCategory extends AsyncTask<String, Void,Integer> {
        private ProgressDialog mDialog;

        @Override
        protected Integer doInBackground(String... urls) {
            try {
                Request request = new Request.Builder().url(urls[0]).get().build();
                Log.e("token ", token);
                response = okHttpClient.newCall(request).execute();
                responseData = response.body().string();
                Log.e("response ", responseData);
                gson = new Gson();
                CategoryResponse ca = gson.fromJson(responseData, CategoryResponse.class);
                totalpage=ca.getTotal_pages();
                Log.e("totalpage",""+totalpage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return totalpage;
        }

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(CategoriesActivity.this);
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
                String rl=null;
                try{
                    rl=shawon(String.valueOf(i));
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                new HttpAsyncCategoryshow().execute(rl);
                i--;
            }
        }
    }
    //*********************************************************************************
    private class HttpAsyncCategoryshow extends AsyncTask<String, Void,ArrayList<CategoryResponse
            .CategoriesEntity>> {
        private ProgressDialog mDialog;

        @Override
        protected ArrayList<CategoryResponse.CategoriesEntity> doInBackground(String... urls) {
            try {
                Request request = new Request.Builder().url(urls[0]).get().build();
                response = okHttpClient.newCall(request).execute();
                responseData = response.body().string();
                gson = new Gson();
                CategoryResponse ca = gson.fromJson(responseData, CategoryResponse.class);
                categoriesName= (ArrayList<CategoryResponse.CategoriesEntity>) ca.getCategories();
                for(int j=0;j<categoriesName.size();j++)
                    Log.e("value",""+categoriesName.get(j).getName());
                totalpage=ca.getTotal_pages();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return categoriesName;
        }

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(CategoriesActivity.this);
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
                Log.e("key",key);
                Integer value=categoriesName.get(i).getId();
                item.add(new CategoriesReturnFromPages(key,value));
            }
            CategoryAdapter cad = new CategoryAdapter(getApplicationContext(),item);
            list.setAdapter(cad);
            cad.notifyDataSetChanged();
        }
    }
}


