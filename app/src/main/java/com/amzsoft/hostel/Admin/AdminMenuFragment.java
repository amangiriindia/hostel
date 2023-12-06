package com.amzsoft.hostel.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.amzsoft.hostel.R;

public class AdminMenuFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_menu, container, false);
    }
    private String getAdminSelectedCollege(Context context) {
        SharedPreferences adminPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        return adminPreferences.getString("selectedCollege", "");
    }
}