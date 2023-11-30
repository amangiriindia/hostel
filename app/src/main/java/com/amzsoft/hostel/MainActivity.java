package com.amzsoft.hostel;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.amzsoft.hostel.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);

//        setSupportActionBar(toolbar);

        // Your other setup code

        ImageView customerSupportIcon = findViewById(R.id.customerSupportIcon);
        customerSupportIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click on the customer support icon
                // For example, open a customer support activity or dialog
            }
        });

        String selectedCollege = getIntent().getStringExtra("selectedCollege");

        if (selectedCollege != null) {
            Toast.makeText(this, "Selected College: " + selectedCollege, Toast.LENGTH_SHORT).show();

        }

        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();  // Get the selected item's ID

                // Check which item was selected and handle accordingly
                if (itemId == R.id.home) {
                    replaceFragment(HomeFragment.newInstance(selectedCollege), "HomeFragment");

                    return true;
                } else if (itemId == R.id.tomorrow) {
                    replaceFragment(TommrowFragment.newInstance(selectedCollege), "TommrowFragment");
                    return true;
                } else if (itemId == R.id.time) {
                    replaceFragment(TimeFragment.newInstance(selectedCollege), "TimeFragment");
                    return true;
                }

                // Return false for any other case
                return false;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, tag);
        fragmentTransaction.commit();
    }
}

