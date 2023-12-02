package com.amzsoft.hostel.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.amzsoft.hostel.R;
import com.google.android.material.textfield.TextInputEditText;

public class AdminLoginActivity extends AppCompatActivity {

    private TextInputEditText adminPinEditText;
    private EditText adminUserIdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminPinEditText = findViewById(R.id.adminpinEditText);
        adminUserIdEditText = findViewById(R.id.adminUserIdEditText);

        // Your existing initialization code goes here
    }

    public void onAdminLoginButtonClick(View view) {
        // Handle the click on the "LOGIN" button
        String adminPin = adminPinEditText.getText().toString();
        String adminUserId = adminUserIdEditText.getText().toString();

        // Check if adminPin and adminUserId are not empty (You can add more validation as needed)
        if (!adminPin.isEmpty() && !adminUserId.isEmpty()) {
            // If both fields are not empty, navigate to AdminHomeActivity
            Intent intent = new Intent(this, AdminMainActivity.class);
            startActivity(intent);
        } else {
            // Show an error or prompt the user to fill in both fields
            // You can customize this part based on your app's requirements
            // For example, show a Toast or setError on the TextInputLayout
        }
    }
}
