package com.framgia.elsytem;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.framgia.elsytem.jsonResponse.CategoryResponse;
import com.framgia.elsytem.jsonResponse.WordResponse;
import com.framgia.elsytem.mypackage.SessionManager;
import com.framgia.elsytem.mypackage.URL;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
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
    CategoryResponse categoryResponse;
    CategoryAdapter categoryAdapter;
    String url = "";
    final int pageno = 1;
    String category_id = "14";
    String option = "all_word";
    static String page = "1";
    Request request;
    Response response = null;
    String responseData = null;
    WordResponse wa;
    Gson gson;
    TextView category;
    private com.framgia.elsytem.mypackage.Constants mConstant;
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    OkHttpClient okHttpClient;
    String token;
    SessionManager sessionManager;
    HashMap<String, String> user;
    ArrayList<WordResponse.WordsEntity> wordName = new ArrayList<>();
    String cat;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails();
        token = user.get(mConstant.KEY_AUTH_TOKEN);
        category = (TextView) findViewById(R.id.textHeader);
        list = (ListView) findViewById(R.id.learned);
        okHttpClient = new OkHttpClient();
        URL ur = new URL();
        url = ur.getUrl().toString();

        LinkedHashMap<String, String> para = new LinkedHashMap<>();
        para.put("category_id", category_id);
        para.put("option", option);
        para.put("page", page);
        para.put("auth_token", token);
        String newURL = null;
        try {
            newURL = makeUrlWithParams(url, para);
            new HttpAsyncCategory().execute(newURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private class HttpAsyncCategory extends AsyncTask<String, Void, ArrayList<WordResponse
            .WordsEntity>> {
        private ProgressDialog mDialog;

        @Override
        protected ArrayList<WordResponse.WordsEntity> doInBackground(String... urls) {
            try {
                request = new Request.Builder().url(urls[0]).get().build();

                Log.e("token ", token);
                response = okHttpClient.newCall(request).execute();
                responseData = response.body().string();
                Log.e("response ", responseData);
                gson = new Gson();
                 wa = gson.fromJson(responseData, WordResponse.class);
                wordName = (ArrayList<WordResponse.WordsEntity>) wa.getWords();
                int totalpage=wa.getTotal_pages();
                if(totalpage>1)
                {
                    for(int i=2;i<=totalpage;i++)
                    {
                        page=String.valueOf(i);
                        request = new Request.Builder().url(urls[0]).get().build();
                        response = okHttpClient.newCall(request).execute();
                        responseData = response.body().string();
                        gson = new Gson();
                        wa = gson.fromJson(responseData, WordResponse.class);
                        wordName = (ArrayList<WordResponse.WordsEntity>) wa.getWords();
                    }
                }

            } catch (IOException e) {
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
            WordAdapter cad = new WordAdapter(getApplication(), wordName);
            list = (ListView) findViewById(R.id.listCategory);
            list.setAdapter(cad);
        }
    }
}