package in.alertmeu.a4a.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import in.alertmeu.a4a.R;
import in.alertmeu.a4a.models.AddMoneyRequestHistoryDAO;
import in.alertmeu.a4a.view.AddMoneyEntryView;


public class AddMoneyListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<AddMoneyRequestHistoryDAO> data;
    AddMoneyRequestHistoryDAO current;
    int ID;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String user_id = "";

    // create constructor to innitilize context and data sent from MainActivity
    public AddMoneyListAdpter(Context context, List<AddMoneyRequestHistoryDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_addmoney_history_details, parent, false);
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
        myHolder.desc.setText(current.getBusiness_name());
        myHolder.desc.setTag(position);

        myHolder.location.setText(current.getLocation_name());
        myHolder.location.setTag(position);

        myHolder.tv_mno.setText(current.getMobile_no());
        myHolder.tv_mno.setTag(position);

        myHolder.datetime.setText("At: " + formateDateFromstring("yyyy-MM-dd hh:mm:ss", "dd-MMM-yyyy hh:mm:ss a", current.getDate_time()));
        myHolder.datetime.setTag(position);

        myHolder.PreviousAmt.setText(current.getAmount());
        myHolder.PreviousAmt.setTag(position);
        myHolder.CurrentAmt.setText(current.getBalance_amount());
        myHolder.CurrentAmt.setTag(position);
        myHolder.clickAddMony.setTag(position);
        myHolder.clickAddMony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("id", current.getId());
                prefEditor.putString("name", current.getBusiness_name()+","+current.getAmount());
                prefEditor.putString("business_user_id", current.getBusiness_user_id());
                prefEditor.commit();
                AddMoneyEntryView addMoneyEntryView = new AddMoneyEntryView();
                addMoneyEntryView.show(((FragmentActivity) context).getSupportFragmentManager(), "addMoneyEntryView");

            }
        });

    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView desc, datetime, location, PreviousAmt, CurrentAmt, tv_mno;
        LinearLayout clickAddMony;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            desc = (TextView) itemView.findViewById(R.id.desc);
            datetime = (TextView) itemView.findViewById(R.id.datetime);
            location = (TextView) itemView.findViewById(R.id.location);
            PreviousAmt = (TextView) itemView.findViewById(R.id.PreviousAmt);
            CurrentAmt = (TextView) itemView.findViewById(R.id.CurrentAmt);
            tv_mno = (TextView) itemView.findViewById(R.id.tv_mno);
            clickAddMony = (LinearLayout) itemView.findViewById(R.id.clickAddMony);
        }

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
