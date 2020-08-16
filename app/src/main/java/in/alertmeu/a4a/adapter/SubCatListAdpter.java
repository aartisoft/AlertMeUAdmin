package in.alertmeu.a4a.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import in.alertmeu.a4a.R;
import in.alertmeu.a4a.activity.AddModifyCategoryActivity;
import in.alertmeu.a4a.models.AddMoneyRequestHistoryDAO;
import in.alertmeu.a4a.models.SubCatModeDAO;
import in.alertmeu.a4a.utils.AppStatus;
import in.alertmeu.a4a.utils.Config;
import in.alertmeu.a4a.utils.Constant;
import in.alertmeu.a4a.utils.WebClient;
import in.alertmeu.a4a.view.AddMoneyEntryView;
import in.alertmeu.a4a.view.UpdateSubCatEntryView;


public class SubCatListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<SubCatModeDAO> data;
    SubCatModeDAO current;
    int ID;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    boolean status;
    String message = "", flag = "", updateResponse = "", subCatId = "";
    ProgressDialog mProgressDialog;
    private JSONObject jsonLeadObj;

    // create constructor to innitilize context and data sent from MainActivity
    public SubCatListAdpter(Context context, List<SubCatModeDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_sub_cat_details, parent, false);
        MyHolder holder = new MyHolder(view);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = position;
        // Get current position of item in recyclerview to bind data and assign values from list
        final MyHolder myHolder = (MyHolder) holder;

        current = data.get(position);
        myHolder.desc.setText(current.getSubcategory_name());
        myHolder.desc.setTag(position);
        myHolder.modifyCatName.setTag(position);
        myHolder.onoffTongleButton.setTag(position);

        myHolder.modifyCatName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("id", current.getId());
                prefEditor.putString("sub_category_name", current.getSubcategory_name());
                prefEditor.commit();
                UpdateSubCatEntryView updateSubCatEntryView = new UpdateSubCatEntryView();
                updateSubCatEntryView.show(((FragmentActivity) context).getSupportFragmentManager(), "updateSubCatEntryView");


            }
        });
        myHolder.onoffTongleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                subCatId = current.getId();
                if (myHolder.onoffTongleButton.isChecked()) {
                    flag = "1";
                    if (AppStatus.getInstance(context).isOnline()) {
                        new updateMainCat().execute();
                    } else {

                        Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }
                    //  Toast.makeText(getApplicationContext(),""+onoffTongleButton.isChecked(),Toast.LENGTH_SHORT).show();
                } else {
                    flag = "0";
                    if (AppStatus.getInstance(context).isOnline()) {
                        new updateMainCat().execute();
                    } else {

                        Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }
                    //  Toast.makeText(getApplicationContext(),""+onoffTongleButton.isChecked(),Toast.LENGTH_SHORT).show();

                }

            }
        });

    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView desc;
        ToggleButton onoffTongleButton;
        ImageView modifyCatName;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            desc = (TextView) itemView.findViewById(R.id.desc);
            modifyCatName = (ImageView) itemView.findViewById(R.id.modifyCatName);
            onoffTongleButton = (ToggleButton) itemView.findViewById(R.id.onoffTongleButton);

        }

    }

    private class updateMainCat extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(context);
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
                        put("id", subCatId);
                        put("status", flag);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();

            updateResponse = serviceAccess.SendHttpPost(Config.URL_ONOFFSUBCATBYADMIN, jsonLeadObj);
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
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

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

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            //LOGE(TAG, "ParseException - dateFormat");
        }

        return outputDate;

    }

}
