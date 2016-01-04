package com.framgia.elsytem.utils;

import android.content.SharedPreferences;

/**
 * Created by avishek on 12/22/15.
 */
public class Constants {
    /**
     * SessionManager's keys
     */
    //User id
    public static final String KEY_ID = "id";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    // Avatar's image decodable string
    public static final String KEY_AVATAR = "avatar";
    // remember token
    public static final String KEY_AUTH_TOKEN = "authToken";
    // remember me
    public static final String KEY_REMEMBER_ME = "rememberMe";
    // UserFunctions TAG
    public static final String TAG_USER_FUNCTIONS = "UserFunctions";

    public static final String AUTH_TOKEN="auth_token";
    public static final String PAGE="page";
    public static final String CCATEGORY_ID ="category_id";
    public static final String OPTION="option";
    public static final String LEARNED ="learned";
    public static final String ID ="id";
    public static final String ANSWER_ID ="answer_id";
    public static final String RESULT_ATTRIBUTES ="results_attributes";
    public static final String LESSON ="lesson";
    public static String CATEGORY_NAME="category_name";
    public static SharedPreferences PREF_STATE = null;
    public static int ANSWER_1=0;
    public static int ANSWER_2=1;
    public static int ANSWER_3=2;
    public static int ANSWER_4=3;
    public static int STATE=0;
    public static int CATEGORY_ID=1;
    public static final int STATUS=200;
    // file name of the avatar in the phone storage
    public static final String KEY_AVATAR_FILE_NAME = "_avatar.jpg";
    public static int ACTIVITY_SWITCH=1;
}
