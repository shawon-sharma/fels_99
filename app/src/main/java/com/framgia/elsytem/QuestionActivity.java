package com.framgia.elsytem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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

import com.framgia.elsytem.jsonResponse.LessonResponse;
import com.framgia.elsytem.model.Done;
import com.framgia.elsytem.utils.Constants;
import com.framgia.elsytem.utils.DatabaseHelper;
import com.framgia.elsytem.utils.SessionManager;
import com.framgia.elsytem.utils.Url;
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
    public static final MediaType JSON
            = MediaType.parse(Constants.CHARSET);
    private TextToSpeech mTextToSpeech;
    private TextView word;
    private ImageButton play;
    private OkHttpClient okHttpClient;
    private LessonResponse lesson;
    private DatabaseHelper mdDatabaseHelper;
    private Button option_1, option_2, option_3, option_4;
    private TextView textWord;
    private Gson gson;
    private String language = Constants.VIETNAM;
    private int word_number = Constants.ZERO;
    private Button[] buttons = new Button[Constants.FOUR];
    private int chosen_answer = Constants.ZERO;
    private String update = null;
    private SessionManager sessionManager;
    private HashMap<String, String> user;
    private String token;
    private int lesson_id = Constants.INT_ONE;
    private int category_id = Constants.INT_ONE;
    private TextView questions;
    private String category_name;
    private int x = Constants.INT_ONE;
    private ArrayList<LessonResponse.LessonEntity.WordsEntity> wordsEntityArrayList;
    private ArrayList<LessonResponse.LessonEntity.WordsEntity.AnswersEntity> answersEntityArrayList = new
            ArrayList<>();

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
        buttons[Constants.ZERO] = option_1;
        option_2 = (Button) findViewById(R.id.choice_2);
        option_2.setOnClickListener(this);
        buttons[Constants.INT_ONE] = option_2;
        option_3 = (Button) findViewById(R.id.choice_3);
        option_3.setOnClickListener(this);
        buttons[Constants.TWO] = option_3;
        option_4 = (Button) findViewById(R.id.choice_4);
        option_4.setOnClickListener(this);
        buttons[Constants.THREE] = option_4;
        textWord = (TextView) findViewById(R.id.word);
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
        mdDatabaseHelper.deleteResult(category_name);
        getSupportActionBar().setTitle(category_name);
        category_id = intent.getIntExtra(String.valueOf(Constants.CATEGORY_ID), Constants.INT_ONE);
        String c_id = Integer.toString(category_id);
        String urlAsync = Url.url.concat(c_id).concat(Url.url_last);
        new HttpAsyncLesson().execute(urlAsync);
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
        createAlert();
    }

    public void createAlert() {
        if (checkWordsNumber()) {
            final AlertDialog.Builder aBuilder = new AlertDialog.Builder(QuestionActivity.this);
            aBuilder.setMessage(R.string.question_alert_message);
            aBuilder.setIcon(R.drawable.ic_check);
            aBuilder.setPositiveButton(R.string.question_alert_positive,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            if (x == Constants.INT_ONE)
                                mdDatabaseHelper.deleteResult(category_name);
                            long insert = mdDatabaseHelper.createresult(wordsEntityArrayList.get(word_number).getContent(), language, answersEntityArrayList.get(chosen_answer).isIs_correct(), category_name);
                            int result_id = wordsEntityArrayList.get(word_number).getResult_id();
                            int answer_id = answersEntityArrayList.get(chosen_answer).getId();
                            boolean state = answersEntityArrayList.get(chosen_answer).isIs_correct();
                            x++;
                            try {
                                update = updateLesson(result_id, answer_id, state);
                                String url_update = Url.update_first.concat(Integer.toString(lesson_id)).concat(Url.update_last);
                                new HttpAsyncUpdate().execute(url_update);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ++word_number;
                            Log.e(getString(R.string.word_before), " " + word_number);
                            if (word_number < wordsEntityArrayList.size()) {
                                textWord.setText(wordsEntityArrayList.get(word_number).getContent());
                                questions.setText((word_number + Constants.INT_ONE) + "/" +
                                        wordsEntityArrayList
                                                .size());
                                answersEntityArrayList = null;
                                answersEntityArrayList = (ArrayList<LessonResponse.LessonEntity
                                        .WordsEntity.AnswersEntity>) wordsEntityArrayList.get(word_number).getAnswers();
                                if (answersEntityArrayList.size() > Constants.ZERO) {
                                    for (int j = Constants.ZERO; j < answersEntityArrayList.size();
                                         j++) {
                                        buttons[j].setText(answersEntityArrayList.get(j).getContent());
                                    }
                                }
                            } else {
                                Intent intent = new Intent(getApplication(), ResultActivity.class);
                                intent.putExtra(Constants.CATEGORY_NAME, category_name);
                                startActivity(intent);
                                finish();
                                dialog.cancel();
                            }
                        }
                    });
            aBuilder.setNegativeButton(R.string.question_alert_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    if (x == Constants.INT_ONE)
                        mdDatabaseHelper.deleteResult(category_name);
                }
            });
            aBuilder.create();
            aBuilder.show();
        }
    }

    public boolean checkWordsNumber() {
        if (word_number < wordsEntityArrayList.size()) {
            return true;
        } else
            return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.STATUS) {
            startActivity(new Intent(getApplication(), CategoriesActivity.class));
        }
    }

    public void speak() {
        String text = textWord.getText().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else
            mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_done:
                long count = mdDatabaseHelper.getAnswerCounts(category_name);
                if (count == Constants.ZERO)
                    Toast.makeText(getApplication(), R.string.question_no_answer, Toast.LENGTH_LONG).show();
                else if (count < Constants.TWENTY) {
                    final AlertDialog.Builder aBuilder = new AlertDialog.Builder(QuestionActivity.this);
                    aBuilder.setMessage(R.string.question_exit);
                    aBuilder.setPositiveButton(R.string.question_alert_positive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Constants.ACTIVITY_SWITCH = Constants.TWO;
                            CategoriesActivity.switch_activity.add(new Done(category_id, Constants.ACTIVITY_SWITCH));
                            Intent intent = new Intent(getApplication(), ResultActivity.class);
                            intent.putExtra(Constants.CATEGORY_NAME, category_name);
                            startActivity(intent);
                            finish();
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
                } else {
                    Constants.ACTIVITY_SWITCH = Constants.TWO;
                    CategoriesActivity.switch_activity.add(new Done(category_id, Constants.ACTIVITY_SWITCH));
                    Intent intent = new Intent(getApplication(), ResultActivity.class);
                    intent.putExtra(Constants.CATEGORY_NAME, category_name);
                    startActivity(intent);
                    finish();
                }
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
            }
        } else {

        }
    }

    public String updateLesson(int word_id, int answer_id, boolean state) throws JSONException {
        JSONObject parent = new JSONObject();
        parent.put(Constants.AUTH_TOKEN, token);
        JSONObject lesson = new JSONObject();
        lesson.put(Constants.learned, state);
        JSONArray jsonArray = new JSONArray();
        JSONObject results = new JSONObject();
        results.put(Constants.ID, word_id);
        results.put(Constants.ANSWER_ID, answer_id);
        jsonArray.put(results);
        lesson.put(Constants.RESULT_ATTRIBUTE, jsonArray);
        parent.put(Constants.LESSON, lesson);
        return parent.toString();
    }

    public String createJson(String token) throws JSONException {
        JSONObject object = new JSONObject();
        object.put(Constants.AUTH_TOKEN, token);
        return object.toString();
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder aBuilder = new AlertDialog.Builder(QuestionActivity.this);
        aBuilder.setMessage(R.string.question_exit);
        aBuilder.setPositiveButton(R.string.question_alert_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
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

    private class HttpAsyncUpdate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String json = update;
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(urls[Constants.ZERO]).patch(requestBody)
                    .build();
            Response response = null;
            try {
                response = okHttpClient.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String responseData = null;
            try {
                responseData = response.body().string();
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

    private class HttpAsyncLesson extends AsyncTask<String, Void, WordReturn> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(QuestionActivity.this);
            mDialog.setTitle(Constants.LESSON);
            mDialog.setMessage(Constants.QUESTION);
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(true);
            mDialog.show();
        }

        @Override
        protected WordReturn doInBackground(String... urls) {
            String json = null;
            try {
                json = createJson(token);
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
                gson = new Gson();
                LessonResponse.LessonEntity lessonEntity;
                lesson = gson.fromJson(responseData, LessonResponse.class);
                lessonEntity = lesson.getLesson();
                lesson_id = lessonEntity.getId();
                wordsEntityArrayList = (ArrayList<LessonResponse.LessonEntity.WordsEntity>) lessonEntity
                        .getWords();
                answersEntityArrayList = (ArrayList<LessonResponse.LessonEntity.WordsEntity
                        .AnswersEntity>) wordsEntityArrayList.get(word_number).getAnswers();
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
            textWord.setText(wordReturn.contain);
            questions.setText((word_number + 1) + "/" + wordsEntityArrayList.size());
            ArrayList<LessonResponse.LessonEntity.WordsEntity.AnswersEntity> answers = wordReturn
                    .answersEntities;
            for (int j = 0; j < answers.size(); j++) {
                buttons[j].setText(answers.get(j).getContent());
            }
        }
    }
}