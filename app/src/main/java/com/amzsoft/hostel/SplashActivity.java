package com.amzsoft.hostel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(SplashActivity.this, CollageNameActivity.class);

        // Create a Handler to post a delayed action
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the HomeActivity after a 5-second delay
                startActivity(intent);

                // Close the splash activity
                finish();
            }
        }, 5000); // 5000 milliseconds = 5 seconds

    }
}