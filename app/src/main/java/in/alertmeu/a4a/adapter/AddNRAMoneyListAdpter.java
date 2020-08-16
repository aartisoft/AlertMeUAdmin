package in.alertmeu.a4a.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.alertmeu.a4a.R;
import in.alertmeu.a4a.models.AddMoneyModeDAO;
import in.alertmeu.a4a.models.AddMoneyRequestHistoryDAO;
import in.alertmeu.a4a.view.AddMoneyEntryView;
import in.alertmeu.a4a.view.UpdateNAREntryView;


public class AddNRAMoneyListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<AddMoneyModeDAO> data;
    AddMoneyModeDAO current;
    int ID;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;
    String user_id = "";

    // create constructor to innitilize context and data sent from MainActivity
    public AddNRAMoneyListAdpter(Context context, List<AddMoneyModeDAO> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        preferences = context.getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_addmoney_rnahistory_details, parent, false);
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
        myHolder.nAmt.setText(current.getNotification_amount());
        myHolder.nAmt.setTag(position);

        myHolder.uaAmt.setText(current.getUser_app_referral_amount());
        myHolder.uaAmt.setTag(position);

        myHolder.baAmt.setText(current.getBusiness_app_referral_amount());
        myHolder.baAmt.setTag(position);

        myHolder.iAmt.setText(current.getInitial_deposit_amount());
        myHolder.iAmt.setTag(position);

        myHolder.clickAddMony.setTag(position);
        myHolder.clickAddMony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ID = (Integer) v.getTag();
                Log.e("", "list Id" + ID);
                current = data.get(ID);
                prefEditor.putString("id", current.getId());
                prefEditor.putString("notification_amount", current.getNotification_amount());
                prefEditor.putString("user_app_referral_amount", current.getUser_app_referral_amount());
                prefEditor.putString("business_app_referral_amount", current.getBusiness_app_referral_amount());
                prefEditor.putString("initial_deposit_amount", current.getInitial_deposit_amount());
                prefEditor.commit();
                UpdateNAREntryView updateNAREntryView = new UpdateNAREntryView();
                updateNAREntryView.show(((FragmentActivity) context).getSupportFragmentManager(), "updateNAREntryView");

            }
        });

    }


    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        TextView nAmt, uaAmt, baAmt, iAmt;
        LinearLayout clickAddMony;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            nAmt = (TextView) itemView.findViewById(R.id.nAmt);
            uaAmt = (TextView) itemView.findViewById(R.id.uaAmt);
            baAmt = (TextView) itemView.findViewById(R.id.baAmt);
            iAmt = (TextView) itemView.findViewById(R.id.iAmt);

            clickAddMony = (LinearLayout) itemView.findViewById(R.id.clickAddMony);
        }

    }


}
