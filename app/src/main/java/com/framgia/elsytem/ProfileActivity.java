package com.framgia.elsytem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.elsytem.adapters.ProfileActivityListAdapter;
import com.framgia.elsytem.jsonResponse.ShowUserResponse;
import com.framgia.elsytem.utils.Constants;
import com.framgia.elsytem.utils.RoundedCornersTransformation;
import com.framgia.elsytem.utils.SessionManager;
import com.framgia.elsytem.utils.Url;
import com.framgia.elsytem.utils.UserFunctions;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = Constants.PROFILE_ACTIVITY;
    private ListView mListViewProfile;
    private ImageView mEditProfile, avatar;
    private TextView mName, mEmail, mLearnedWords;
    private SessionManager mSession;
    private Button mLesson, mWords;
    private HashMap<String, String> mUser;
    private ProgressBar mProgressBar;

    /**
     * Decodes the base64 string into byte array
     *
     * @param imageDataString - a {@link java.lang.String}
     * @return byte array
     */
    public static byte[] decodeImage(String imageDataString) {
        return Base64.decode(imageDataString, Base64.DEFAULT);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUpToolbar();
        initializeViews();
        mInitializeListeners();
    }

    public boolean isConnected() {
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(Activity
                .CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void mShowDialog(Context context, String title, String message,
                             Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle(title);
        // Setting Dialog Message
        alertDialog.setMessage(message);
        if (status != null)
            // Setting alert dialog icon
            alertDialog.setIcon((status) ? R.drawable.ico_success : R.drawable.ico_fail);
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.action_cancel), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.action_turn_on_wifi), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string
                .action_turn_on_cellular_data), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isConnected()) {
            mShowDialog(ProfileActivity.this, getString(R.string
                            .connection_error_title_activity_login),
                    getString(R.string.connection_error_message_activity_login),
                    false);
        } else {
            mGetSessionData();
            mLoadAvatar(mUser.get(Constants.KEY_AVATAR));
            mGetUserDetailsFromJson();
        }
    }

    private void mGetUserDetailsFromJson() {
        new HttpAsyncTaskShowUser().execute(Url.url_show_user + mUser.get(Constants.ID) +
                Constants.DOT_JSON);
    }

    private void mLoadAvatar(String avatarString) {
        if (!avatarString.isEmpty()) {
            if (isUrl(avatarString)) {
                Picasso.with(this)
                        .load(avatarString)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .resize(Constants.AVATAR_WIDTH_HEIGHT_AND_RADIUS, Constants.AVATAR_WIDTH_HEIGHT_AND_RADIUS)
                        .centerCrop()
                        .transform(new RoundedCornersTransformation(Constants
                                .AVATAR_WIDTH_HEIGHT_AND_RADIUS, Constants.ROUNDED_AVATAR_MARGIN))
                        .placeholder(R.drawable.ic_person_outline_black_36dp)
                        .error(R.drawable.ico_fail)
                        .into(avatar);
            } else {
                // Write image byte array from base64 string into file system
                mCreateFileFromBase64String(avatarString);
                // get the uri of the created image
                Uri uri = mGetUri(getFilesDir() + Constants.KEY_AVATAR_FILE_NAME);
                Picasso.with(this)
                        .load(uri)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .resize(Constants.AVATAR_WIDTH_HEIGHT_AND_RADIUS, Constants.AVATAR_WIDTH_HEIGHT_AND_RADIUS)
                        .centerCrop()
                        .transform(new RoundedCornersTransformation(Constants
                                .AVATAR_WIDTH_HEIGHT_AND_RADIUS, Constants.ROUNDED_AVATAR_MARGIN))
                        .placeholder(R.drawable.ic_person_outline_black_36dp)
                        .error(R.drawable.ico_fail)
                        .into(avatar);
            }
        } else Picasso.with(this)
                .load(R.drawable.ic_person_outline_black_36dp)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .resize(Constants.AVATAR_WIDTH_HEIGHT_AND_RADIUS, Constants.AVATAR_WIDTH_HEIGHT_AND_RADIUS)
                .centerCrop()
                .into(avatar);
    }

    private Uri mGetUri(String filePath) {
        return Uri.fromFile(new File(filePath));
    }

    private void mCreateFileFromBase64String(String avatar) {
        FileOutputStream imageOutputFile = null;
        try {
            imageOutputFile = new FileOutputStream(getFilesDir()
                    + Constants.KEY_AVATAR_FILE_NAME, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            imageOutputFile.write(decodeImage(avatar));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            imageOutputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mGetSessionData() {
        mSession = new SessionManager(getApplicationContext());
        mUser = mSession.getUserDetails();
    }

    private void mInitializeListeners() {
        mLesson = (Button) findViewById(R.id.lesson_btn);
        mLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), CategoriesActivity.class));
            }
        });
        mWords = (Button) findViewById(R.id.words_btn);
        mWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(getApplication(), WordlistActivity.class));
                startActivity(new Intent(getApplication(), LearnedActivity.class));
            }
        });
        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UpdateProfileActivity.class));
            }
        });
    }

    /**
     * Checks if avatar is a url
     */
    public boolean isUrl(String avatar) {
        URL url = null;
        try {
            url = new URL(avatar);
        } catch (MalformedURLException e) {
            Log.v(TAG, e.toString());
        }
        return url != null;
    }

    private void initializeViews() {
        mListViewProfile = (ListView) findViewById(R.id.list_view);
        mEditProfile = (ImageView) findViewById(R.id.edit_profile);
        mName = (TextView) findViewById(R.id.person);
        mEmail = (TextView) findViewById(R.id.email_textview);
        mLearnedWords = (TextView) findViewById(R.id.desc_textview);
        avatar = (ImageView) findViewById(R.id.avatar);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                logOut();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        logOut();
    }

    public void logOut() {
        // dialog box
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(getString(R.string.activity_main_alert_dialog_title))
                .setMessage(getString(R.string.activity_main_alert_dialog_message))
                .setPositiveButton(getString(R.string.activity_main_alert_dialog_yes), new DialogInterface
                        .OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new HttpAsyncTaskSignOut().execute(Url.url_sign_out);
                    }
                })
                .setNegativeButton(getString(R.string.activity_main_alert_dialog_no), null)
                .show();
    }

    private class HttpAsyncTaskShowUser extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... urls) {
            return new UserFunctions().showUser(urls[0], mUser.get(Constants.KEY_AUTH_TOKEN));
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressBar.setVisibility(View.GONE);
            List<ShowUserResponse.UserEntity.ActivitiesEntity> activityList;
            ShowUserResponse response;
            response = new Gson().fromJson(result, ShowUserResponse.class);
            int id = 0;
            try {
                id = response.getUser().getId();
            } catch (Exception e) {
            }
            if (id == Integer.parseInt(mUser.get(Constants.KEY_ID))) {
                mName.setText(response.getUser().getName());
                mEmail.setText(response.getUser().getEmail());
                mLearnedWords.setText(getString(R.string.text_learned) + " " + response.getUser()
                        .getLearned_words() + " " + getString(R.string.text_words));
                activityList = response.getUser().getActivities();
                ProfileActivityListAdapter adapter = new ProfileActivityListAdapter
                        (getApplicationContext(), activityList);
                mListViewProfile.setAdapter(adapter);
            } else Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    private class HttpAsyncTaskSignOut extends AsyncTask<String, Void, String> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(ProfileActivity.this);
            mDialog.setTitle(R.string.contacting_servers);
            mDialog.setMessage(getString(R.string.signing_out));
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(true);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            UserFunctions userFunction = new UserFunctions();
            return userFunction.signOut(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            mDialog.dismiss();
            if (result.equals(getString(R.string.logout_response))) {
                mSession.logoutUser();
                finish();
            }
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }
}
