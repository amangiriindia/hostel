package com.amzsoft.hostel.Activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.amzsoft.hostel.R;
import com.google.android.material.appbar.MaterialToolbar;

public class FeedbackActivity extends AppCompatActivity {

    Spinner feedbackTypeSpinner;
    EditText headingEditText,descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedbackTypeSpinner = findViewById(R.id.feedbackTypeSpinner);
         headingEditText = findViewById(R.id.headingEditText);
         descriptionEditText = findViewById(R.id.descriptionEditText);

        submitFeedback();
        String selectedCollege = getIntent().getStringExtra("selectedCollege");

        // Enable the Up button
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the Up button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            // Set the Up button color to white
            Drawable upArrow = getResources().getDrawable(R.drawable.back_arrow);
            actionBar.setHomeAsUpIndicator(upArrow);
        }


    }

    private void submitFeedback() {
        // Get data from form


        String feedbackType = feedbackTypeSpinner.getSelectedItem().toString();
        String heading = headingEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        // Validate data
        if (getString(R.string.select_feedback_type).equals(feedbackType)) {
            // Show an error message or set an error on the spinner
            Toast.makeText(this, R.string.select_feedback_type_error, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(heading)) {
            headingEditText.setError("Required");
            return;
        }

        if (TextUtils.isEmpty(description)) {
            descriptionEditText.setError("Required");
            return;
        }

        // If all data is valid, proceed to store in Firebase
        storeFeedback(feedbackType, heading, description);
    }

    private void storeFeedback(String feedbackType, String heading, String description) {
        // Here, you can use Firebase to store the feedback data
        // For example, you can use Firebase Realtime Database or Firestore

        // In this example, I'll use a simple Toast message to simulate storing data
        String feedbackMessage = "Feedback Type: " + feedbackType +
                "\nHeading: " + heading +
                "\nDescription: " + description;

        Toast.makeText(this, feedbackMessage, Toast.LENGTH_SHORT).show();
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
    public void onSubmitButtonClick(View view) {
        submitFeedback();
    }
}
