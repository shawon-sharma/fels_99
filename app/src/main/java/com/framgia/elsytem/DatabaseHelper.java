package com.framgia.elsytem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.framgia.elsytem.model.Result;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by ahsan on 12/17/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "RESULT.db";
    private static final int DATABASE_VERSION = 1;
    private DatabaseHelper myDbHelper;
    private Context context;
    private SQLiteDatabase mDb;
    /**
     * table result
     */
    private static final String FIELD_ID = "_id";
    private static final String TABLE_RESULT = "result";
    private static final String FIELD_WORD = "word";
    private static final String FIELD_LANGUAGE = "language";
    private static final String FIELD_STATE = "state";
    private static final String FIELD_CATEGORY_NAME="category_name";
    /**
     * status table create statement
     */
    private static final String CREATE_TABLE_RESULT = "CREATE TABLE " + TABLE_RESULT
            + "(" + FIELD_ID + " INTEGER PRIMARY KEY, " + FIELD_WORD + " " +
            "text, "+FIELD_LANGUAGE+" "+"text, "+FIELD_CATEGORY_NAME+" "+"text, "+FIELD_STATE+" integer )";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RESULT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESULT);
        /**Create new tables*/
        onCreate(db);
    }
    public DatabaseHelper open() throws SQLException {
        myDbHelper = new DatabaseHelper(context);
        mDb = myDbHelper.getWritableDatabase();
        return this;
    }
    public void close() {
        if (myDbHelper != null) {
            myDbHelper.close();
        }
    }
    /**
     * Creating result
     */
    public long createresult(String word,String language,boolean state,String category_name) {
        int answer_state=0;
        if(state==true)
            answer_state=1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FIELD_WORD, word);
        values.put(FIELD_CATEGORY_NAME,category_name);
        values.put(FIELD_LANGUAGE, language);
        values.put(FIELD_STATE,answer_state);
        long insert = db.insert(TABLE_RESULT, null, values);
        return insert;
    }
    /**
     * get results
     * @return
     */
    public ArrayList<Result> getresult(){
        SQLiteDatabase database=this.getReadableDatabase();
        ArrayList<Result> arrayList=new ArrayList<Result>();
        String query="Select * from "+TABLE_RESULT;
        Cursor cursor = database.rawQuery(query, null);
        if(cursor!=null & cursor.getCount()>0){
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                int id=cursor.getInt(cursor.getColumnIndex(FIELD_ID));
                String word = cursor.getString(cursor
                        .getColumnIndex(FIELD_WORD));
                String language = cursor.getString(cursor
                        .getColumnIndex(FIELD_LANGUAGE));
                 int state=cursor.getInt(cursor.getColumnIndex(FIELD_STATE));
             arrayList.add(new Result(id,word,language,state));
                cursor.moveToNext();
            }
        }
        return arrayList;
    }

    public ArrayList<Result>getCategoryResults(String category_name){
        SQLiteDatabase database=this.getReadableDatabase();
        ArrayList<Result> arrayList=new ArrayList<Result>();
        String selectQuery = "SELECT * FROM " + TABLE_RESULT + " WHERE " + FIELD_CATEGORY_NAME + " = '"+category_name+"'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if(cursor!=null & cursor.getCount()>0){
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                int id=cursor.getInt(cursor.getColumnIndex(FIELD_ID));
                String word = cursor.getString(cursor
                        .getColumnIndex(FIELD_WORD));
                String language = cursor.getString(cursor
                        .getColumnIndex(FIELD_LANGUAGE));
                int state=cursor.getInt(cursor.getColumnIndex(FIELD_STATE));
                arrayList.add(new Result(id,word,language,state));
                cursor.moveToNext();
            }
        }
        return arrayList;
    }

    public long getCorrectAnswers(String category_name){
        SQLiteDatabase database=this.getReadableDatabase();
        long correct=0;
        ArrayList<Result> arrayList=new ArrayList<Result>();
        String query="Select * from "+TABLE_RESULT+" where "+FIELD_STATE+" = " +"1"+ " and "+FIELD_CATEGORY_NAME + " = '"+category_name+"'";
        Cursor cursor = database.rawQuery(query, null);
        if(cursor!=null & cursor.getCount()>0){
             correct=cursor.getCount();
        }
        return correct;
    }
    public int getAnswerCounts(String category_name) {
        String countQuery = "SELECT  * FROM " + TABLE_RESULT +" WHERE "+ FIELD_CATEGORY_NAME + " = '"+category_name+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }


    /**
     * closing database
     */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
