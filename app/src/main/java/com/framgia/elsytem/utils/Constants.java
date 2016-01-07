package com.framgia.elsytem.utils;

import android.app.ProgressDialog;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.framgia.elsytem.adapters.WordAdapter;
import com.framgia.elsytem.jsonResponse.WordResponse;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by avishek on 12/22/15.
 */
public class Constants {
    /**
     * UserFunctions' and SessionManager's common keys
     */
    // User id
    public static final String KEY_ID = "id";
    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";
    // Avatar's image decodable string
    public static final String KEY_AVATAR = "avatar";
    // remember token
    public static final String KEY_AUTH_TOKEN = "auth_token";
    // remember me
    public static final String KEY_REMEMBER_ME = "remember_Me";
    // UserFunctions TAG
    // User key
    public static final String KEY_USER = "user";
    public static final String KEY_SESSION = "session";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PASSWORD_CONFIRMATION = "password_confirmation";
    public static final String KEY_MESSAGE = "message";
    public static final String TAG_USER_FUNCTIONS = "UserFunctions";
    public static final String AUTH_TOKEN = KEY_AUTH_TOKEN;
    public static final String PAGE = "page";
    public static final String CCATEGORY_ID = "category_id";
    public static final String OPTION = "option";
    public static final String ANSWER_ID = "answer_id";
    public static final String QUESTION = "Questions ";
    public static final String LESSON = "lesson";
    public static final String RESULT_ATTRIBUTE = "results_attributes";
    public static final String ID = "id";
    public static final int TWENTY = 20;
    public static final int STATUS = 200;
    // file name of the avatar in the phone storage
    public static final String KEY_AVATAR_FILE_NAME = "_avatar.jpg";
    public static final String all = "all_word";
    public static final String learned = "learned";
    public static final String NOT_LEARNED = "no_learn";
    // image dimensions
    public static final int AVATAR_WIDTH_HEIGHT_AND_RADIUS = 100;
    public static final int ROUNDED_AVATAR_MARGIN = 0;
    public static final String Learned_words = "You're learned ";
    public static final String OPTION_ALL = "ALL";
    public static final String OPTION_LEARN = "LEARNED";
    public static final String OPTION_NOT_LEARN = "NOT learn";
    public static final String FILEPATH = "MyInvoices";
    public static final String WORD_LIST = "all word";
    public static final String ONE = "1";
    public static final int INT_ONE = 1;
    public static final int ZERO = 0;
    public static final int TWO = 2;
    public static final int THREE = 3;
    public static final int FOUR = 4;
    public static final String DOT_JSON = ".json";
    public static final String TEST_FOLDER = "TestFolder";
    public static final String DOT_PDF = ".pdf";
    public static final String VIETNAM = "Vietnam";
    public static final String CHARSET = "application/json; charset=utf-8";
    public static final String DATE_FORMAT = "dd-MMM-yyyy";
    public static final String MESSAGE = "message";
    public static final String LOGIN_ACTIVITY = "LoginActivity";
    public static final String REGISTER_ACTIVITY = "RegisterActivity";
    public static final String PROFILE_ACTIVITY = "ProfileActivity";
    public static final String UPDATE_PROFILE_ACTIVITY = "UpdateProfileActivity";
    public static final String INFO = "Provide the required information!";
    public static final String ROUNDED_TRANSFORMATION = "RoundedTransformation(radius=";
    public static final String MARGIN = ", margin=";
    public static final String DIAMETER = ", diameter=";
    public static final String CORNER_TYPE = ", cornerType=";
    public static String CATEGORY_NAME = "category_name";
    public static int ANSWER_1 = 0;
    public static int ANSWER_2 = 1;
    public static int ANSWER_3 = 2;
    public static int ANSWER_4 = 3;
    public static int CATEGORY_ID = 1;
    public static int ACTIVITY_SWITCH = 1;

    //categories activity variable name declaration
    public static int catTotalPage = Constants.ZERO;
    public static ListView list;
    public static String url;
    public static Gson gson;
    public static String token;
    public static String catNewURL;
    public static String catPage = Constants.ONE;
    public static OkHttpClient okHttpClient;
    public static SessionManager sessionManager;
    public static Response catResponse = null;
    public static String catResponseData = null;
    //learned activity variable name declaration

    public static int n = Constants.ZERO;
    public static  String word_list = Constants.WORD_LIST;
    public static String curl;
    public static File myFile;
    public static String newURL;
    public static Request request;
    public static Response response;
    public static int mWidth;
    public static String responseData;
    public static String categoryNewUrl;
    public static Spinner spinnerOption;
    public static Spinner spinnerCategory;
    public static WordResponse wordResponse;
    public static Response categoryResponse;
    public static String page = Constants.ONE;
    public static String categoryResponseData;
    public static WordAdapter categoryAdapter;
    public static HashMap<String, String> user;
    public static String option = Constants.all;
    public static int totalPage = Constants.ZERO;
    public static ProgressDialog mDialog;
    public static ArrayAdapter<String> dataAdapter;
    public static String category_id = Constants.ONE;
    public static int cat_total_page = Constants.ZERO;
    public static String categoryPageNumber = Constants.ONE;
    public static LinkedHashMap<String, String> categoryClick;
    public static String filepath = Constants.FILEPATH;
    public static Boolean mIsSpinnerFirstCall, mIsSpinnerOption;




}
