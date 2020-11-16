package com.qinglianyun.loginintercept;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Login2Activity extends AppCompatActivity {

    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        final LoginCarrier carrier = getIntent().getParcelableExtra(LoginInterceptor.mINVOKER);

        mBtn = findViewById(R.id.btn_login);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginApplication.setLoginStatus(true);
                Toast.makeText(Login2Activity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                if (null != carrier) {
                    carrier.invoke(Login2Activity.this);
                    finish();
                } else {
                    Intent intent = new Intent(Login2Activity.this, MainActivity.class);
                    Login2Activity.this.startActivity(intent);
                    finish();
                }
            }
        });
    }
}
