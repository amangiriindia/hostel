package com.amzsoft.hostel.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amzsoft.hostel.Adapter.ServiceItemAdapter;
import com.amzsoft.hostel.Model.ServiceItemsModel;
import com.amzsoft.hostel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminNearByFragment extends Fragment {

    private RecyclerView nearbyServicesRecyclerView;
    private List<ServiceItemsModel> serviceItemList;
    private ServiceItemAdapter serviceItemAdapter;
    private String selectedCollege = "";
    FirebaseFirestore firestore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_admin_near_by, container, false);

        firestore = FirebaseFirestore.getInstance();

        String college = getAdminSelectedCollege(getContext());
        if (!TextUtils.isEmpty(college)) {
            // Do something with the selected college name
            selectedCollege = college;
        } else {
            // Handle the case where the selected college name is not found
            // You might want to show a default college or take some appropriate action
            Toast.makeText(getActivity(), "Selected college not found", Toast.LENGTH_SHORT).show();
        }

        nearbyServicesRecyclerView = rootView.findViewById(R.id.admin_nearbyServicesRecyclerView);
        nearbyServicesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        serviceItemList = new ArrayList<>();
        serviceItemAdapter = new ServiceItemAdapter(serviceItemList, getContext());
        nearbyServicesRecyclerView.setAdapter(serviceItemAdapter);
        fetchNearbyServicesData();

        return rootView;
    }


    public String getAdminSelectedCollege(Context context) {
        SharedPreferences adminPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        return adminPreferences.getString("selectedCollege", "");
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

}