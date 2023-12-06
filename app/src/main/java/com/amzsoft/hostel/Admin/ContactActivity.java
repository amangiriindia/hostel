package com.amzsoft.hostel.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.amzsoft.hostel.R;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
    }
    private String getAdminSelectedCollege(Context context) {
        SharedPreferences adminPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        return adminPreferences.getString("selectedCollege", "");
    }
}