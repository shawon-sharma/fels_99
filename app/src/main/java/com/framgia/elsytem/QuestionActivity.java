package com.framgia.elsytem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.elsytem.model.Lesson;
import com.framgia.elsytem.mypackage.Constants;
import com.framgia.elsytem.mypackage.SessionManager;
import com.framgia.elsytem.mypackage.Url;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class QuestionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, View.OnClickListener {
    TextToSpeech mTextToSpeech;
    TextView word;
    ImageButton play;
    OkHttpClient okHttpClient;
    Lesson lesson;
    DatabaseHelper mdDatabaseHelper;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    Button option_1, option_2, option_3, option_4;
    TextView txttword;
    Gson gson;
    String language = "Vietnam";
    int word_number = 0;
    Button[] buttons = new Button[4];
    int chosen_answer = 0;
    String update = null;
    SessionManager sessionManager;
    HashMap<String, String> user;
    String token;
    int lesson_id = 1;
    int category_id = 1;
    TextView questions;
    String category_name;
    ArrayList<Lesson.LessonEntity.WordsEntity> wordsEntityArrayList;
    ArrayList<Lesson.LessonEntity.WordsEntity.AnswersEntity> answersEntityArrayList = new ArrayList<Lesson.LessonEntity.WordsEntity.AnswersEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mdDatabaseHelper = new DatabaseHelper(this);
        questions = (TextView) findViewById(R.id.question_number);
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails();
        token = user.get(Constants.KEY_AUTH_TOKEN);
        option_1 = (Button) findViewById(R.id.choice_1);
        option_1.setOnClickListener(this);
        buttons[0] = option_1;
        option_2 = (Button) findViewById(R.id.choice_2);
        option_2.setOnClickListener(this);
        buttons[1] = option_2;
        option_3 = (Button) findViewById(R.id.choice_3);
        option_3.setOnClickListener(this);
        buttons[2] = option_3;
        option_4 = (Button) findViewById(R.id.choice_4);
        option_4.setOnClickListener(this);
        buttons[3] = option_4;
        txttword = (TextView) findViewById(R.id.word);
        okHttpClient = new OkHttpClient();
        play = (ImageButton) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
        Intent intent = getIntent();
        category_name = intent.getStringExtra(Constants.CATEGORY_NAME);
        getSupportActionBar().setTitle(category_name);
        category_id = intent.getIntExtra(String.valueOf(Constants.CATEGORY_ID), 1);
        String c_id = Integer.toString(category_id);
        String urlasync = Url.url.concat(c_id).concat(Url.url_last);
        new HttpAsyncLesson().execute(urlasync);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTextToSpeech = new TextToSpeech(this, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTextToSpeech.shutdown();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choice_1:
                chosen_answer = Constants.ANSWER_1;
                break;
            case R.id.choice_2:
                chosen_answer = Constants.ANSWER_2;
                break;
            case R.id.choice_3:
                chosen_answer = Constants.ANSWER_3;
                break;
            case R.id.choice_4:
                chosen_answer = Constants.ANSWER_4;
                break;
        }
        createalert();
    }

    public void createalert() {
        final AlertDialog.Builder aBuilder = new AlertDialog.Builder(QuestionActivity.this);
        aBuilder.setMessage(R.string.question_alert_message);
        aBuilder.setIcon(R.drawable.ic_check);
        aBuilder.setPositiveButton(R.string.question_alert_positive,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        long insert = mdDatabaseHelper.createresult(wordsEntityArrayList.get(word_number).getContent(), language, answersEntityArrayList.get(chosen_answer).isIs_correct(), category_name);
                        Log.e("insert", " " + insert);
                        int result_id = wordsEntityArrayList.get(word_number).getResult_id();
                        int answer_id = answersEntityArrayList.get(chosen_answer).getId();
                        boolean state = answersEntityArrayList.get(chosen_answer).isIs_correct();
                        try {
                            update = updatelesson(result_id, answer_id, state);
                            String url_update = Url.update_first.concat(Integer.toString(lesson_id)).concat(Url.update_last);
                            new HttpAsyncUpdate().execute(url_update);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        word_number++;
                        if (word_number < wordsEntityArrayList.size()) {
                            txttword.setText(wordsEntityArrayList.get(word_number).getContent());
                            questions.setText((word_number + 1) + "/" + wordsEntityArrayList.size());
                            answersEntityArrayList = null;
                            answersEntityArrayList = (ArrayList<Lesson.LessonEntity.WordsEntity.AnswersEntity>) wordsEntityArrayList.get(word_number).getAnswers();
                            if (answersEntityArrayList.size() > 0) {
                                for (int j = 0; j < answersEntityArrayList.size(); j++) {
                                    buttons[j].setText(answersEntityArrayList.get(j).getContent());
                                }
                            }
                        } else {
                            Toast.makeText(getApplication(), R.string.question_done, Toast.LENGTH_LONG).show();
                        }
                    }
                });
        aBuilder.setNegativeButton(R.string.question_alert_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        aBuilder.create();
        aBuilder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.STATUS) {
            startActivity(new Intent(getApplication(), CategoriesActivity.class));
        }
    }

    public void speak() {
        String text = txttword.getText().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_done:
                Intent intent = new Intent(getApplication(), ResultActivity.class);
                intent.putExtra(Constants.CATEGORY_NAME, category_name);
                startActivityForResult(intent, Constants.STATUS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTextToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }/* else {
                play.setEnabled(true);
                speak();
            }*/
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    public String updatelesson(int word_id, int answer_id, boolean state) throws JSONException {
        JSONObject parent = new JSONObject();
        parent.put("auth_token", token);
        JSONObject lesson = new JSONObject();
        lesson.put("learned", state);
        JSONArray jsonArray = new JSONArray();
        JSONObject results = new JSONObject();
        results.put("id", word_id);
        results.put("answer_id", answer_id);
        jsonArray.put(results);
        lesson.put("results_attributes", jsonArray);
        parent.put("lesson", lesson);
        return parent.toString();
    }

    private class HttpAsyncUpdate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String json = update;
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(urls[0]).patch(requestBody).build();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String responseData = null;
            try {
                responseData = response.body().string();
                Log.e("response ", responseData);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public String createjson(String token) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("auth_token", token);
        Log.e("token ", token);
        return object.toString();
    }

    private class HttpAsyncLesson extends AsyncTask<String, Void, WordReturn> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(QuestionActivity.this);
            mDialog.setTitle("Lesson");
            mDialog.setMessage("Questions ");
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(true);
            mDialog.show();
        }

        @Override
        protected WordReturn doInBackground(String... urls) {
            String json = null;
            try {
                json = createjson(token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(urls[0]).post(requestBody).build();
            Response response = null;
            String responseData = null;
            WordReturn wordReturn = null;
            try {
                response = okHttpClient.newCall(request).execute();
                responseData = response.body().string();
                Log.e("response ", responseData);
                gson = new Gson();
                Lesson.LessonEntity lessonEntity;
                lesson = gson.fromJson(responseData, Lesson.class);
                lessonEntity = lesson.getLesson();
                lesson_id = lessonEntity.getId();
                wordsEntityArrayList = (ArrayList<Lesson.LessonEntity.WordsEntity>) lessonEntity.getWords();
                answersEntityArrayList = (ArrayList<Lesson.LessonEntity.WordsEntity.AnswersEntity>) wordsEntityArrayList.get(word_number).getAnswers();
                String contain = wordsEntityArrayList.get(word_number).getContent();
                wordReturn = new WordReturn(contain, answersEntityArrayList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return wordReturn;
        }

        @Override
        protected void onPostExecute(WordReturn wordReturn) {
            mDialog.dismiss();
            txttword.setText(wordReturn.contain);
            questions.setText((word_number + 1) + "/" + wordsEntityArrayList.size());
            ArrayList<Lesson.LessonEntity.WordsEntity.AnswersEntity> answers = wordReturn.answersEntities;
            for (int j = 0; j < answers.size(); j++) {
                buttons[j].setText(answers.get(j).getContent());
            }
        }
    }
}