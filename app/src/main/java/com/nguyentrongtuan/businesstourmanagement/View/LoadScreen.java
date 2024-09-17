package com.nguyentrongtuan.businesstourmanagement.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.nguyentrongtuan.businesstourmanagement.R;

public class LoadScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_load);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent iLogin = new Intent(LoadScreen.this, LoginActivity.class);
                startActivity(iLogin);
            }
        }, 2000);
    }
}