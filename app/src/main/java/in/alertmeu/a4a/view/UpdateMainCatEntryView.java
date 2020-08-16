package in.alertmeu.a4a.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import in.alertmeu.a4a.R;
import in.alertmeu.a4a.utils.AppStatus;
import in.alertmeu.a4a.utils.Config;
import in.alertmeu.a4a.utils.Constant;
import in.alertmeu.a4a.utils.Listener;
import in.alertmeu.a4a.utils.WebClient;


public class UpdateMainCatEntryView extends DialogFragment {
    Context context;
    SharedPreferences preferences;
    Editor prefEditor;
    String discontinueResponse = "", message = "";
    JSONObject jsonObj;
    Boolean status;
    int count = 0;
    View registerView;
    private JSONObject jsonLeadObj;
    ProgressDialog mProgressDialog;
    JSONArray jsonArray;
    EditText currencyEdtTxt, mainCatEdtTxt, priceEdtTxt, discountEdtTxt;
    ImageView back_arrow1;
    Button placeBtn;
    String location = "";
    Spinner spinnerLocation;

    JSONObject jsonLeadObj1;
    int quantity = 0;
    TextView itemName;
    private RecyclerView mList;
    String currency_sign = "", ads_pricing = "", discount = "", category_name = "";
    private static Listener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        registerView = inflater.inflate(R.layout.dialog_update_main_cat, null);

        context = getActivity();
        Window window = getDialog().getWindow();

        // set "origin" to top left corner, so to speak
        window.setGravity(Gravity.CENTER | Gravity.CENTER);

        // after that, setting values for x and y works "naturally"
        WindowManager.LayoutParams params = window.getAttributes();

        params.y = 50;
        window.setAttributes(params);
        preferences = getActivity().getSharedPreferences("Prefrence", getActivity().MODE_PRIVATE);
        prefEditor = preferences.edit();

        currencyEdtTxt = (EditText) registerView.findViewById(R.id.currencyEdtTxt);
        mainCatEdtTxt = (EditText) registerView.findViewById(R.id.mainCatEdtTxt);
        priceEdtTxt = (EditText) registerView.findViewById(R.id.priceEdtTxt);
        discountEdtTxt = (EditText) registerView.findViewById(R.id.discountEdtTxt);
        itemName = (TextView) registerView.findViewById(R.id.itemName);

        itemName.setText(preferences.getString("country_code", ""));

        currencyEdtTxt.setText(preferences.getString("currency_sign", ""));
        mainCatEdtTxt.setText(preferences.getString("category_name", ""));
        priceEdtTxt.setText(preferences.getString("ads_pricing", ""));
        discountEdtTxt.setText(preferences.getString("discount", ""));

        placeBtn = (Button) registerView.findViewById(R.id.submit);
        back_arrow1 = (ImageView) registerView.findViewById(R.id.back_arrow1);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        back_arrow1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        placeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                currency_sign = currencyEdtTxt.getText().toString().trim();
                ads_pricing = priceEdtTxt.getText().toString().trim();
                discount = discountEdtTxt.getText().toString().trim();
                category_name = mainCatEdtTxt.getText().toString().trim();
                if (validate(currency_sign, category_name, ads_pricing, discount)) {

                    if (AppStatus.getInstance(context).isOnline()) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(placeBtn.getWindowToken(), 0);
                        new upDateLocation().execute();

                    } else {

                        Toast.makeText(context, Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                    }


                } else {

                }

            }
        });

        getDialog().setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //Hide your keyboard here!!!
                    //Toast.makeText(getActivity(), "PLease enter your information to get us connected with you.", Toast.LENGTH_LONG).show();
                    return true; // pretend we've processed it
                } else
                    return false; // pass on to be processed as normal
            }
        });
        return registerView;
    }


    public boolean validate(String currency_sign, String category_name, String ads_pricing, String discount) {
        boolean isValidate = false;
        if (currency_sign.equals("")) {
            Toast.makeText(getActivity(), "Please Currency Sign", Toast.LENGTH_LONG).show();
        } else if (category_name.equals("")) {
            Toast.makeText(getActivity(), "Please Enter New Main Category Name", Toast.LENGTH_LONG).show();
        } else if (ads_pricing.equals("")) {
            Toast.makeText(getActivity(), "Please Enter Price Per Hour", Toast.LENGTH_LONG).show();
        } else if (discount.equals("")) {
            Toast.makeText(getActivity(), "Please Enter Discount", Toast.LENGTH_LONG).show();
        } else {
            isValidate = true;
        }
        return isValidate;
    }


    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {

                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    //This is the filter
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        //Toast.makeText(getActivity(), "Your information is valuable for us and won't be misused.", Toast.LENGTH_SHORT).show();
                        count++;
                        if (count >= 1) {

                            dismiss();
                        }
                        return true;
                    } else {
                        //Hide your keyboard here!!!!!!
                        return true; // pretend we've processed it
                    }
                } else
                    return false; // pass on to be processed as normal
            }
        });
    }

    //
    private class upDateLocation extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Create a progressdialog
            mProgressDialog = new ProgressDialog(context);
            // Set progressdialog title
            mProgressDialog.setTitle("Please Wait...");
            // Set progressdialog message
            mProgressDialog.setMessage("Insert Main Cat...");
            //mProgressDialog.setIndeterminate(false);
            // Show progressdialog
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            jsonLeadObj = new JSONObject() {
                {
                    try {
                        put("id", preferences.getString("mainCatId", ""));
                        put("currency_sign", currency_sign);
                        put("ads_pricing", ads_pricing);
                        put("discount", discount);
                        put("category_name", category_name);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("json exception", "json exception" + e);
                    }
                }
            };

            WebClient serviceAccess = new WebClient();
            Log.i("json", "json" + jsonLeadObj);
            discontinueResponse = serviceAccess.SendHttpPost(Config.URL_UPDATENEWMAINCATBYADMIN, jsonLeadObj);
            Log.i("resp", "discontinueResponse" + discontinueResponse);
            if (discontinueResponse.compareTo("") != 0) {
                status = true;
                if (isJSONValid(discontinueResponse)) {

                    try {

                        JSONObject jObject = new JSONObject(discontinueResponse);
                        status = jObject.getBoolean("status");
                        message = jObject.getString("message");
                        jsonArray = new JSONArray(discontinueResponse);

                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                } else {

                    // Toast.makeText(context, "Please check your network connection", Toast.LENGTH_LONG).show();

                }
            } else {
                status = false;
                // Toast.makeText(context, "Please check your network connection.", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Close the progressdialog
            mProgressDialog.dismiss();
            if (status) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                dismiss();
                mListener.messageReceived(message);
            } else {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                dismiss();

            }

        }
    }

    public static void bindListener(Listener listener) {
        mListener = listener;
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