package com.amzsoft.hostel;

import static com.amzsoft.hostel.CollageNameActivity.DATA_PASSED_FLAG;
import static com.amzsoft.hostel.CollageNameActivity.PREFS_NAME;
import static com.amzsoft.hostel.CollageNameActivity.SELECTED_COLLEGE_KEY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (isDataPassed()) {
            // Retrieve selectedCollege from SharedPreferences
            String selectedCollege = getSelectedCollege();

            // Check if selectedCollege is not null
            if (selectedCollege != null) {
                // Display a toast message with the selected college value
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                intent.putExtra("selectedCollege", selectedCollege);

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
            }else{

            }
        }else {
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

    private boolean isDataPassed() {
        // Retrieve dataPassedFlag from SharedPreferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(DATA_PASSED_FLAG, false);
    }

    private String getSelectedCollege() {
        // Retrieve selectedCollege from SharedPreferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(SELECTED_COLLEGE_KEY, null);
    }
}
