package com.amzsoft.hostel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CollageNameActivity extends AppCompatActivity {
    String pinFromFirestore ="";
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
                            collegeNames.add("Please select a college");
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


    private void fetchPinFromFirestore(String selectedCollege, PinFetchCallback callback) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference collegeDocument = firestore.collection("collage_name").document(selectedCollege);

        collegeDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Check if "collage_pin" field exists in the document
                        if (document.contains("collage_pin")) {
                            // Retrieve the PIN
                            pinFromFirestore = document.getString("collage_pin");

                            // Now you can use the retrieved PIN as needed
                            // For example, you can compare it with the entered PIN

                            // Invoke the callback
                            callback.onPinFetched(pinFromFirestore);
                        } else {
                            // "collage_pin" field doesn't exist in the document
                            // Handle the case where the PIN is not available
                            callback.onPinFetched(null);
                        }
                    } else {
                        // Handle the case where the document doesn't exist
                        callback.onPinFetched(null);
                    }
                } else {
                    // Handle errors
                    Toast.makeText(CollageNameActivity.this, "Error fetching PIN: " + task.getException(), Toast.LENGTH_SHORT).show();
                    callback.onPinFetched(null);
                }
            }
        });
    }


    public void onNextButtonClick(View view) {
        // Handle the "Next" button click
        String selectedCollege = collegeSpinner.getSelectedItem().toString();

        // Validate if a college is selected
        if (getString(R.string.select_college).equals(selectedCollege)) {
            // Show an error message or set an error on the spinner
            Toast.makeText(this, R.string.select_college_error, Toast.LENGTH_SHORT).show();
            return; // Exit the method if no college is selected
        }

        // Validate PIN
        TextInputEditText pinEditText = findViewById(R.id.pinEditText);
        String enteredPin = pinEditText.getText().toString().trim();

        // Fetch PIN from Firestore with a callback
        fetchPinFromFirestore(selectedCollege, new PinFetchCallback() {
            @Override
            public void onPinFetched(String pin) {
                if (pin != null && pin.equals(enteredPin)) {
                    // PIN is correct, navigate to MainActivity

                    // Create an Intent to start the MainActivity
                    Intent intent = new Intent(CollageNameActivity.this, MainActivity.class);

                    // You can pass the selected college name to MainActivity if needed
                    intent.putExtra("selectedCollege", selectedCollege);

                    // Start the MainActivity
                    startActivity(intent);
                } else {
                    // Incorrect PIN or PIN not available, show a toast message
                    Toast.makeText(CollageNameActivity.this, "Incorrect PIN. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void onContactHereClick(View view) {
        // Handle the click event here
        Toast.makeText(this, "Contact here clicked!", Toast.LENGTH_SHORT).show();
        // Add your desired action, such as opening a contact form or performing any other operation.
        Intent intent = new Intent(CollageNameActivity.this, CollageNotListedActivity.class);
        startActivity(intent);
        //
    }
    private interface PinFetchCallback {
        void onPinFetched(String pin);
    }

}