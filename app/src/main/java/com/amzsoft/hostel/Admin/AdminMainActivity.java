package com.amzsoft.hostel.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.amzsoft.hostel.Activity.CollageNameActivity;
import com.amzsoft.hostel.R;
import com.google.android.material.navigation.NavigationView;

public class AdminMainActivity extends AppCompatActivity {
    
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);



        drawerLayout = findViewById(R.id.adminDrawerLayout);
        navigationView = findViewById(R.id.admimNavigationView);
        toolbar = findViewById(R.id.admin_toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Disable the default drawer indicator
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(android.R.color.white));
        toggle.setDrawerIndicatorEnabled(false);

        // Set a custom icon for the toggle button
        toggle.setHomeAsUpIndicator(R.drawable.baseline_menu_24);

        toolbar.setNavigationOnClickListener(view -> {
            // Handle the custom toggle button click here
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        loadFragment(new AdminHomeFragment());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle item clicks here
                int id = item.getItemId();

                if (id == R.id.admin_menu) {
                    // Handle admin_menu click

                    loadFragment(new AdminMenuFragment());
                } else if (id == R.id.admin_nearby_service) {
                    // Handle admin_nearby_service click
                    loadFragment(new AdminNearByFragment());
                } else if (id == R.id.admin_Slider) {
                    // Handle admin_Slider click
                    loadFragment(new AdminSliderFragment());
                } else if (id == R.id.admin_feedback) {
                    // Handle admin_feedback click
                    loadFragment(new AdminFeedbackFragment());
                } else if (id == R.id.admin_contact) {
                    // Handle admin_Contact click
                    loadFragment(new AdminContactFragment());
                } else if (id == R.id.admin_timing) {
                    // Handle admin_timing click
                    loadFragment(new AdminTimingFragment());
                }else if (id == R.id.admin_support) {
                    // Handle admin_Custmer_support click
                    loadFragment(new CostmerSupportFragment());
                }else if(id == R.id.admin_home){
                    loadFragment(new AdminHomeFragment());
                } else if (id == R.id.admin_logout) {
                  showLogoutConfirmationDialog();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    // Add this method to show the confirmation dialog for logout
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User confirmed, proceed with logout
                logoutAdmin();

                // Navigate to CollageNameActivity after logout
                Intent intent = new Intent(AdminMainActivity.this, CollageNameActivity.class);
                startActivity(intent);
                finish(); // Optionally, finish the current activity after starting a new one
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User canceled, do nothing
            }
        });
        builder.show();
    }


    private void logoutAdmin() {
        SharedPreferences adminPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = adminPreferences.edit();

        // Set selectedCollege to an empty string
        editor.putString("selectedCollege", "");

        // Set adminLoginStatus to false
        editor.putBoolean("adminLoginStatus", false);

        // Apply the changes
        editor.apply();
    }

    @Override
    public void onBackPressed() {
      if(drawerLayout.isDrawerOpen(GravityCompat.START)){
          drawerLayout.closeDrawer(GravityCompat.START);
      }else{
          super.onBackPressed();
      }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // Replace the existing fragment with the new one
        ft.replace(R.id.admin_container, fragment);

        // Add the transaction to the back stack
        ft.addToBackStack(null);

        // Commit the transaction
        ft.commit();
    }

}