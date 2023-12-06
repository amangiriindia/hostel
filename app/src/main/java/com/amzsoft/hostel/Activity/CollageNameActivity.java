package com.amzsoft.hostel.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amzsoft.hostel.Admin.AdminLoginActivity;
import com.amzsoft.hostel.R;
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
    String pinFromFirestore = "";
    private Spinner collegeSpinner;
    private ImageView adminLoginDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_name);

        collegeSpinner = findViewById(R.id.collegeSpinner);
        adminLoginDots = findViewById(R.id.adminlogindots);

        adminLoginDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAdminLoginPopup(v);
            }
        });

        // Fetch college names from Firestore and populate the spinner
        fetchCollegeNames();
    }

    private void showAdminLoginPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.admin_nav_text, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.admin_login_option) {
                    // Handle admin login option
                    Intent intent = new Intent(CollageNameActivity.this, AdminLoginActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
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

                    // PIN is correct, save the flag indicating data has been passed
                    saveDataPassedFlag();

                    // Save selectedCollege to SharedPreferences
                    saveSelectedCollege(selectedCollege);

                    // Move to MainActivity
                    moveToMainActivity(selectedCollege);
                } else {
                    // Incorrect PIN or PIN not available, show a toast message
                    Toast.makeText(CollageNameActivity.this, "Incorrect PIN. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    static final String PREFS_NAME = "MyPrefsFile";
    static final String DATA_PASSED_FLAG = "dataPassedFlag";
    static final String SELECTED_COLLEGE_KEY = "selectedCollege";

    private void saveDataPassedFlag() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(DATA_PASSED_FLAG, true);
        editor.apply();
    }

    private boolean isDataPassed() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(DATA_PASSED_FLAG, false);
    }

    private void moveToMainActivity(String selectedCollege) {
        // Move to MainActivity
        Intent intent = new Intent(CollageNameActivity.this, MainActivity.class);
        intent.putExtra("selectedCollege", selectedCollege);
        startActivity(intent);
        finish(); // Corrected the method name
    }


    private void saveSelectedCollege(String selectedCollege) {
        // Save selectedCollege to SharedPreferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SELECTED_COLLEGE_KEY, selectedCollege);
        editor.apply();
    }

    public void onContactHereClick(View view) {
        // Handle the click event here

        // Add your desired action, such as opening a contact form or performing any other operation.
        Intent intent = new Intent(CollageNameActivity.this, CollageNotListedActivity.class);
        startActivity(intent);
        //
    }

    private interface PinFetchCallback {
        void onPinFetched(String pin);
    }

}
