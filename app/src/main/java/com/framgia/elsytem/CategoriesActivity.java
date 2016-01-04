package com.framgia.elsytem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.framgia.elsytem.jsonResponse.CategoryResponse;
import com.framgia.elsytem.model.Done;
import com.framgia.elsytem.utils.Constants;
import com.framgia.elsytem.utils.SessionManager;
import com.framgia.elsytem.utils.Url;
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
    String catNewURL = "";
    Gson gson;
    TextView category;

    String catPage = "1";
    private com.framgia.elsytem.utils.Constants mConstant;

    OkHttpClient okHttpClient;
    String token;
    SessionManager sessionManager;
    HashMap<String, String> user;
    ArrayList<CategoryResponse.CategoriesEntity> categoriesName = new ArrayList<CategoryResponse.CategoriesEntity>();
    //ArrayList<CategoriesReturnFromPages> catitem=new ArrayList<>();
    ArrayList<CategoriesReturnFromPages> catItem = new ArrayList<>();
    int catTotalPage = 0;
    Response catResponse = null;
    String catResponseData = null;
    ArrayList<String> catAll = new ArrayList<>();
    public static  ArrayList<Done>switch_activity=new ArrayList<>();

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

        url = Url.categoryFetchURL;

        try {
            String catnewURL = catURLBody(catPage);
            new HttpAsyncCategory().execute(catnewURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int category_id = catItem.get(position).getCategoriesId();
                String category_name = catItem.get(position).getCategoriesName();
                Intent intent = new Intent(getApplication(), QuestionActivity.class);
                intent.putExtra(String.valueOf(Constants.CATEGORY_ID), category_id);
                intent.putExtra(Constants.CATEGORY_NAME, category_name);
                startActivity(intent);
            }
        });
    }

    public String catURLBody(String page) throws Exception {
        LinkedHashMap<String, String> catPara = new LinkedHashMap<>();
        catPara.put(Constants.AUTH_TOKEN, token);
        catPara.put(Constants.PAGE, page);
        catNewURL = makeUrlWithParams(url, catPara);
        return catNewURL;

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

    private class HttpAsyncCategory extends AsyncTask<String, Void, Integer> {
        private ProgressDialog mDialog;

        @Override
        protected Integer doInBackground(String... urls) {
            try {
                Request request = new Request.Builder().url(urls[0]).get().build();
                catResponse = okHttpClient.newCall(request).execute();
                catResponseData = catResponse.body().string();
                gson = new Gson();
                CategoryResponse ca = gson.fromJson(catResponseData, CategoryResponse.class);
                catTotalPage = ca.getTotal_pages();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return catTotalPage;
        }
        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(CategoriesActivity.this);
            mDialog.setTitle(R.string.categoriesactivity_settitle);
            mDialog.setMessage(getString(R.string.please_wait));
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(true);
            mDialog.show();
        }
        @Override
        protected void onPostExecute(Integer i) {

            mDialog.dismiss();
            while (i >= 1) {
                String catrl = null;
                try {
                    catrl = catURLBody(String.valueOf(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new HttpAsyncCategoryshow().execute(catrl);
                i--;
            }
            mDialog.dismiss();
        }
    }

    private class HttpAsyncCategoryshow extends AsyncTask<String, Void, ArrayList<CategoryResponse
            .CategoriesEntity>> {
        @Override
        protected ArrayList<CategoryResponse.CategoriesEntity> doInBackground(String... urls) {
            try {
                Request request = new Request.Builder().url(urls[0]).get().build();
                catResponse = okHttpClient.newCall(request).execute();
                catResponseData = catResponse.body().string();
                gson = new Gson();
                CategoryResponse ca = gson.fromJson(catResponseData, CategoryResponse.class);
                categoriesName = (ArrayList<CategoryResponse.CategoriesEntity>) ca
                        .getCategories();
                catTotalPage = ca.getTotal_pages();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return categoriesName;
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onPostExecute(ArrayList<CategoryResponse.CategoriesEntity> categoriesName) {
            for (int i = 0; i < categoriesName.size(); i++) {
                String key = categoriesName.get(i).getName();
                Integer value = categoriesName.get(i).getId();
                String image = categoriesName.get(i).getPhoto();
                catItem.add(new CategoriesReturnFromPages(key, value, image));
                catAll.add(key);
            }
            CategoryAdapter cad = new CategoryAdapter(getApplicationContext(), catItem);
            list.setAdapter(cad);
            cad.notifyDataSetChanged();
        }
    }
}

