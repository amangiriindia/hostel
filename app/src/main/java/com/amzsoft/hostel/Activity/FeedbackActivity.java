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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {

    Spinner feedbackTypeSpinner;
    EditText headingEditText,descriptionEditText;
     String selectedCollege;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedbackTypeSpinner = findViewById(R.id.feedbackTypeSpinner);
         headingEditText = findViewById(R.id.headingEditText);
         descriptionEditText = findViewById(R.id.descriptionEditText);


        selectedCollege = getIntent().getStringExtra("selectedCollage");

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

    private void submitFeedback(String feedbackType, String heading, String description, String selectedCollege) {
        // Get data from form



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
        storeFeedback(feedbackType, heading, description, selectedCollege);
    }

    private void storeFeedback(String feedbackType, String heading, String description, String selectedCollege) {
        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a map to represent the feedback data
        Map<String, Object> feedbackData = new HashMap<>();
        feedbackData.put("feedbackType", feedbackType);
        feedbackData.put("heading", heading);
        feedbackData.put("description", description);

        // Add the feedback data to Firestore
        db.collection("collage_name")
                .document(selectedCollege)
                .collection("feedback")
                .add(feedbackData)
                .addOnSuccessListener(documentReference -> {
                    // Feedback data added successfully
                    Toast.makeText(FeedbackActivity.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Finish the activity or navigate to another screen if needed
                })
                .addOnFailureListener(e -> {
                    // Handle failures
                    Toast.makeText(FeedbackActivity.this, "Error submitting feedback: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
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
        // Get data from form
        String feedbackType = feedbackTypeSpinner.getSelectedItem().toString();
        String heading = headingEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        // Validate data and submit feedback
        submitFeedback(feedbackType, heading, description, selectedCollege);
    }




}
