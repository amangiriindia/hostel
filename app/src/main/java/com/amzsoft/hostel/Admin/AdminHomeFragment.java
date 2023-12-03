package com.amzsoft.hostel.Admin;

import android.os.Bundle;
import android.os.Handler;
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
import com.amzsoft.hostel.Adapter.ServiceItemAdapter;
import com.amzsoft.hostel.Model.DailyItemModel;
import com.amzsoft.hostel.Model.ServiceItemsModel;
import com.amzsoft.hostel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private RecyclerView nearbyServicesRecyclerView;
    private List<ServiceItemsModel> serviceItemList;
    private ServiceItemAdapter serviceItemAdapter;
    private String selectedCollege = "Ambalika Institute of Management and Technology";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_admin_home, container, false);


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







        nearbyServicesRecyclerView = rootView.findViewById(R.id.admin_nearbyServicesRecyclerView);
        nearbyServicesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        serviceItemList = new ArrayList<>();
        serviceItemAdapter = new ServiceItemAdapter(serviceItemList, getContext());
        nearbyServicesRecyclerView.setAdapter(serviceItemAdapter);
        fetchNearbyServicesData();










        return rootView;
    }












    private void fetchNearbyServicesData() {


        // Fetch data from Firestore for nearby services
        CollectionReference servicesRef =
                firestore.collection("collage_name")
                        .document(selectedCollege)
                        .collection("service");

        servicesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        ServiceItemsModel serviceItem = document.toObject(ServiceItemsModel.class);
                        if (serviceItem != null) {
                            serviceItemList.add(serviceItem);
                        }
                    }
                    serviceItemAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Error fetching nearby services", Toast.LENGTH_SHORT).show();
                }
            }
        });
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