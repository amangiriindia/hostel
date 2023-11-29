package com.amzsoft.hostel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class CollageNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_name);

        Spinner collegeSpinner = findViewById(R.id.collegeSpinner);
        Button nextButton = findViewById(R.id.nextButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.college_names,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        collegeSpinner.setAdapter(adapter);

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

    public void onNextButtonClick(View view) {
        // Handle the "Next" button click
        Spinner collegeSpinner = findViewById(R.id.collegeSpinner);
        String selectedCollege = collegeSpinner.getSelectedItem().toString();

        // Perform actions with the selected college name (e.g., navigate to the next screen)
        Toast.makeText(this, "Selected College: " + selectedCollege, Toast.LENGTH_SHORT).show();

    }
}