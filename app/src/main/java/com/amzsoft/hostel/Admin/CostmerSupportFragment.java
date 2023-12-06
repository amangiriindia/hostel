package com.amzsoft.hostel.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.amzsoft.hostel.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CostmerSupportFragment extends Fragment {

    private EditText headingEditText;
    private EditText descriptionEditText;
    private String selectedCollege ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_costmer_support, container, false);
        String college = getAdminSelectedCollege(getContext());
        if (!TextUtils.isEmpty(college)) {
            // Do something with the selected college name
            selectedCollege = college;
        } else {
            // Handle the case where the selected college name is not found
            // You might want to show a default college or take some appropriate action
            Toast.makeText(getActivity(), "Selected college not found", Toast.LENGTH_SHORT).show();
        }
        // Initialize your EditText views
        headingEditText = rootView.findViewById(R.id.headingEditText);
        descriptionEditText = rootView.findViewById(R.id.descriptionEditText);

        // Set a click listener for the submit button
        Button submitBtn = rootView.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the submitFeedback method when the button is clicked
                submitFeedback();
            }
        });

        return rootView;
    }

    private void submitFeedback() {
        // Get data from form
        String feedbackType = "Costumer Support"; // Assuming the feedback type is fixed here
        String heading = headingEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        // Validate data
        if (TextUtils.isEmpty(heading)) {
            headingEditText.setError("Required");
            return;
        }

        if (TextUtils.isEmpty(description)) {
            descriptionEditText.setError("Required");
            return;
        }

        // You need to get the selectedCollege from somewhere; it's not clear from your provided code

        // If all data is valid, proceed to store in Firebase
        storeFeedback(feedbackType, heading, description, selectedCollege);
    }

    private String getAdminSelectedCollege(Context context) {
        SharedPreferences adminPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        return adminPreferences.getString("selectedCollege", "");
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
                    Toast.makeText(getContext(), "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                    // Finish the activity or navigate to another screen if needed
                })
                .addOnFailureListener(e -> {
                    // Handle failures
                    Toast.makeText(getContext(), "Error submitting feedback: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}