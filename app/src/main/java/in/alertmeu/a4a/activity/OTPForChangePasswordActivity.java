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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class OTPForChangePasswordActivity extends AppCompatActivity {
    EditText edtCode;
    Button btnNext;
    String id;
    TextView txtId;
    String code = "", mobile = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpfor_change_password);
        edtCode = (EditText) findViewById(R.id.edtCode);
        btnNext = (Button) findViewById(R.id.btnNext);
        txtId = (TextView) findViewById(R.id.txtId);
        Intent intent = getIntent();
        mobile = intent.getStringExtra("mobile");
        Random random = new Random();
        id = String.format("%06d", random.nextInt(1000000));
        txtId.setText(id);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    code = edtCode.getText().toString().trim();
                    if (code.equals(id)) {
                        Intent intent = new Intent(OTPForChangePasswordActivity.this, CreateChangePassActivity.class);
                        intent.putExtra("mobile", mobile);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Code mismatch. Please try again!", Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(), Constant.INTERNET_MSG, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}


