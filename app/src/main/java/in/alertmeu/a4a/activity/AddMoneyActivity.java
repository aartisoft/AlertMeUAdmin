package in.alertmeu.a4a.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.alertmeu.a4a.R;
import in.alertmeu.a4a.adapter.AddMoneyListAdpter;
import in.alertmeu.a4a.adapter.AddNRAMoneyListAdpter;
import in.alertmeu.a4a.adapter.SubCatListAdpter;
import in.alertmeu.a4a.jsonparser.JsonHelper;
import in.alertmeu.a4a.models.AddMoneyModeDAO;
import in.alertmeu.a4a.models.SubCatModeDAO;
import in.alertmeu.a4a.utils.AppStatus;
import in.alertmeu.a4a.utils.Config;
import in.alertmeu.a4a.utils.Constant;
import in.alertmeu.a4a.utils.Listener;
import in.alertmeu.a4a.utils.WebClient;
import in.alertmeu.a4a.view.AddNAREntryView;
import in.alertmeu.a4a.view.SubCatEntryView;
import in.alertmeu.a4a.view.UpdateMainCatEntryView;
import in.alertmeu.a4a.view.UpdateNAREntryView;

public class AddMoneyActivity extends AppCompatActivity {
    CountryCodePicker ccp;
    String country_code = "", mainCatId = "", subCatResponse = "";
    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    private FloatingActionButton fab;
    RecyclerView sCatList;
    List<AddMoneyModeDAO> data;
    AddNRAMoneyListAdpter addNRAMoneyListAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_addbutton);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        sCatList = (RecyclerView) findViewById(R.id.sCatList);
        ccp.setCountryPreference(ccp.getDefaultCountryNameCode());
        fab = (FloatingActionButton) findViewById(R.id.fab);
        data = new ArrayList<>();
        //  Toast.makeText(getApplicationContext(), ccp.getDefaultCountryCodeWithPlus(), Toast.LENGTH_SHORT).show();
        country_code = ccp.getSelectedCountryCodeWithPlus();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                // Toast.makeText(getApplicationContext(), "Updated " + ccp.getSelectedCountryCodeWithPlus(), Toast.LENGTH_SHORT).show();
                country_code = ccp.getSelectedCountryCodeWithPlus();
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    new getAddMoneyList().execute();
                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
            new getAddMoneyList().execute();
        } else {

            Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
        }
        UpdateNAREntryView.bindListener(new Listener() {
            @Override
            public void messageReceived(String messageText) {
                new getAddMoneyList().execute();
            }
        });
        AddNAREntryView.bindListener(new Listener() {
            @Override
            public void messageReceived(String messageText) {
                new getAddMoneyList().execute();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNAREntryView addNAREntryView = new AddNAREntryView();
                addNAREntryView.show(getSupportFragmentManager(), "addNAREntryView");

            }
        });
    }

    //
    private class getAddMoneyList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(AddMoneyActivity.this);
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
            prefEditor.putString("country_code", country_code);
            prefEditor.commit();
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
            subCatResponse = serviceAccess.SendHttpPost(Config.URL_GETALLMONEYADDNAPBYADMIN, jsonLeadObj);
            data.clear();
            Log.i("resp", subCatResponse);
            if (subCatResponse.compareTo("") != 0) {
                if (isJSONValid(subCatResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            JsonHelper jsonHelper = new JsonHelper();
                            data = jsonHelper.parseAddMoneyList(subCatResponse);

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
            addNRAMoneyListAdpter = new AddNRAMoneyListAdpter(AddMoneyActivity.this, data);
            sCatList.setAdapter(addNRAMoneyListAdpter);
            sCatList.setLayoutManager(new LinearLayoutManager(AddMoneyActivity.this));
            addNRAMoneyListAdpter.notifyDataSetChanged();
            if (data.size() == 0) {
                fab.setVisibility(View.VISIBLE);
            } else {
                fab.setVisibility(View.GONE);
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