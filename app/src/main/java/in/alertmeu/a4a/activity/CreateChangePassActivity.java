package in.alertmeu.a4a.activity;

import androidx.appcompat.app.AppCompatActivity;
import in.alertmeu.a4a.R;
import in.alertmeu.a4a.utils.AppStatus;
import in.alertmeu.a4a.utils.Constant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateChangePassActivity extends AppCompatActivity {
    EditText uPassword;
    Button btnNext;
    String password = "", mobile = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_change_pass);
        uPassword = (EditText) findViewById(R.id.uPassword);
        btnNext = (Button) findViewById(R.id.btnNext);
        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    password = uPassword.getText().toString().trim();
                    if (password.length() < 6 && !isValidPassword(password)) {

                        Toast.makeText(getApplicationContext(), "Not Valid", Toast.LENGTH_SHORT).show();
                    } else {

                        // Toast.makeText(getApplicationContext(), "Valid", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateChangePassActivity.this, ReEnterChangePassctivity.class);
                        intent.putExtra("password", password);
                        intent.putExtra("mobile", mobile);
                        startActivity(intent);
                    }


                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
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

}


