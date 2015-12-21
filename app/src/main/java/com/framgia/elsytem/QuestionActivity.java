package com.framgia.elsytem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.framgia.elsytem.model.Lesson;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
public class QuestionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    TextToSpeech mTextToSpeech;
    TextView word;
    ImageButton play;
    OkHttpClient okHttpClient;
    Lesson lesson;
    public static SharedPreferences.Editor editor = null;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    Button option_1, option_2, option_3, option_4;
    TextView txttword;
    Gson gson;
    int word_number = 0;
    Button[]buttons=new Button[4];
    ArrayList<Lesson.LessonEntity.WordsEntity> wordsEntityArrayList;
    ArrayList<Lesson.LessonEntity.WordsEntity.AnswersEntity> answersEntityArrayList=new ArrayList<Lesson.LessonEntity.WordsEntity.AnswersEntity>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.question_title);
        option_1 = (Button) findViewById(R.id.choice_1);
        buttons[0]=option_1;
        option_2 = (Button) findViewById(R.id.choice_2);
        buttons[1]=option_2;
        option_3 = (Button) findViewById(R.id.choice_3);
        buttons[2]=option_3;
        option_4 = (Button) findViewById(R.id.choice_4);
        buttons[3]=option_4;
        txttword = (TextView) findViewById(R.id.word);
        okHttpClient = new OkHttpClient();
        mTextToSpeech = new TextToSpeech(this, this);
        word = (TextView) findViewById(R.id.word);
        play = (ImageButton) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });
        option_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createalert();
            }
        });
        option_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createalert();
            }
        });
        option_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createalert();
            }
        });
        option_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createalert();
            }
        });
        Constants.PREF_STATE = getApplicationContext().getSharedPreferences("MyPref",
                MODE_PRIVATE);
        word_number = Constants.PREF_STATE.getInt("state", 0);
        new HttpAsyncLesson().execute(" https://manh-nt.herokuapp.com/categories/1/lessons.json");
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
                        word_number++;
                        txttword.setText(wordsEntityArrayList.get(word_number).getContent());
                        answersEntityArrayList=null;
                        answersEntityArrayList = (ArrayList<Lesson.LessonEntity.WordsEntity.AnswersEntity>) wordsEntityArrayList.get(word_number).getAnswers();
                        if(answersEntityArrayList.size()>0) {
                            for(int j=0;j<answersEntityArrayList.size();j++) {
                                buttons[j].setText(answersEntityArrayList.get(j).getContent());
                            }
                        }
                        editor = Constants.PREF_STATE.edit();
                        editor.putInt("state",word_number);
                        editor.commit();
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
    public void speak() {
        String text = word.getText().toString();
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
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id== R.id.action_done)
            startActivity(new Intent(getApplication(),ResultActivity.class));
        return super.onOptionsItemSelected(item);
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
    private class HttpAsyncLesson extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String json = "";
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(urls[0]).post(requestBody).build();
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
        protected void onPostExecute(String responseData) {
            try {
                gson = new Gson();
                Lesson.LessonEntity lessonEntity;
                lesson = gson.fromJson(responseData, Lesson.class);
                lessonEntity = lesson.getLesson();
                wordsEntityArrayList = (ArrayList<Lesson.LessonEntity.WordsEntity>) lessonEntity.getWords();
                txttword.setText(wordsEntityArrayList.get(word_number).getContent());
                answersEntityArrayList = (ArrayList<Lesson.LessonEntity.WordsEntity.AnswersEntity>) wordsEntityArrayList.get(word_number).getAnswers();
                for(int j=0;j<answersEntityArrayList.size();j++) {
                    buttons[j].setText(answersEntityArrayList.get(j).getContent());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
