package com.amzsoft.hostel.Admin;

import android.annotation.SuppressLint;
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

import com.amzsoft.hostel.Adapter.ContactItemAdapter;
import com.amzsoft.hostel.Model.ContactItemModel;
import com.amzsoft.hostel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class AdminContactFragment extends Fragment {

    private RecyclerView recyclerViewcontact;
    private List<ContactItemModel> contactList;
    private ContactItemAdapter contactAdapter;
    private FirebaseFirestore firestore;
    private String selectedCollege;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_admin_contact, container, false);

        // Initialize Firestore
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

        // RecyclerView setup
        recyclerViewcontact = rootView.findViewById(R.id.admin_contactrecyclerView);
        recyclerViewcontact.setLayoutManager(new LinearLayoutManager(getActivity()));
        contactList = new ArrayList<>();
        contactAdapter = new ContactItemAdapter(contactList, getContext());
        recyclerViewcontact.setAdapter(contactAdapter);

        // Fetch data from Firestore
        fetchDataFromFirestore();

        return rootView;
    }

    private String getAdminSelectedCollege(Context context) {
        SharedPreferences adminPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        return adminPreferences.getString("selectedCollege", "");
    }


    private void fetchDataFromFirestore() {
        CollectionReference contactsRef =
                firestore.collection("collage_name")
                        .document(selectedCollege)
                        .collection("contact");

        contactsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        ContactItemModel contact = document.toObject(ContactItemModel.class);
                        if (contact != null) {
                            contactList.add(contact);
                        }
                    }
                    contactAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Error fetching contacts", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
