package com.framgia.elsytem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.elsytem.jsonResponse.UserResponse;
import com.framgia.elsytem.model.User;
import com.framgia.elsytem.utils.Constants;
import com.framgia.elsytem.utils.SessionManager;
import com.framgia.elsytem.utils.Url;
import com.framgia.elsytem.utils.UserFunctions;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = Constants.LOGIN_ACTIVITY;
    private EditText mEtEmail, mEtPassword;
    private TextView mValidation;
    private Button mButtonLogin;
    private CheckBox mCheckBoxRememberMe;
    private String mEmail, mPassword;
    private int mRememberMe;
    private SessionManager mSession;
    private User mUser;
    private TextView mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        mSession = new SessionManager(getApplicationContext());
        if (mSession.isLoggedInAndRemember()) {
            mStartProfileActivity();
        } else {
            initializeViews();
            mSession.deleteSessionData();
            mRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                }
            });
            mEtPassword.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_DPAD_CENTER:
                            case KeyEvent.KEYCODE_ENTER:
                                mSignIn();
                                return true;
                            default:
                                break;
                        }
                    }
                    return false;
                }
            });
            mButtonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSignIn();
                }
            });
        }
    }

    private void mStartProfileActivity() {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        finish();
    }

    private void mSignIn() {
        mEmail = mEtEmail.getText().toString();
        mPassword = mEtPassword.getText().toString();
        if (mCheckBoxRememberMe.isChecked()) mRememberMe = 1;
        else mRememberMe = Constants.ZERO;
        if (!isConnected()) {
            mShowDialog(LoginActivity.this, getString(R.string
                            .connection_error_title_activity_login),
                    getString(R.string.connection_error_message_activity_login),
                    false);
        } else if (!mEmail.isEmpty() && !mPassword.isEmpty()) {
            new HttpAsyncTaskSignIn().execute(Url.url_sign_in);
        } else if (mEmail.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string
                    .empty_email_activity_login), Toast
                    .LENGTH_SHORT).show();
        } else if (mPassword.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string
                    .empty_password_activity_login), Toast
                    .LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string
                    .empty_email_password_activity_login), Toast.LENGTH_SHORT).show();
        }
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

    private void initializeViews() {
        mRegister = (TextView) findViewById(R.id.text_create_account);
        mEtEmail = (EditText) findViewById(R.id.edit_text_email);
        mEtPassword = (EditText) findViewById(R.id.edit_text_password);
        mValidation = (TextView) findViewById(R.id.text_validation);
        mButtonLogin = (Button) findViewById(R.id.button_login);
        mCheckBoxRememberMe = (CheckBox) findViewById(R.id.check_remember);
    }

    public boolean isConnected() {
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(Activity
                .CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private class HttpAsyncTaskSignIn extends AsyncTask<String, Void, String> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(LoginActivity.this);
            mDialog.setTitle(getString(R.string.contacting_servers));
            mDialog.setMessage(getString(R.string.signing_in));
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(true);
            mDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            mUser = new User();
            mUser.setEmail(mEmail);
            mUser.setPassword(mPassword);
            UserFunctions userFunction = new UserFunctions();
            return userFunction.signIn(urls[Constants.ZERO], mUser, mRememberMe);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            mDialog.dismiss();
            String msg = "";
            try {
                msg = new JSONObject(result).getString(Constants.MESSAGE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                // showing the validation message got from json response
                if (msg.equals(getString(R.string.invalid_username_password_message))) {
                    mValidation.setVisibility(View.VISIBLE);
                } else {
                    UserResponse userResponse;
                    userResponse = new Gson().fromJson(result, UserResponse.class);
                    String name = userResponse.getUser().getName();
                    String email = userResponse.getUser().getEmail();
                    //creating session
                    if (!name.isEmpty() && !email.isEmpty())
                        mSession.createLoginSession(userResponse.getUser().getId(), name, email,
                                userResponse.getUser().getAvatar(), userResponse.getUser()
                                        .getAuth_token(), mRememberMe);
                    //now finish this activity and go to ProfileActivity
                    mStartProfileActivity();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result.isEmpty())
                Toast.makeText(getBaseContext(), getString(R.string.toast_message_try_again), Toast.LENGTH_LONG)
                        .show();
        }
    }
}