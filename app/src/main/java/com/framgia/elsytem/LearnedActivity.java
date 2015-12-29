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
import com.framgia.elsytem.jsonResponse.WordResponse;
import com.framgia.elsytem.mypackage.SessionManager;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LearnedActivity extends AppCompatActivity {
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
        new HttpAsyncCategory().execute("https://manh-nt.herokuapp.com/words.json?category_id=5&option=all_word");
    }

    private class HttpAsyncCategory extends AsyncTask<String, Void, ArrayList<WordResponse.WordsEntity>> {
        private ProgressDialog mDialog;

        @Override
        protected ArrayList<WordResponse.WordsEntity> doInBackground(String... urls) {
            try {

                Request request = new Request.Builder().url(urls[0]).get().build();
                Response response = null;
                String responseData = null;
                WordReturn wordReturn = null;
                Log.e("token ", token);
                response = okHttpClient.newCall(request).execute();
                responseData = response.body().string();
                Log.e("response ", responseData);
                gson = new Gson();
                WordResponse wa = gson.fromJson(responseData, WordResponse.class);
                wordName = (ArrayList<WordResponse.WordsEntity>)wa.getWords();
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
            WordAdapter cad = new WordAdapter(getApplication(),wordName);
            list = (ListView) findViewById(R.id.listCategory);
            list.setAdapter(cad);
        }
    }
}