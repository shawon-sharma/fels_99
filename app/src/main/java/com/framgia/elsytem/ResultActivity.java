package com.framgia.elsytem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.framgia.elsytem.model.Result;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    ListView resultlist;
    ArrayList<Result> arrayList;
    ArrayAdapter<Result> resAdapter;
    TextView correct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        resultlist=(ListView)findViewById(R.id.result_list);
        correct=(TextView)findViewById(R.id.correctnumber);
        databaseHelper=new DatabaseHelper(this);
        arrayList=databaseHelper.getresult();
        long number_correct=databaseHelper.getCorrectAnswers();
        correct.setText(number_correct+"/"+arrayList.size());
        resAdapter=new ResultAdapter(getApplication(),arrayList);
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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
