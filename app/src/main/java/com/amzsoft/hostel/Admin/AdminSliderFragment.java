package com.amzsoft.hostel.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amzsoft.hostel.Adapter.AdminSliderAdapter;
import com.amzsoft.hostel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminSliderFragment extends Fragment {
    private String selectedCollege ;
    private ImageView selectedImageView;
    private Uri selectedImageUri;
    private RecyclerView recyclerView;
    private AdminSliderAdapter adapter;
    private List<String> imageUrlList = new ArrayList<>(); //

    private final ActivityResultLauncher<String> getImageContent =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            selectedImageUri = uri;
                            selectedImageView.setImageURI(uri);
                            selectedImageView.setVisibility(View.VISIBLE);
                        }
                    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_slider, container, false);

        Button btnSelectImage = rootView.findViewById(R.id.btnSelectImage);
        selectedImageView = rootView.findViewById(R.id.selectedImageView);
        Button btnSubmit = rootView.findViewById(R.id.btnSubmit);
        recyclerView = rootView.findViewById(R.id.sliderImagerecyclerView);
        String college = getAdminSelectedCollege(getContext());
        if (!TextUtils.isEmpty(college)) {
            // Do something with the selected college name
            selectedCollege = college;
        } else {
            // Handle the case where the selected college name is not found
            // You might want to show a default college or take some appropriate action
            Toast.makeText(getActivity(), "Selected college not found", Toast.LENGTH_SHORT).show();
        }
        btnSelectImage.setOnClickListener(v -> getImageContent.launch("image/*"));

        btnSubmit.setOnClickListener(v -> {
            if (selectedImageUri != null) {
                // Upload image to Firebase Storage
                uploadImageToStorage(selectedImageUri);
            }
        });


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdminSliderAdapter(imageUrlList);
        recyclerView.setAdapter(adapter);

        // Fetch data from Firestore
        fetchDataFromFirestore();

        return rootView;
    }



    private void fetchDataFromFirestore() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("collage_name")
                .document(selectedCollege)
                .collection("slider")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Clear the existing list before adding new URLs
                            imageUrlList.clear();

                            // Loop through the query snapshot and add image URLs to the list
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String imageUrl = document.getString("img_url");
                                if (imageUrl != null) {
                                    imageUrlList.add(imageUrl);
                                }
                            }

                            // Notify the adapter that the data set has changed
                            adapter.notifyDataSetChanged();
                        } else {
                            // Handle task failure
                        }
                    }
                });
    }

    private void uploadImageToStorage(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a unique filename for the image
        String fileName = "slider/" + selectedCollege + "/" + System.currentTimeMillis();

        StorageReference imageRef = storageRef.child(fileName);

        // Upload the file to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    // Get the URL of the uploaded image
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // You can use the image URL (uri) as needed
                        String imageUrl = uri.toString();

                        // Store image URL in Firestore
                        storeImageUrlInFirestore(imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle unsuccessful uploads
                    // You might want to show an error message to the user
                });
    }

    private String getAdminSelectedCollege(Context context) {
        SharedPreferences adminPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE);
        return adminPreferences.getString("selectedCollege", "");
    }

    private void storeImageUrlInFirestore(String imageUrl) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Reference to the Firestore collection and document
        DocumentReference documentReference = firestore.collection("collage_name")
                .document(selectedCollege)
                .collection("slider")
                .document();

        // Create a map to store the data
        Map<String, Object> data = new HashMap<>();
        data.put("img_url", imageUrl);

        // Store the map in Firestore
        documentReference.set(data)
                .addOnSuccessListener(aVoid -> {
                    // Image URL stored successfully in Firestore
                    // You can add any additional logic here if needed
                    Toast.makeText(getContext(), "Slider Notification uploaded", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failures
                    Toast.makeText(getContext(), "Failed To add ", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), ""+imageUrl, Toast.LENGTH_SHORT).show();
                    // You might want to show an error message to the user
                });
    }



}
