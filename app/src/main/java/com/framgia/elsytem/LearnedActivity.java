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

import com.framgia.elsytem.jsonResponse.WordResponse;
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
    TextView category;
    private com.framgia.elsytem.mypackage.Constants mConstant;
    OkHttpClient okHttpClient;
    String token;
    SessionManager sessionManager;
    HashMap<String, String> user;
    ArrayList<WordResponse.WordsEntity> wordName = new ArrayList<>();
    ArrayList<WordResponse.WordsEntity> wordId = new ArrayList<>();
    ArrayList<WordReturnByCategory> item = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learned);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails();
        token = user.get(mConstant.KEY_AUTH_TOKEN);
        list = (ListView) findViewById(R.id.learned);
        okHttpClient = new OkHttpClient();
        URL ur = new URL();
        url = ur.getWordfetchurl().toString();
        try {
            String newURL = shawon(page);
            new HttpAsyncCategory().execute(newURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String shawon(String page) throws Exception {
        LinkedHashMap<String, String> para = new LinkedHashMap<>();
        para.put("category_id", category_id);
        para.put("option", option);
        para.put("page", page);
        para.put("auth_token", token);
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

    private class HttpAsyncCategory extends AsyncTask<String, Void, Integer> {
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
                    newURL = shawon(String.valueOf(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new HttpAsyncWord().execute(newURL);
                i--;
            }
        }
    }

    private class HttpAsyncWord extends AsyncTask<String, Void, ArrayList<WordResponse.WordsEntity>> {
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