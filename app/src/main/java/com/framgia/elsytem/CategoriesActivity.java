package com.framgia.elsytem;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.framgia.elsytem.jsonResponse.CategoryResponse;
import com.framgia.elsytem.mypackage.SessionManager;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CategoriesActivity extends AppCompatActivity {
    ListView list;
    CategoryResponse categoryResponse;
    CategoryAdapter categoryAdapter;
    String url = "";
    final int pageno = 1;
    Gson gson;
    TextView category;
    private com.framgia.elsytem.mypackage.Constants mConstant;
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    OkHttpClient okHttpClient;
    String token;
    SessionManager sessionManager;
    HashMap<String, String> user;
    ArrayList<CategoryResponse.CategoriesEntity> categoriesName = new ArrayList<CategoryResponse.CategoriesEntity>();
    String cat;

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
        new HttpAsyncCategory().execute("https://manh-nt.herokuapp.com/categories.json?auth_token=" + token);
    }

    public String createjson(String token) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("page", pageno);
        object.put("auth_token", token);
        Log.e("token ", token + " / " + object.toString());
        return object.toString();
    }

    private class HttpAsyncCategory extends AsyncTask<String, Void, ArrayList<CategoryResponse.CategoriesEntity>> {
        private ProgressDialog mDialog;

        @Override
        protected ArrayList<CategoryResponse.CategoriesEntity> doInBackground(String... urls) {
            try {
                String json = null;
                json = createjson(token);
                RequestBody requestBody = RequestBody.create(JSON, json);
                Request request = new Request.Builder().url(urls[0]).get().build();
                Response response = null;
                String responseData = null;
                WordReturn wordReturn = null;
                Log.e("token ", token);
                response = okHttpClient.newCall(request).execute();
                responseData = response.body().string();
                Log.e("response ", responseData);
                gson = new Gson();
                CategoryResponse.CategoriesEntity catt = new CategoryResponse.CategoriesEntity();
                CategoryResponse ca = gson.fromJson(responseData, CategoryResponse.class);
                categoriesName = (ArrayList<CategoryResponse.CategoriesEntity>) ca.getCategories();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
            CategoryAdapter cad = new CategoryAdapter(getApplicationContext(), categoriesName);
            list = (ListView) findViewById(R.id.listCategory);
            list.setAdapter(cad);
        }
    }
}



