package com.amzsoft.hostel.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amzsoft.hostel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminLoginActivity extends AppCompatActivity {

    private TextInputEditText adminPinEditText;
    private EditText adminUserIdEditText;
    private Spinner collegeSpinner;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        adminPinEditText = findViewById(R.id.admin_pinEditText);
        adminUserIdEditText = findViewById(R.id.admin_id);
        collegeSpinner =findViewById(R.id.admin_collegeSpinner);

        // Your existing initialization code goes here

        fetchCollegeNames();
    }





    private void fetchCollegeNames() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("collage_name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> collegeNames = new ArrayList<>();
                            collegeNames.add("Please select a college");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Assuming the document ID is the college name
                                String collegeName = document.getId();
                                collegeNames.add(collegeName);
                            }

                            // Populate the spinner with the retrieved college names
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    AdminLoginActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    collegeNames
                            );
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            collegeSpinner.setAdapter(adapter);
                        } else {
                            // Handle errors
                            Toast.makeText(
                                    AdminLoginActivity.this,
                                    "Error fetching college names: " + task.getException(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });
    }

    public void onAdminLoginButtonClick(View view) {
        // Handle the click on the "LOGIN" button
        String adminPin = adminPinEditText.getText().toString();
        String adminUserId = adminUserIdEditText.getText().toString();
        String selectedCollege = collegeSpinner.getSelectedItem().toString();

        // Check if adminPin, adminUserId, and selectedCollege are not empty
        if (!adminPin.isEmpty() && !adminUserId.isEmpty() && !selectedCollege.equals("Please select a college")) {
            // Fetch data from Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("collage_name")
                    .document(selectedCollege)
                    .collection("admin_id_password")
                    .document(adminUserId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Document exists, check if the password matches
                                    String storedPassword = document.getString("admin_password");
                                    if (adminPin.equals(storedPassword)) {
                                        // Passwords match, login successful
                                        Intent intent =new Intent(AdminLoginActivity.this,AdminMainActivity.class);
                                        intent.putExtra("selectedCollage",selectedCollege);
                                        startActivity(intent);
                                        storeUserData(selectedCollege,true);
                                        finish();

                                        // Add code to navigate to AdminHomeActivity if needed
                                    } else {
                                        // Passwords do not match
                                        Toast.makeText(
                                                AdminLoginActivity.this,
                                                "Incorrect password",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                } else {
                                    // Document does not exist
                                    Toast.makeText(
                                            AdminLoginActivity.this,
                                            "User not found",
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            } else {
                                // Handle errors
                                Toast.makeText(
                                        AdminLoginActivity.this,
                                        "Error fetching user data: " + task.getException(),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    });
        } else {
            // Fields are empty
            Toast.makeText(
                    AdminLoginActivity.this,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void storeUserData(String selectedCollege, boolean adminLoginStatus) {
        // Store selected college and admin login status in SharedPreferences
        SharedPreferences adminpreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = adminpreferences.edit();
        editor.putString("selectedCollege", selectedCollege);
        editor.putBoolean("adminLoginStatus", adminLoginStatus);
        editor.apply();
    }


}
