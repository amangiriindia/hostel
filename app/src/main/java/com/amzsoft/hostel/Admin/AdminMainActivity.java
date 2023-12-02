package com.amzsoft.hostel.Admin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
        
        drawerLayout =findViewById(R.id.adminDrawerLayout);
        navigationView =findViewById(R.id.admimNavigationView);
        toolbar =findViewById(R.id.admin_toolbar);
        
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(
                this,drawerLayout,toolbar,R.string.open_drawer,R.string.close_drawer);
        
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        loadFragment(new AdminMenuFragment());


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id =item.getItemId();
                if(id == R.id.items1){
                    loadFragment(new AdminContactFragment());
                } else if (id ==R.id.items2) {
                    loadFragment(new AdminMenuFragment());
                }

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });


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
        FragmentManager fm =getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.admin_container,fragment);
        ft.commit();
    }
}