package in.alertmeu.a4a.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.alertmeu.a4a.R;
import in.alertmeu.a4a.adapter.SubCatListAdpter;
import in.alertmeu.a4a.jsonparser.JsonHelper;
import in.alertmeu.a4a.models.MainCatModeDAO;
import in.alertmeu.a4a.models.SubCatModeDAO;
import in.alertmeu.a4a.utils.AppStatus;
import in.alertmeu.a4a.utils.Config;
import in.alertmeu.a4a.utils.Constant;
import in.alertmeu.a4a.utils.Listener;
import in.alertmeu.a4a.utils.WebClient;
import in.alertmeu.a4a.view.AddMoneyEntryView;
import in.alertmeu.a4a.view.MainCatEntryView;
import in.alertmeu.a4a.view.SubCatEntryView;
import in.alertmeu.a4a.view.UpdateMainCatEntryView;
import in.alertmeu.a4a.view.UpdateSubCatEntryView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddModifyCategoryActivity extends AppCompatActivity {
    CountryCodePicker ccp;
    String country_code = "", mainCatNameResponse = "", mainCatId = "", subCatResponse = "";
    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj, jsonObj1;
    ArrayList<MainCatModeDAO> mainCatModeDAOArrayList;
    Spinner displayMainCategoryName;
    boolean status;
    String message = "", flag = "", updateResponse = "";
    List<SubCatModeDAO> data;
    SubCatListAdpter subCatListAdpter;
    RecyclerView sCatList;
    ImageView addCatName, modifyCatName;
    LinearLayout hideshow;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    private FloatingActionButton fab;
    ToggleButton onoffTongleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_button);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        displayMainCategoryName = (Spinner) findViewById(R.id.displayMainCategoryName);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        sCatList = (RecyclerView) findViewById(R.id.sCatList);
        addCatName = (ImageView) findViewById(R.id.addCatName);
        hideshow = (LinearLayout) findViewById(R.id.hideshow);
        modifyCatName = (ImageView) findViewById(R.id.modifyCatName);
        onoffTongleButton = (ToggleButton) findViewById(R.id.onoffTongleButton);
        ccp.setCountryPreference(ccp.getDefaultCountryNameCode());
        data = new ArrayList<>();
        mainCatModeDAOArrayList = new ArrayList<>();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        //  Toast.makeText(getApplicationContext(), ccp.getDefaultCountryCodeWithPlus(), Toast.LENGTH_SHORT).show();
        country_code = ccp.getSelectedCountryCodeWithPlus();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                // Toast.makeText(getApplicationContext(), "Updated " + ccp.getSelectedCountryCodeWithPlus(), Toast.LENGTH_SHORT).show();
                country_code = ccp.getSelectedCountryCodeWithPlus();
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    new initMainCatNameSpinner().execute();
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            new initMainCatNameSpinner().execute();
        } else {

            Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
        }
        addCatName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    prefEditor.putString("country_code", country_code);
                    prefEditor.commit();
                    MainCatEntryView mainCatEntryView = new MainCatEntryView();
                    mainCatEntryView.show(getSupportFragmentManager(), "mainCatEntryView");

                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mainCatId.equals("0")) {
                    prefEditor.putString("mainCatId", mainCatId);
                    prefEditor.commit();
                    SubCatEntryView subCatEntryView = new SubCatEntryView();
                    subCatEntryView.show(getSupportFragmentManager(), "subCatEntryView");
                } else {
                    Toast.makeText(getApplicationContext(), "Please select main Category", Toast.LENGTH_SHORT).show();
                }
            }
        });
        MainCatEntryView.bindListener(new Listener() {
            @Override
            public void messageReceived(String messageText) {
                new initMainCatNameSpinner().execute();
            }
        });

        UpdateMainCatEntryView.bindListener(new Listener() {
            @Override
            public void messageReceived(String messageText) {
                new initMainCatNameSpinner().execute();
            }
        });
        SubCatEntryView.bindListener(new Listener() {
            @Override
            public void messageReceived(String messageText) {
                new getSubCatList().execute();
            }
        });

        UpdateSubCatEntryView.bindListener(new Listener() {
            @Override
            public void messageReceived(String messageText) {
                new getSubCatList().execute();
            }
        });

        onoffTongleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onoffTongleButton.isChecked()) {
                    flag = "1";
                    if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                        new updateMainCat().execute();
                    } else {

                        Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }
                    //  Toast.makeText(getApplicationContext(),""+onoffTongleButton.isChecked(),Toast.LENGTH_SHORT).show();
                } else {
                    flag = "0";
                    if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                        new updateMainCat().execute();
                    } else {

                        Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }
                    //  Toast.makeText(getApplicationContext(),""+onoffTongleButton.isChecked(),Toast.LENGTH_SHORT).show();

                }
            }
        });
        modifyCatName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mainCatId.equals("0")) {
                    prefEditor.putString("mainCatId", mainCatId);
                    prefEditor.commit();
                    UpdateMainCatEntryView updateMainCatEntryView = new UpdateMainCatEntryView();
                    updateMainCatEntryView.show(getSupportFragmentManager(), "updateMainCatEntryView");
                } else {
                    Toast.makeText(getApplicationContext(), "Please select main Category", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //
    private class initMainCatNameSpinner extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(AddModifyCategoryActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("country_code", country_code);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            mainCatNameResponse = serviceAccess.SendHttpPost(Config.URL_GETALLMAINCATEGORYBYADMIN, jsonLeadObj);
            Log.i("resp", "leadListResponse" + mainCatNameResponse);
            mainCatModeDAOArrayList.clear();
            mainCatModeDAOArrayList.add(new MainCatModeDAO("0", "", "", "", "", "Select Category", ""));

            if (mainCatNameResponse.compareTo("") != 0) {
                if (isJSONValid(mainCatNameResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {
                                JSONObject jsonObject = new JSONObject(mainCatNameResponse);
                                status = jsonObject.getBoolean("status");
                                if (status) {
                                    if (!jsonObject.isNull("dataList")) {
                                        JSONArray LeadSourceJsonObj = jsonObject.getJSONArray("dataList");

                                        for (int i = 0; i < LeadSourceJsonObj.length(); i++) {
                                            JSONObject json_data = LeadSourceJsonObj.getJSONObject(i);
                                            mainCatModeDAOArrayList.add(new MainCatModeDAO(json_data.getString("id"), json_data.getString("country_code"), json_data.getString("currency_sign"), json_data.getString("ads_pricing"), json_data.getString("discount"), json_data.getString("category_name"), json_data.getString("status")));

                                        }
                                    }
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
                            //    Toast.makeText(getApplicationContext(), "Please check your network connection", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //   Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            ArrayAdapter<MainCatModeDAO> adapter = new ArrayAdapter<MainCatModeDAO>(AddModifyCategoryActivity.this, android.R.layout.simple_spinner_dropdown_item, mainCatModeDAOArrayList);
            // MyAdapter adapter = new MyAdapter(StudentsListActivity.this,R.layout.spinner_item,locationlist);
            displayMainCategoryName.setAdapter(adapter);
            displayMainCategoryName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#1c5fab"));
                    MainCatModeDAO LeadSource = (MainCatModeDAO) parent.getSelectedItem();
                    // Toast.makeText(getApplicationContext(), "Source ID: " + LeadSource.getId() + ",  Source Name : " + LeadSource.getItemName(), Toast.LENGTH_SHORT).show();
                    mainCatId = LeadSource.getId();
                    new getSubCatList().execute();

                    prefEditor.putString("currency_sign", LeadSource.getCurrency_sign());
                    prefEditor.putString("ads_pricing", LeadSource.getAds_pricing());
                    prefEditor.putString("discount", LeadSource.getDiscount());
                    prefEditor.putString("category_name", LeadSource.getCategory_name());
                    prefEditor.commit();

                    if (!mainCatId.equals("0")) {
                        hideshow.setVisibility(View.VISIBLE);

                    } else {

                        hideshow.setVisibility(View.GONE);
                        mainCatId = "0";
                    }
                    if (LeadSource.getStatus().equals("1")) {
                        onoffTongleButton.setChecked(true);
                    } else {
                        onoffTongleButton.setChecked(false);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }


            });


        }
    }

    //
    private class getSubCatList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(AddModifyCategoryActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Loading...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("m_id", mainCatId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            subCatResponse = serviceAccess.SendHttpPost(Config.URL_GETALLSUBCATEGORYBYADMIN, jsonLeadObj);
            data.clear();
            Log.i("resp", subCatResponse);
            if (subCatResponse.compareTo("") != 0) {
                if (isJSONValid(subCatResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            JsonHelper jsonHelper = new JsonHelper();
                            data = jsonHelper.parseSubCatList(subCatResponse);

                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            subCatListAdpter = new SubCatListAdpter(AddModifyCategoryActivity.this, data);
            sCatList.setAdapter(subCatListAdpter);
            sCatList.setLayoutManager(new LinearLayoutManager(AddModifyCategoryActivity.this));
            subCatListAdpter.notifyDataSetChanged();

        }
    }

    private class updateMainCat extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(AddModifyCategoryActivity.this);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Updating...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("id", mainCatId);
                        put("status", flag);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();

            updateResponse = serviceAccess.SendHttpPost(Config.URL_ONOFFMAINCATBYADMIN, jsonLeadObj);
            Log.i("resp", updateResponse);
            if (isJSONValid(updateResponse)) {
                try {

                    JSONObject jsonObject = new JSONObject(updateResponse);
                    status = jsonObject.getBoolean("status");
                    message = jsonObject.getString("message");


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }


            return null;

        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                new initMainCatNameSpinner().execute();
            } else {

                Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
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
}
