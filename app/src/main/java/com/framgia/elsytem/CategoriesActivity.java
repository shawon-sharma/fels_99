package com.framgia.elsytem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.framgia.elsytem.adapters.CategoryAdapter;
import com.framgia.elsytem.jsonResponse.CategoryResponse;
import com.framgia.elsytem.model.Done;
import com.framgia.elsytem.utils.Constants;
import com.framgia.elsytem.utils.Libs;
import com.framgia.elsytem.utils.SessionManager;
import com.framgia.elsytem.utils.Url;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {
    public static ArrayList<Done> switch_activity = new ArrayList<>();
    private ArrayList<CategoryResponse.CategoriesEntity> categoriesName = new ArrayList<>();
    private ArrayList<CategoriesReturnFromPages> catItem = new ArrayList<>();
    private ArrayList<String> catAll = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Constants.sessionManager = new SessionManager(this);
        Constants.user = Constants.sessionManager.getUserDetails();
        Constants.token = Constants.user.get(Constants.KEY_AUTH_TOKEN);
        Constants.list = (ListView) findViewById(R.id.listCategory);
        Constants.okHttpClient = new OkHttpClient();
        Constants.url = Url.categoryFetchURL;

        try {
            String catnewURL = Libs.catURLBody(Constants.catPage);
            new HttpAsyncCategory().execute(catnewURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Constants.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class HttpAsyncCategory extends AsyncTask<String, Void, Integer> {
        private ProgressDialog mDialog;

        @Override
        protected Integer doInBackground(String... urls) {
            try {
                Request request = new Request.Builder().url(urls[0]).get().build();
                Constants.catResponse = Constants.okHttpClient.newCall(request).execute();
                Constants.catResponseData = Constants.catResponse.body().string();
                Constants.gson = new Gson();
                CategoryResponse ca = Constants.gson.fromJson(Constants.catResponseData, CategoryResponse
                        .class);
                Constants.catTotalPage = ca.getTotal_pages();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Constants.catTotalPage;
        }

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(CategoriesActivity.this);
            mDialog.setTitle(R.string.categories_activity_set_title);
            mDialog.setMessage(getString(R.string.please_wait));
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(true);
            mDialog.show();
        }

        @Override
        protected void onPostExecute(Integer i) {

            mDialog.dismiss();
            while (i >= 1) {
                String categoryRl = null;
                try {
                    categoryRl = Libs.catURLBody(String.valueOf(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new HttpAsyncCategoryShow().execute(categoryRl);
                i--;
            }
            mDialog.dismiss();
        }
    }

    private class HttpAsyncCategoryShow extends AsyncTask<String, Void, ArrayList<CategoryResponse
            .CategoriesEntity>> {
        @Override
        protected ArrayList<CategoryResponse.CategoriesEntity> doInBackground(String... urls) {
            try {
                Request request = new Request.Builder().url(urls[0]).get().build();
                Constants.catResponse = Constants.okHttpClient.newCall(request).execute();
                Constants.catResponseData = Constants.catResponse.body().string();
                Constants.gson = new Gson();
                CategoryResponse ca = Constants.gson.fromJson(Constants.catResponseData, CategoryResponse
                        .class);
                categoriesName = (ArrayList<CategoryResponse.CategoriesEntity>) ca
                        .getCategories();
                Constants.catTotalPage = ca.getTotal_pages();
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
                int learnedWords = categoriesName.get(i).getLearned_words();
                catItem.add(new CategoriesReturnFromPages(key, value, image, learnedWords));
                catAll.add(key);
            }
            CategoryAdapter cad = new CategoryAdapter(getApplicationContext(), catItem);
            Constants.list.setAdapter(cad);
            cad.notifyDataSetChanged();
        }
    }
}