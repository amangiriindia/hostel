package com.amzsoft.hostel.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amzsoft.hostel.R;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder> {

    private List<String> imageUrlList; // List of image URLs
    private FirebaseFirestore firestore;
    private String selectedCollege;  // Variable to store the selected college

    public ImageSliderAdapter(String selectedCollege) {
        // Initialize Firebase Firestore reference
        firestore = FirebaseFirestore.getInstance();

        // Initialize imageUrlList as an empty ArrayList
        imageUrlList = new ArrayList<>();

        // Set the selected college
        this.selectedCollege = selectedCollege;

        // Fetch image URLs from Firebase Firestore based on the selected college
        fetchImageUrls();
    }

    // Method to update data based on the selected college
    public void updateData(String selectedCollege) {
        this.selectedCollege = selectedCollege;
        fetchImageUrls();
    }

    private void fetchImageUrls() {
        // Fetch image URLs from Firebase Firestore using the updated selected college
        firestore.collection("collage_name")
                .document(selectedCollege)
                .collection("slider")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Clear the existing list before adding new URLs
                        imageUrlList.clear();

                        // Loop through the query snapshot and add image URLs to the list
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String imageUrl = document.getString("img_url");
                            imageUrlList.add(imageUrl);
                        }

                        // Notify the adapter about the changes
                        notifyDataSetChanged();
                    } else {
                        // Handle the error case
                        Exception exception = task.getException();
                        if (exception != null) {
                            // Log or display the error
                            Log.e("FetchImageUrls", "Error fetching image URLs", exception);
                        }
                    }
                });
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_slider, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageUrlList.get(position);
        // Use Glide to load image from the URL into the ImageView
        Glide.with(holder.imageView.getContext()).load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}

