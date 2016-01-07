package com.framgia.elsytem.utils;

import android.util.Log;

import com.framgia.elsytem.model.Profile;
import com.framgia.elsytem.model.User;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

/**
 * Created by avishek on 12/11/15.
 */
public class UserFunctions {
    public static final MediaType JSON
            = MediaType.parse(Constants.CHARSET);
    static OkHttpClient okHttpClient;

    public UserFunctions() {
        okHttpClient = new OkHttpClient();
    }

    /**
     * Function to show user
     */
    public String showUser(String url, String auth_token) {
        String result = "";
        try {
            result = okHttpClient.
                    newCall(new Request.Builder()
                            .url(url + "?" + Constants.KEY_AUTH_TOKEN + "=" + auth_token)
                            .get()
                            .build()).execute().body().string();
        } catch (Exception e) {
            Log.e(Constants.TAG_USER_FUNCTIONS, e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * Function to Login
     **/
    public String signIn(String url, User user, int isRememberMeChecked) {
        String result = "";
        try {
            /**
             * OkHttp
             */
            // 1. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.KEY_EMAIL, user.getEmail());
            jsonObject.put(Constants.KEY_PASSWORD, user.getPassword());
            jsonObject.put(Constants.KEY_REMEMBER_ME, isRememberMeChecked);
            // 2. sending request and converting JSON response to String
            result = okHttpClient.newCall(new Request.Builder().url(url).post(RequestBody.create
                    (JSON, new JSONObject().put(Constants.KEY_SESSION, jsonObject).toString())).build())
                    .execute().body().string();
        } catch (Exception e) {
            Log.e(Constants.TAG_USER_FUNCTIONS, e.getLocalizedMessage());
        }
        // 4. return result
        return result;
    }

    /**
     * Sign Up
     */
    public String signUp(String url, User user) {
        String result = "";
        try {
            /**
             * OkHttp
             */
            // 1. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.KEY_NAME, user.getName());
            jsonObject.put(Constants.KEY_EMAIL, user.getEmail());
            jsonObject.put(Constants.KEY_PASSWORD, user.getPassword());
            jsonObject.put(Constants.KEY_PASSWORD_CONFIRMATION, user.getPassword_confirmation());
            // 2. sending request and converting JSON response to String
            result = new JSONObject(okHttpClient.newCall(new Request.Builder().url(url).post
                    (RequestBody.create(JSON, new JSONObject().put(Constants.KEY_USER, jsonObject)
                                    .toString()
                    )).build()).execute().body().string()).getString(Constants.KEY_MESSAGE);
        } catch (Exception e) {
            Log.e(Constants.TAG_USER_FUNCTIONS, e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * function to sign out
     */
    public String signOut(String url) {
        String result = "";
        try {
            result = new JSONObject(okHttpClient.newCall(new Request.Builder().url(url).delete()
                    .build()).execute().body().string()).getString(Constants.KEY_MESSAGE);
        } catch (Exception e) {
            Log.e(Constants.TAG_USER_FUNCTIONS, e.getLocalizedMessage());
        }
        return result;
    }

    public String updateProfile(String url, Profile profile) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.KEY_NAME, profile.getName());
            jsonObject.put(Constants.KEY_PASSWORD, profile.getNew_password());
            jsonObject.put(Constants.KEY_PASSWORD_CONFIRMATION, profile.getPassword_confirmation());
            jsonObject.put(Constants.KEY_AVATAR, profile.getAvatar());
            JSONObject jsonObjectUser = new JSONObject();
            jsonObjectUser.put(Constants.KEY_AUTH_TOKEN, profile.getAuthToken());
            jsonObjectUser.put(Constants.KEY_USER, jsonObject);
            result = okHttpClient.newCall(new Request.Builder().url(url).patch(RequestBody.create
                    (JSON, jsonObjectUser.toString())).build()).execute().body().string();
        } catch (Exception e) {
            Log.e(Constants.TAG_USER_FUNCTIONS, e.getLocalizedMessage());
        }
        return result;
    }
}
