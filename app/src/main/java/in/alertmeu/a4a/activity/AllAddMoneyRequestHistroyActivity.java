package in.alertmeu.a4a.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.alertmeu.a4a.R;
import in.alertmeu.a4a.adapter.AddMoneyListAdpter;
import in.alertmeu.a4a.jsonparser.JsonHelper;
import in.alertmeu.a4a.models.AddMoneyRequestHistoryDAO;
import in.alertmeu.a4a.utils.Config;
import in.alertmeu.a4a.utils.Listener;
import in.alertmeu.a4a.utils.WebClient;
import in.alertmeu.a4a.view.AddMoneyEntryView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AllAddMoneyRequestHistroyActivity extends AppCompatActivity {
    RecyclerView addMoneyList;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    ProgressDialog mProgressDialog;
    JSONObject jsonLeadObj;
    JSONArray jsonArray;
    String advertisementListResponse = "";
    List<AddMoneyRequestHistoryDAO> data;
    AddMoneyListAdpter addMoneyListAdpter;
    TextView availBalTxt;
    Button addMoney, AdHistory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_add_money_request_histroy);
        addMoneyList=(RecyclerView)findViewById(R.id.addMoneyList);
        new getAddMoneyRequest().execute();

        AddMoneyEntryView.bindListener(new Listener() {
            @Override
            public void messageReceived(String messageText) {
                new getAddMoneyRequest().execute();
            }
        });
    }

    private class getAddMoneyRequest extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(AllAddMoneyRequestHistroyActivity.this);
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
                        put("business_user_id", preferences.getString("admin_user_id", ""));

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };
            WebClient serviceAccess = new WebClient();


            Log.i("json", "json" + jsonLeadObj);
            advertisementListResponse = serviceAccess.SendHttpPost(Config.URL_GETALLREQUESTMONEYADD, jsonLeadObj);
            Log.i("resp", "advertisementListResponse" + advertisementListResponse);
            if (advertisementListResponse.compareTo("") != 0) {
                if (isJSONValid(advertisementListResponse)) {

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            try {


                                JsonHelper jsonHelper = new JsonHelper();
                                data = jsonHelper.parsetransactionhistoryList(advertisementListResponse);
                                jsonArray = new JSONArray(advertisementListResponse);

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
                            //Toast.makeText(getApplicationContext(), "Please check your webservice", Toast.LENGTH_LONG).show();
                        }
                    });

                    return null;
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(getApplicationContext(), "Please check your network connection.", Toast.LENGTH_LONG).show();
                    }
                });

                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            mProgressDialog.dismiss();
            if (data.size() > 0) {
                addMoneyListAdpter = new AddMoneyListAdpter(AllAddMoneyRequestHistroyActivity.this, data);
                addMoneyList.setAdapter(addMoneyListAdpter);
                addMoneyList.setLayoutManager(new LinearLayoutManager(AllAddMoneyRequestHistroyActivity.this));
                // Toast.makeText(getApplicationContext(), "" + data.size(), Toast.LENGTH_SHORT).show();

            } else {
                addMoneyListAdpter = new AddMoneyListAdpter(AllAddMoneyRequestHistroyActivity.this, data);
                addMoneyList.setAdapter(addMoneyListAdpter);
                addMoneyList.setLayoutManager(new LinearLayoutManager(AllAddMoneyRequestHistroyActivity.this));

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
