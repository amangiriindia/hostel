package com.amzsoft.hostel.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.amzsoft.hostel.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeFragment extends Fragment {





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time, container, false);
    }
    public static TimeFragment newInstance(String selectedCollege) {
        TimeFragment fragment = new TimeFragment();
        Bundle args = new Bundle();
        args.putString("selectedCollege", selectedCollege);
        fragment.setArguments(args);
        return fragment;
    }

}