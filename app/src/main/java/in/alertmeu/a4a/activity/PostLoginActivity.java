package in.alertmeu.a4a.activity;

import androidx.appcompat.app.AppCompatActivity;
import in.alertmeu.a4a.FirebaseNotification.SharedPrefManager;
import in.alertmeu.a4a.R;
import in.alertmeu.a4a.utils.AppStatus;
import in.alertmeu.a4a.utils.Config;
import in.alertmeu.a4a.utils.Constant;
import in.alertmeu.a4a.utils.WebClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostLoginActivity extends AppCompatActivity {
    TextView username, notusername, forGotPass;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    EditText u_pass;
    Button login;
    String userPassword = "", loginResponse = "", msg = "", userId = "", userMobile = "";
    boolean status;
    ProgressDialog mProgressDialog;
    JSONObject jsonObj, jsonObject;
    String deviceId = "";
    TelephonyManager telephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        username = (TextView) findViewById(R.id.username);
        notusername = (TextView) findViewById(R.id.notusername);
        u_pass = (EditText) findViewById(R.id.u_pass);
        forGotPass = (TextView) findViewById(R.id.forGotPass);
        login = (Button) findViewById(R.id.login);

        System.out.println("value is =" + preferences.getString("admin_user_name", ""));
        if (!preferences.getString("admin_user_name", "").equals("")) {
            username.setText("Welcome back, " + preferences.getString("admin_user_name", "") + "!");
            notusername.setVisibility(View.VISIBLE);
        } else {
            username.setText("Welcome");
            notusername.setVisibility(View.GONE);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {


                    userPassword = u_pass.getText().toString().trim();
                    if (validate(userPassword)) {
                       /* if (preferences.getString("flag", "").equals("email")) {
                            userId = preferences.getString("userEmail", "");
                            new userEmailLogin().execute();
                        }*/
                        if (preferences.getString("flag", "").equals("mobile")) {
                            userMobile = preferences.getString("AdminUserMobile", "");
                            new userMobileLogin().execute();
                        }
                    }
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }

            }
        });
        notusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostLoginActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        forGotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

                    userMobile = preferences.getString("AdminUserMobile", "");
                    Intent intent = new Intent(PostLoginActivity.this, OTPForChangePasswordActivity.class);
                    intent.putExtra("mobile", userMobile);
                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });

        deviceId = getDeviceId();

    }

    public boolean validate(String userPassword) {
        boolean isValidate = false;
        if (userPassword.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter Password.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {
            isValidate = true;
        }
        return isValidate;
    }

    private class userEmailLogin extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(PostLoginActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Login...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            final String token = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();
            jsonObj = new JSONObject() {
                {
                    try {


                        put("email_users", userId);
                        put("password_users", userPassword);
                        put("fcm_id", token);
                        put("device_id", deviceId);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonObj);
            loginResponse = serviceAccess.SendHttpPost(Config.URL_CHECKUSERLOGIN, jsonObj);
            Log.i("resp", "loginResponse" + loginResponse);


            if (loginResponse.compareTo("") != 0) {
                if (isJSONValid(loginResponse)) {


                    try {

                        jsonObject = new JSONObject(loginResponse);
                        status = jsonObject.getBoolean("status");
                        msg = jsonObject.getString("message");
                        if (status) {
                            if (!jsonObject.isNull("user_id")) {
                                JSONArray ujsonArray = jsonObject.getJSONArray("user_id");
                                for (int i = 0; i < ujsonArray.length(); i++) {
                                    JSONObject UJsonObject = ujsonArray.getJSONObject(i);
                                    prefEditor.putString("user_id", UJsonObject.getString("id"));
                                    prefEditor.putString("user_name", UJsonObject.getString("first_name") + " " + UJsonObject.getString("last_name"));
                                    prefEditor.putString("user_mobile", UJsonObject.getString("mobile_no"));
                                    prefEditor.putString("user_mail", UJsonObject.getString("email_id"));
                                    prefEditor.putString("favloc", "0");
                                    prefEditor.commit();
                                }
                            }

                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {
                    // Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                }
            } else {

                // Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (status) {
                // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(PostLoginActivity.this, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                LoginActivity.fa.finish();
                finish();

                // Close the progressdialog
                mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                mProgressDialog.dismiss();

            }
        }
    }

    private class userMobileLogin extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(PostLoginActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Login...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            final String token = SharedPrefManager.getInstance(getApplicationContext()).getDeviceToken();
            jsonObj = new JSONObject() {
                {
                    try {


                        put("userMobile", userMobile);
                        put("password_users", userPassword);
                        put("fcm_id", token);
                        put("device_id", deviceId);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonObj);
            loginResponse = serviceAccess.SendHttpPost(Config.URL_MOBILEUSERLOGIN, jsonObj);
            Log.i("resp", "loginResponse" + loginResponse);


            if (loginResponse.compareTo("") != 0) {
                if (isJSONValid(loginResponse)) {


                    try {

                        jsonObject = new JSONObject(loginResponse);
                        status = jsonObject.getBoolean("status");
                        msg = jsonObject.getString("message");
                        if (status) {
                            if (!jsonObject.isNull("user_id")) {
                                JSONArray ujsonArray = jsonObject.getJSONArray("user_id");
                                for (int i = 0; i < ujsonArray.length(); i++) {
                                    JSONObject UJsonObject = ujsonArray.getJSONObject(i);
                                    prefEditor.putString("admin_user_id", UJsonObject.getString("id"));
                                    prefEditor.putString("admin_user_name", UJsonObject.getString("first_name") + " " + UJsonObject.getString("last_name"));
                                    prefEditor.putString("admin_user_mobile", UJsonObject.getString("mobile_no"));
                                    prefEditor.putString("admin_user_mail", UJsonObject.getString("email_id"));
                                    prefEditor.putString("favloc", "0");
                                    prefEditor.commit();
                                }
                            }

                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {
                    // Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                }
            } else {

                // Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (status) {
                // Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


                Intent intent = new Intent(PostLoginActivity.this, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                LoginActivity.fa.finish();
                finish();

                // Close the progressdialog
                mProgressDialog.dismiss();
            } else {
                // Close the progressdialog
                Toast.makeText(getApplicationContext(), "Please enter valid password!", Toast.LENGTH_LONG).show();
                mProgressDialog.dismiss();

            }
        }
    }

    protected boolean isJSONValid(String registrationResponse) {
        // TODO Auto-generated method stub
        try {
            new JSONObject(registrationResponse);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(registrationResponse);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public String getDeviceId() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = telephonyManager.getDeviceId();
        return deviceId;
    }

}
