package com.framgia.elsytem;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class WordlistActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Word list Activity";
    Button word;
    OkHttpClient okHttpClient;
    public static final MediaType JSON = MediaType.parse("applicaiton/json;charset=utf-8");
    private int mWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordlist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        okHttpClient = new OkHttpClient();
        word = (Button) findViewById(R.id.back_btn);
        word.setOnClickListener(this);
        Display mDisplay = getWindowManager().getDefaultDisplay();
        mWidth = mDisplay.getWidth();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        new HttpAsyncTask().execute("https://manh-nt.herokuapp.com/relationships.json");
    }

    //**************************new class
    private class HttpAsyncTaskWordlist extends AsyncTask<String, Void, String> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(WordlistActivity.this);
            mDialog.setTitle("Contacting Servers");
            mDialog.setMessage("Updating profile ...");
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(true);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            /*Profile profile = new Profile();
            UserFunctions userFunction = new UserFunctions();
            return userFunction.updateProfile(urls[0], profile);*/
            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            mDialog.dismiss();
            Log.e(TAG, result);
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    //***********************************new class
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            //  return POST(urls[0], person);

            String json = null;
            try {
                // json = create(person);
                // json = createLesson();
                json = getwords();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(urls[0]).post(requestBody).build();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            // Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
            Toast.makeText(getBaseContext(), "response " + result, Toast.LENGTH_LONG).show();
            Log.e("result ", result);
        }
    }

    //********************************getwords
    public String getwords() throws JSONException {
        String result;
        JSONObject parent = new JSONObject();
        JSONArray words = new JSONArray();
        JSONObject word = new JSONObject();
        word.put("id", 1);
        word.put("content", "Framgia");
        JSONArray answer = new JSONArray();
        JSONObject answer_1 = new JSONObject();
        answer_1.put("id", 1);
        answer_1.put("content", "framui");
        answer_1.put("is_correct", false);
        JSONObject answer_2 = new JSONObject();
        answer_2.put("id", 2);
        answer_2.put("content", "framgia");
        answer_2.put("is_correct", true);
        JSONObject answer_3 = new JSONObject();
        answer_3.put("id", 3);
        answer_3.put("content", "frmgia");
        answer_3.put("is_correct", false);
        JSONObject answer_4 = new JSONObject();
        answer_4.put("id", 4);
        answer_4.put("content", "gtgia");
        answer_4.put("is_correct", false);
        answer.put(answer_1);
        answer.put(answer_2);
        answer.put(answer_3);
        answer.put(answer_4);
        word.put("answers", answer);
        words.put(word);
        parent.put("words", words);
        //parent.put("user", jsonObject);
        result = words.toString();
        return result;
    }
}
