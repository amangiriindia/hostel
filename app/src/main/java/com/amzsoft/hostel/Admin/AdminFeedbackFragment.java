package com.amzsoft.hostel.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amzsoft.hostel.Adapter.FeedbackAdapter;
import com.amzsoft.hostel.Model.FeedbackModel;
import com.amzsoft.hostel.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class AdminFeedbackFragment extends Fragment {

    private String selectedCollege;

    RecyclerView recyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView= inflater.inflate(R.layout.fragment_admin_feedback, container, false);

         recyclerView = rootView.findViewById(R.id.admin_feedback_recyclerView);
        String college = getAdminSelectedCollege(getContext());
        if (!TextUtils.isEmpty(college)) {
            // Do something with the selected college name
            selectedCollege = college;
        } else {
            // Handle the case where the selected college name is not found
            // You might want to show a default college or take some appropriate action
            Toast.makeText(getActivity(), "Selected college not found", Toast.LENGTH_SHORT).show();
        }
        fetchDataAndDisplay();
        return rootView;
    }

    private String getAdminSelectedCollege(Context context) {
        SharedPreferences adminPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        return adminPreferences.getString("selectedCollege", "");
    }

    private void fetchDataAndDisplay() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Fetch data from Firestore
        db.collection("collage_name")
                .document(selectedCollege)
                .collection("feedback")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<FeedbackModel> feedbackList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        FeedbackModel feedback = document.toObject(FeedbackModel.class);
                        feedbackList.add(feedback);
                    }

                    // Display data in RecyclerView
                    displayFeedbackInRecyclerView(feedbackList);
                })
                .addOnFailureListener(e -> {
                    // Handle failures

                });
    }

    private void displayFeedbackInRecyclerView(List<FeedbackModel> feedbackList) {
        // Use rootView to find the RecyclerView in the Fragment layout


        // Check if recyclerView is not null before proceeding
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            // Create and set the adapter
            FeedbackAdapter feedbackAdapter = new FeedbackAdapter(feedbackList);
            recyclerView.setAdapter(feedbackAdapter);
        } else {
            // Handle the case when recyclerView is null
            Toast.makeText(getActivity(), "RecyclerView not found", Toast.LENGTH_SHORT).show();
        }
    }


}