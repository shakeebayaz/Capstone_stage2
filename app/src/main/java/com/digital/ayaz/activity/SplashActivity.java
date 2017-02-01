package com.digital.ayaz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.digital.ayaz.R;
import com.digital.ayaz.Utils.Constants;

public class SplashActivity extends AppCompatActivity {


    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                nextActivity(MainActivity.class);
                finish();
            }
        }, Constants.SPLASH_TIME_OUT);
        findViewById(R.id.title).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                nextActivity(MainActivity.class);
                return true;
            }
        });
    }

    public void nextActivity(Class className) {
        Intent i = new Intent(SplashActivity.this, className);
        startActivity(i);
    }

}
