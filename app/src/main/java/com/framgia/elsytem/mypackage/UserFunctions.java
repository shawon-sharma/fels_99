package com.framgia.elsytem.mypackage;

import android.util.Log;

import com.framgia.elsytem.model.Profile;
import com.framgia.elsytem.model.User;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

/**
 * Created by avishek on 12/11/15.
 */
public class UserFunctions {
    User user;
    Constants constant;
    static OkHttpClient okHttpClient;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public UserFunctions() {
        okHttpClient = new OkHttpClient();
    }

    /**
     * Function to Login
     **/
    public String signIn(String url, User user, int isRememberMeChecked) {
        String result = "";
        String json = "";
        try {
            /**
             * OkHttp
             */
            // 1. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", user.getEmail());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("remember_me", isRememberMeChecked);
            // 2. build parentJsonObject and put the previous object into this one
            JSONObject parentJsonObject = new JSONObject();
            parentJsonObject.put("session", jsonObject);
            // 3. convert JSONObject to JSON to String
            json = parentJsonObject.toString();
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            Log.e(constant.TAG_USER_FUNCTIONS + " I/OStream", e.getLocalizedMessage());
        }
        // 4. return result
        return result;
    }

    /**
     * Sign Up
     */
    public String signUp(String url, User user) {
        String result = "";
        String json = "";
        try {
            /**
             * OkHttp
             */
            // 1. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", user.getName());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("password_confirmation", user.getPassword_confirmation());
            // 2. build parentJsonObject and put the previous object into this one
            JSONObject parentJsonObject = new JSONObject();
            parentJsonObject.put("user", jsonObject);
            // 3. convert JSONObject to JSON to String
            json = parentJsonObject.toString();
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(url).post(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            String jsonData = response.body().string();
            // 4. get the message from response
            result = new JSONObject(jsonData).getString("message");
        } catch (Exception e) {
            Log.e(constant.TAG_USER_FUNCTIONS + " I/OStream", e.getLocalizedMessage());
        }
        return result;
    }

    /**
     * function to sign out
     */
    public String signOut(String url) {
        String result = "";
        String json = "";
        try {
            JSONObject jsonObject = new JSONObject();
            json = jsonObject.toString();
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(url).delete(requestBody).build();
            Response response = okHttpClient.newCall(request).execute();
            String jsonData = response.body().string();
            result = new JSONObject(jsonData).getString("message");
        } catch (Exception e) {
            Log.e(constant.TAG_USER_FUNCTIONS + " I/OStream", e.getLocalizedMessage());
        }
        return result;
    }

    public String updateProfile(String url, Profile profile) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", profile.getName());
            jsonObject.put("password", profile.getNew_password());
            jsonObject.put("password_confirmation", profile.getPassword_confirmation());
            jsonObject.put("avatar", profile.getAvatar());
            JSONObject jsonObjectUser = new JSONObject();
            jsonObjectUser.put("auth_token", profile.getAuthToken());
            jsonObjectUser.put("user", jsonObject);
            result = okHttpClient.newCall(new Request.Builder().url(url).patch(RequestBody.create
                    (JSON, jsonObjectUser.toString())).build()).execute().body().string();
        } catch (Exception e) {
            Log.e(constant.TAG_USER_FUNCTIONS + " I/OStream", e.getLocalizedMessage());
        }
        return result;
    }
}
