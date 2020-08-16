package in.alertmeu.a4a.activity;

import androidx.appcompat.app.AppCompatActivity;
import in.alertmeu.a4a.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelpCenterActivity extends AppCompatActivity {
    LinearLayout logout;
    static SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    LinearLayout changenumber, faq, contactus, termsprivacy, changepassword;
    TextView appversion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_center);
        preferences = getSharedPreferences("Prefrence", Context.MODE_PRIVATE);
        prefEditor = preferences.edit();
        changenumber = (LinearLayout) findViewById(R.id.changenumber);
        changepassword = (LinearLayout) findViewById(R.id.changepassword);
        logout = (LinearLayout) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HelpCenterActivity.this, LogoutActivity.class);
                startActivity(intent);
                finish();

            }
        });

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpCenterActivity.this, ChangeMyPasswordActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}
