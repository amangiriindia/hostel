package com.amzsoft.hostel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CollageNameActivity extends AppCompatActivity {

    private Spinner collegeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_name);

        collegeSpinner = findViewById(R.id.collegeSpinner);
        Button nextButton = findViewById(R.id.nextButton);

        // Fetch college names from Firestore and populate the spinner
        fetchCollegeNames();

        collegeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle the selected item (if needed)
                String selectedCollege = parentView.getItemAtPosition(position).toString();
                // You can use 'selectedCollege' as needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
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
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Assuming the document ID is the college name
                                String collegeName = document.getId();
                                collegeNames.add(collegeName);
                            }

                            // Populate the spinner with the retrieved college names
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    CollageNameActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    collegeNames
                            );
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            collegeSpinner.setAdapter(adapter);
                        } else {
                            // Handle errors
                            Toast.makeText(
                                    CollageNameActivity.this,
                                    "Error fetching college names: " + task.getException(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                });
    }

    public void onNextButtonClick(View view) {
        // Handle the "Next" button click
        String selectedCollege = collegeSpinner.getSelectedItem().toString();

        // Perform actions with the selected college name (e.g., navigate to the next screen)
        Toast.makeText(this, "Selected College: " + selectedCollege, Toast.LENGTH_SHORT).show();

        // Create an Intent to start the MainActivity
        Intent intent = new Intent(this, MainActivity.class);

        // You can pass the selected college name to MainActivity if needed
        intent.putExtra("selectedCollege", selectedCollege);

        // Start the MainActivity
        startActivity(intent);
    }

}