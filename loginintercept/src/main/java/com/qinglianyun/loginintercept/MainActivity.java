package com.qinglianyun.loginintercept;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ServiceLoader;

public class MainActivity extends AppCompatActivity {

    private TextView mTvJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Serviceloader
        ServiceLoader.load(MainActivity.class);
        mTvJump = findViewById(R.id.tv_jump_order_detail);
        mTvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                MainActivity.this.startActivity(intent);


//                LoginInterceptor.interceptor(MainActivity.this, OrderDetailActivity.class.getName(), null);
            }
        });
    }
}
