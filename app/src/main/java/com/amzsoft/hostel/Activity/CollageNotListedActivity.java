package com.amzsoft.hostel.Activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.amzsoft.hostel.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DatabaseReference;

public class CollageNotListedActivity extends AppCompatActivity {
    private EditText editTextCollegeName, editTextLocation, editTextName,
            editTextEmail, editTextPhoneNumber,editTextCourse,editTextYear;
    private Button submitBtn;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_not_listed);



        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        MaterialToolbar toolbar = findViewById(R.id.clg_list_toolbar);
        setSupportActionBar(toolbar);

        // Enable the Up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set the Up button color to white
            Drawable upArrow = getResources().getDrawable(R.drawable.back_arrow);
            actionBar.setHomeAsUpIndicator(upArrow);
        }

        // Initialize Firebase

        // Initialize Views
        editTextCollegeName = findViewById(R.id.editTextCollegeName);
        editTextLocation = findViewById(R.id.editTextCollegeLocation);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextCourse = findViewById(R.id.editTextCourse);
        editTextYear = findViewById(R.id.editTextYear);

        submitBtn = findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });



    }

    private void submitForm() {
        // Get data from form
        String collegeName = editTextCollegeName.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();
        String course = editTextCourse.getText().toString().trim();
        String yearText = editTextYear.getText().toString().trim();

        // Validate data
        if (TextUtils.isEmpty(collegeName)) {
            editTextCollegeName.setError("Required");
            return;
        }

        if (TextUtils.isEmpty(location)) {
            editTextLocation.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            editTextPhoneNumber.setError("Required");
            return;
        }
        if (TextUtils.isEmpty(course)) {
            editTextCourse.setError("Required");
            return;
        }

        if (TextUtils.isEmpty(yearText)) {
            editTextYear.setError("Required");
            return;
        }


        Toast.makeText(this, "Your Data submitted successfully our Team will be contact with in 2 working day!", Toast.LENGTH_SHORT).show();

        // Repeat the validation for other fields...

        // If all data is valid, proceed to store in Firebase

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the Up button click
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}