package in.alertmeu.a4a.activity;

import androidx.appcompat.app.AppCompatActivity;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangeMyPasswordActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    EditText old_password, new_password,reenter_password;
    Button btnNext;
    String newpassword = "", oldpassword = "", mobile = "";
    private JSONObject jsonLeadObj, jsonSchedule;
    ProgressDialog mProgressDialog;
    boolean status;
    String msg = "";
    String updateStatusResponse = "", imagePathResponse = "",reenterpassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_my_password);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        reenter_password = (EditText) findViewById(R.id.reenter_password);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldpassword = old_password.getText().toString().trim();
                newpassword = new_password.getText().toString().trim();
                if (validate(oldpassword, newpassword, reenterpassword)) {
                    if (reenterpassword.equals(newpassword)) {
                        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

                            if (newpassword.length() < 6 && !isValidPassword(newpassword)) {

                                Toast.makeText(getApplicationContext(), "Not Valid", Toast.LENGTH_SHORT).show();
                            } else {

                                // Toast.makeText(getApplicationContext(), "Valid", Toast.LENGTH_SHORT).show();
                                new updatePassword().execute();


                            }


                        } else {

                            Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Password mismatch. Please try again!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    public boolean validate(String userOldPass, String userNewPass, String userRENewPass) {
        boolean isValidate = false;
        if (userOldPass.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter your old password.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (userNewPass.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please enter your new password.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else if (userRENewPass.trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Please reenter your  password.", Toast.LENGTH_LONG).show();
            isValidate = false;

        } else {

            isValidate = true;
        }
        return isValidate;
    }


    private class updatePassword extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(ChangeMyPasswordActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Updating Password...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("user_id", preferences.getString("admin_user_id", ""));
                        put("old_password", oldpassword);
                        put("new_password", newpassword);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj);
            updateStatusResponse = serviceAccess.SendHttpPost(Config.URL_UPDATEADMINUSERPASSWORD, jsonLeadObj);
            Log.i("resp", "updateStatusResponse" + updateStatusResponse);

            if (updateStatusResponse.compareTo("") != 0) {
                if (isJSONValid(updateStatusResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                JSONObject jsonObject = new JSONObject(updateStatusResponse);
                                msg = jsonObject.getString("message");
                                status = jsonObject.getBoolean("status");

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            if (status) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ChangeMyPasswordActivity.this, HelpCenterActivity.class);
                startActivity(intent);
                finish();


            } else {

                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


            }


        }
    }

    protected boolean isJSONValid(String callReoprtResponse2) {
        // TODO Auto-generated method stub
        try {
            new JSONObject(callReoprtResponse2);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(callReoprtResponse2);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
    //
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent setIntent = new Intent(ChangeMyPasswordActivity.this, HelpCenterActivity.class);
        startActivity(setIntent);
        finish();
    }
}
