package com.amzsoft.hostel.Activity;

import static com.amzsoft.hostel.Activity.CollageNameActivity.DATA_PASSED_FLAG;
import static com.amzsoft.hostel.Activity.CollageNameActivity.PREFS_NAME;
import static com.amzsoft.hostel.Activity.CollageNameActivity.SELECTED_COLLEGE_KEY;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.amzsoft.hostel.Admin.AdminMainActivity;
import com.amzsoft.hostel.R;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        String adminCollageName =getadminSelectedCollege();
        Boolean adminLoginFlag = isAdminLoggedIn();

        if(adminLoginFlag == true){
            Intent intent =new Intent(SplashActivity.this, AdminMainActivity.class);
            intent.putExtra("selectedCollege",adminCollageName);
            startDelayedActivity(intent,2000);
        }
        else if (isDataPassed()) {
            // Retrieve selectedCollege from SharedPreferences
            String selectedCollege = getSelectedCollege();
            if (selectedCollege != null) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putExtra("selectedCollege", selectedCollege);
                startDelayedActivity(intent, 2000);

            }

        }else {
            Intent intent = new Intent(SplashActivity.this, CollageNameActivity.class);
             startDelayedActivity(intent,2000);
        }



    }
    private String getadminSelectedCollege() {
        SharedPreferences adminPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        return adminPreferences.getString("selectedCollege", "");
    }

    private boolean isAdminLoggedIn() {
        SharedPreferences adminPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        return adminPreferences.getBoolean("adminLoginStatus", false);
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
    private void startDelayedActivity(Intent intent, long delayMillis) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish(); // Close the splash activity
            }
        }, delayMillis);
    }

}
