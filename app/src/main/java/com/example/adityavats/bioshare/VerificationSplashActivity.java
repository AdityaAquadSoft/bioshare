package com.example.adityavats.bioshare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class VerificationSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_splash);
        Thread splashVerificationThread=new Thread(){
            public void run(){
                try {
                    sleep(2000);
                        Intent splashIntent = new Intent();
                        splashIntent.setClass(getApplicationContext(), WhatSellingActivity.class);
                        startActivity(splashIntent);
                        finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        splashVerificationThread.start();
    }
}
