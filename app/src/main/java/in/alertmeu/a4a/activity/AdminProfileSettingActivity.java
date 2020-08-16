package in.alertmeu.a4a.activity;

import androidx.appcompat.app.AppCompatActivity;
import in.alertmeu.a4a.R;
import in.alertmeu.a4a.imageUtils.ImageLoader;
import in.alertmeu.a4a.models.AddMoneyRequestHistoryDAO;
import in.alertmeu.a4a.utils.AppStatus;
import in.alertmeu.a4a.utils.Config;
import in.alertmeu.a4a.utils.Constant;
import in.alertmeu.a4a.utils.WebClient;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdminProfileSettingActivity extends AppCompatActivity {
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    LinearLayout inviteMsg, btnHelp, btnshopPrec, myPlaces, myAccount, myRequestMoney, addModifyCat,addModifyMoney;
    TextView userName, userMobile, usermailid;
    private JSONObject jsonLeadObj;
    String businessUserResponse = "";
    ProgressDialog mProgressDialog;
    String businessName, businessMobile, businessEmail, businessImage;
    ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile_setting);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        btnHelp = (LinearLayout) findViewById(R.id.btnHelp);
        btnshopPrec = (LinearLayout) findViewById(R.id.btnshopPrec);
        userName = (TextView) findViewById(R.id.userName);
        userMobile = (TextView) findViewById(R.id.userMobile);
        usermailid = (TextView) findViewById(R.id.usermailid);
        profilePic = (ImageView) findViewById(R.id.profilePic);
        myPlaces = (LinearLayout) findViewById(R.id.myPlaces);
        inviteMsg = (LinearLayout) findViewById(R.id.inviteMsg);
        myAccount = (LinearLayout) findViewById(R.id.myAccount);
        addModifyCat = (LinearLayout) findViewById(R.id.addModifyCat);
        myRequestMoney = (LinearLayout) findViewById(R.id.myRequestMoney);
        addModifyMoney= (LinearLayout) findViewById(R.id.addModifyMoney);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfileSettingActivity.this, HelpCenterActivity.class);
                startActivity(intent);
            }
        });
       /* btnshopPrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfileSettingActivity.this, BusinessExpandableListViewActivity.class);
                startActivity(intent);
            }
        });*/
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {

            // new getData().execute();

        } else {

            Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
        }
        myPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    Intent intent = new Intent(AdminProfileSettingActivity.this, MyPlacesActivity.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
        myRequestMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    Intent intent = new Intent(AdminProfileSettingActivity.this, AllAddMoneyRequestHistroyActivity.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
        addModifyMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    Intent intent = new Intent(AdminProfileSettingActivity.this, AddMoneyActivity.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
          /* profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    Intent intent = new Intent(AdminProfileSettingActivity.this, EditAccountSetupActivity.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminProfileSettingActivity.this, MyAccountActivity.class);
                startActivity(intent);
            }
        });*/
        inviteMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdminProfileSettingActivity.this);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.custom_inviteapp_alertdialog, null);
                alertDialogBuilder.setView(view);
                alertDialogBuilder.setCancelable(true);
                final AlertDialog dialog = alertDialogBuilder.create();
                dialog.show();
                Button UserApps = (Button) dialog.findViewById(R.id.btnUserApp);
                UserApps.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_u_alert);
                        String path = getExternalCacheDir() + "/shareimage.jpg";
                        java.io.OutputStream out = null;
                        java.io.File file = new java.io.File(path);
                        try {
                            out = new java.io.FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        path = file.getPath();
                        Uri bmpUri = Uri.parse("file://" + path);

                        Intent shareIntent = new Intent();
                        shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://bit.ly/2Z66LXa");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        shareIntent.setType("image/jpg");
                        startActivity(Intent.createChooser(shareIntent, "Share User App with"));
                    }
                });

                Button btnBusinessApp = (Button) dialog.findViewById(R.id.btnBusinessApp);
                btnBusinessApp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo_h_alert);
                        String path = getExternalCacheDir() + "/shareimage.jpg";
                        java.io.OutputStream out = null;
                        java.io.File file = new java.io.File(path);
                        try {
                            out = new java.io.FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        path = file.getPath();
                        Uri bmpUri = Uri.parse("file://" + path);

                        Intent shareIntent = new Intent();
                        shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://bit.ly/2Z66LXa");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                        shareIntent.setType("image/jpg");
                        startActivity(Intent.createChooser(shareIntent, "Share Business App with"));

                    }
                });
                dialog.show();

            }
        });
        addModifyCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    Intent intent = new Intent(AdminProfileSettingActivity.this, AddModifyCategoryActivity.class);
                    startActivity(intent);
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

  /*  private class getData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            // mProgressDialog = new ProgressDialog(BusinessProfileSettingActivity.this);
            // Set progressdialog title
            //  mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            //   mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            //   mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("admin_user_id", preferences.getString("admin_user_id", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            businessUserResponse = serviceAccess.SendHttpPost(Config.URL_GETBUSINESSUSERDEATILS, jsonLeadObj);
            Log.i("resp", "businessUserResponse" + businessUserResponse);


            if (businessUserResponse.compareTo("") != 0) {
                if (isJSONValid(businessUserResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {

                                JSONArray leadJsonObj = new JSONArray(businessUserResponse);
                                for (int i = 0; i < leadJsonObj.length(); i++) {
                                    JSONObject object = leadJsonObj.getJSONObject(i);
                                    businessName = object.getString("business_name");
                                    businessMobile = object.getString("mobile_no");
                                    businessEmail = object.getString("business_email");
                                    profilePath = object.getString("profilePath");
                                }
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
                            Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
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
            userName.setText(businessName);
            userMobile.setText(businessMobile);
            usermailid.setText(businessEmail);
            ImageLoader imageLoader = new ImageLoader(AdminProfileSettingActivity.this);
            imageLoader.DisplayImage(businessImage, profilePic);


            // Close the progressdialog
            //  mProgressDialog.dismiss();
        }
    }*/

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
        finish();
        Intent setIntent = new Intent(AdminProfileSettingActivity.this, HomePageActivity.class);
        startActivity(setIntent);

    }
}

