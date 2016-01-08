package com.framgia.elsytem.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.framgia.elsytem.LoginActivity;

import java.util.HashMap;

/**
 * Created by avishek on 12/11/15.
 */
public class SessionManager {// Shared Preferences
    // SharedPref file name
    private static final String PREF_NAME = "FELS99";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // if Logged in, then 1
    private static final String LOGIN_TRUE = "1";
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(int id, String name, String email, String avatar, String
            rememberToken, int rememberMe) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        // storing user id in pref
        editor.putString(Constants.KEY_ID, Integer.toString(id));
        // Storing name in pref
        editor.putString(Constants.KEY_NAME, name);
        // Storing email in pref
        editor.putString(Constants.KEY_EMAIL, email);
        // Storing avatar in pref
        editor.putString(Constants.KEY_AVATAR, avatar);
        // Storing remember token
        editor.putString(Constants.KEY_AUTH_TOKEN, rememberToken);
        // Storing remember me
        editor.putString(Constants.KEY_REMEMBER_ME, Integer.toString(rememberMe));
        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (!this.isLoggedInAndRemember()) {
            logoutUser();
        }
    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user id
        user.put(Constants.KEY_ID, pref.getString(Constants.KEY_ID, null));
        // user name
        user.put(Constants.KEY_NAME, pref.getString(Constants.KEY_NAME, null));
        // user email id
        user.put(Constants.KEY_EMAIL, pref.getString(Constants.KEY_EMAIL, null));
        // user avatar
        user.put(Constants.KEY_AVATAR, pref.getString(Constants.KEY_AVATAR, null));
        // user remember token
        user.put(Constants.KEY_AUTH_TOKEN, pref.getString(Constants.KEY_AUTH_TOKEN, null));
        // user remember me
        user.put(Constants.KEY_REMEMBER_ME, pref.getString(Constants.KEY_REMEMBER_ME, null));
        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Delete session
     */
    public void deleteSessionData() {
        editor.clear();
        editor.commit();
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    /**
     * Quick check for login and remember me
     **/
    // Get Login State
    public boolean isLoggedInAndRemember() {
        return isLoggedIn() && getUserDetails().get(Constants.KEY_REMEMBER_ME).equals(LOGIN_TRUE);
    }
}
