package com.amzsoft.hostel.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.amzsoft.hostel.Adapter.DailyItemsAdapter;
import com.amzsoft.hostel.Adapter.ImageSliderAdapter;
import com.amzsoft.hostel.Model.DailyItemModel;
import com.amzsoft.hostel.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class AdminHomeFragment extends Fragment {

    private ViewPager2 viewPager2;
    private ImageSliderAdapter imageSliderAdapter;


    RecyclerView recyclerView;
    List<DailyItemModel> dailyItemModelList;
    DailyItemsAdapter dailyItemAdapter;
    private FirebaseFirestore firestore;


    private Timer timer;
    private final long DELAY_MS = 2000;
    private final long PERIOD_MS = 3000;

    private String selectedCollege;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_admin_home, container, false);

        String college = getAdminSelectedCollege(getContext());
        if (!TextUtils.isEmpty(college)) {
            // Do something with the selected college name
            selectedCollege = college;
        } else {
            // Handle the case where the selected college name is not found
            // You might want to show a default college or take some appropriate action
            Toast.makeText(getActivity(), "Selected college not found", Toast.LENGTH_SHORT).show();
        }
        firestore = FirebaseFirestore.getInstance();




        // Initialize viewPager2 and imageSliderAdapter with selectedCollege
        viewPager2 = rootView.findViewById(R.id.admin_imageSliderViewPager);
        imageSliderAdapter = new ImageSliderAdapter(selectedCollege);

        // Set the adapter to viewPager2
        viewPager2.setAdapter(imageSliderAdapter);

        // Start auto slide
        startAutoSlide();




        try {
            // Initialize RecyclerView and set its layout manager and adapter
            recyclerView = rootView.findViewById(R.id.admin_recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            dailyItemModelList = new ArrayList<>();
            dailyItemAdapter = new DailyItemsAdapter(getContext(), dailyItemModelList);
            recyclerView.setAdapter(dailyItemAdapter);
            String currentday = getCurrentDay().toLowerCase();
            //   Toast.makeText(getActivity(), ""+currentday, Toast.LENGTH_SHORT).show();
            // Fetch data from Firestore


            firestore.collection("collage_name")
                    .document(selectedCollege)
                    .collection(currentday)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            // Clear the existing list before adding new items
                            dailyItemModelList.clear();

                            // Loop through the query snapshot and add items to the list
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                // Convert the document data to your model class
                                DailyItemModel dailyItemModel = document.toObject(DailyItemModel.class);

                                // Add the model to the list
                                dailyItemModelList.add(dailyItemModel);
                            }

                            // Notify the adapter about the changes
                            dailyItemAdapter.notifyDataSetChanged();

                            // Display a toast or perform other actions as needed
                            // Toast.makeText(getActivity(), "Data loaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle the failure case
                            Log.e("FirestoreError", "Failed to load data", e);
                            Toast.makeText(getActivity(), "Failed to load data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });



        } catch (Exception e) {
            Log.e("Aman", e.toString());
        }













        return rootView;
    }










    private String getAdminSelectedCollege(Context context) {
        SharedPreferences adminPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        return adminPreferences.getString("selectedCollege", "");
    }



    private void startAutoSlide() {
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                int itemCount = imageSliderAdapter.getItemCount();
                if (itemCount > 0) {
                    int nextPage = (viewPager2.getCurrentItem() + 1) % itemCount;
                    viewPager2.setCurrentItem(nextPage, true);
                }
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    public static String getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        return dateFormat.format(calendar.getTime());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }



}