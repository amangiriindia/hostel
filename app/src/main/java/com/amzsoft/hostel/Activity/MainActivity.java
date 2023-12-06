package com.amzsoft.hostel.Activity;


import static com.amzsoft.hostel.Activity.CollageNameActivity.DATA_PASSED_FLAG;
import static com.amzsoft.hostel.Activity.CollageNameActivity.PREFS_NAME;
import static com.amzsoft.hostel.Activity.CollageNameActivity.SELECTED_COLLEGE_KEY;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.amzsoft.hostel.Admin.AdminLoginActivity;
import com.amzsoft.hostel.Fragment.HomeFragment;
import com.amzsoft.hostel.Fragment.TimeFragment;
import com.amzsoft.hostel.Fragment.TommrowFragment;
import com.amzsoft.hostel.R;
import com.amzsoft.hostel.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    String selectedCollege;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the selected college from the intent
        selectedCollege = getIntent().getStringExtra("selectedCollege");

        // Your other setup code

        // Set up the click listener for the customer support icon
        ImageView customerSupportIcon = findViewById(R.id.customerSupportIcon);
        customerSupportIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click on the customer support icon
                showHomePopup(v);
            }
        });

        // Rest of your onCreate method

        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Get the selected item's ID
                int itemId = item.getItemId();

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

        // Set the default selected item
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment, tag);
        fragmentTransaction.commit();
    }

    // Rest of your MainActivity code

    private void showHomePopup(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.nav_text_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();  // Get the selected item's ID

                // Handle clicks on the popup menu items using if-else statements
                if (itemId == R.id.menu_home_feedback) {
                    // Handle feedback option
                    Intent feedbackIntent = new Intent(MainActivity.this, FeedbackActivity.class);
                    feedbackIntent.putExtra("selectedCollage",selectedCollege);
                    startActivity(feedbackIntent);
                    return true;
                } else if (itemId == R.id.menu_home_logout) {
                    showLogoutConfirmationDialog();
                    // Implement your logout logic here
                    return true;
                } else {
                    return false;
                }
            }
        });

        // Show the popup menu
        popupMenu.show();
    }


    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Implement your logout logic here
                logout();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, user canceled logout
            }
        });

        builder.show();
    }



    private void logout() {
        // Handle logout option
        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
        startActivity(intent);

        // Clear SharedPreferences values
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(DATA_PASSED_FLAG, false);
        editor.remove(SELECTED_COLLEGE_KEY);
        editor.apply();

        finish();  // Finish the current activity
    }

    public void onAdminloginIconClick(View view) {
        // Handle the click on admin login icon
        showAdminLogin();
    }

    private void showAdminLogin() {
        // Redirect to AdminLoginActivity
        Intent intent = new Intent(this, AdminLoginActivity.class);
        startActivity(intent);
    }
}
