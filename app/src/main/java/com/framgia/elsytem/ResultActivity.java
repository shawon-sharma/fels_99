package com.framgia.elsytem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.framgia.elsytem.model.Result;
import com.framgia.elsytem.mypackage.Constants;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    ListView resultlist;
    ArrayList<Result> arrayList;
    ArrayAdapter<Result> resAdapter;
    TextView number;
    TextView lesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        resultlist = (ListView) findViewById(R.id.result_list);
        lesson=(TextView)findViewById(R.id.lesson);
        Intent intent=getIntent();
        String category_name=intent.getStringExtra(Constants.CATEGORY_NAME);
        number=(TextView)findViewById(R.id.correctnumber);
        databaseHelper = new DatabaseHelper(this);
        long correct=databaseHelper.getCorrectAnswers(category_name);
        int count=databaseHelper.getAnswerCounts(category_name);
        number.setText(correct+"/"+count);
        lesson.setText(category_name);
        arrayList = databaseHelper.getCategoryResults(category_name);
        resAdapter = new ResultAdapter(getApplication(),arrayList);
        resultlist.setAdapter(resAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_ok:
                startActivity(new Intent(getApplication(), ProfileActivity.class));
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
