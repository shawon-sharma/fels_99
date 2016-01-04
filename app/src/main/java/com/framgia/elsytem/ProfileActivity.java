package com.framgia.elsytem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.elsytem.mypackage.Constants;
import com.framgia.elsytem.mypackage.SessionManager;
import com.framgia.elsytem.mypackage.Url;
import com.framgia.elsytem.mypackage.UserFunctions;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    ListView listViewProfile;
    String[] formattedDate = new String[1];
    int images[] = {R.drawable.e, R.drawable.b, R.drawable.ic_a};
    ImageView editProfile, avatar;
    TextView name, email;
    SessionManager session;
    Constants constant;
    Button lesson, words;
    HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUpToolbar();
        initializeViews();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate[0] = df.format(c.getTime());
        mInitializeListeners();
        mGetSessionData();
        name.setText(user.get(Constants.KEY_NAME));
        email.setText(user.get(Constants.KEY_EMAIL));
        mLoadAvatar(user.get(Constants.KEY_AVATAR));
    }

    private void mLoadAvatar(String avatarString) {
        if (!avatarString.isEmpty()) {
            if (isUrl(avatarString)) {
                Picasso.with(this)
                        .load(avatarString)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .resize(100, 100)
                        .centerCrop()
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
                        .resize(100, 100)
                        .centerCrop()
                        .placeholder(R.drawable.ic_person_outline_black_36dp)
                        .error(R.drawable.ico_fail)
                        .into(avatar);
            }
        }
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
        session = new SessionManager(getApplicationContext());
        user = session.getUserDetails();
    }

    private void mInitializeListeners() {
        ViewAdapter v = new ViewAdapter(this, formattedDate);
        lesson = (Button) findViewById(R.id.lesson_btn);
        lesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), CategoriesActivity.class));
            }
        });
        words = (Button) findViewById(R.id.words_btn);
        words.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), WordlistActivity.class));
            }
        });
        listViewProfile.setAdapter(v);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UpdateProfileActivity.class));
                finish();
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

    /**
     * Decodes the base64 string into byte array
     *
     * @param imageDataString - a {@link java.lang.String}
     * @return byte array
     */
    public static byte[] decodeImage(String imageDataString) {
        return Base64.decode(imageDataString, Base64.DEFAULT);
    }

    private void initializeViews() {
        listViewProfile = (ListView) findViewById(R.id.listview);
        editProfile = (ImageView) findViewById(R.id.edit_profile);
        name = (TextView) findViewById(R.id.person);
        email = (TextView) findViewById(R.id.email_textview);
        avatar = (ImageView) findViewById(R.id.avatar);
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
                session.logoutUser();
                finish();
            }
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }
    }
}

class ViewAdapter extends ArrayAdapter<String> {
    Context c;
    String[] formattedDate;

    ViewAdapter(Context c, String[] formattedDate) {
        super(c, R.layout.profile_list, R.id.textView2, formattedDate);
        this.c = c;
        this.formattedDate = formattedDate;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflate = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflate.inflate(R.layout.profile_list, parent, false);
        ImageView i = (ImageView) row.findViewById(R.id.imageView);
        TextView t1 = (TextView) row.findViewById(R.id.textView);
        TextView t2 = (TextView) row.findViewById(R.id.textView2);
        t1.setText(formattedDate[position]);
        return row;
    }
}
