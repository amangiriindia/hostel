package com.amzsoft.hostel.Adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.amzsoft.hostel.R;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AdminSliderAdapter extends RecyclerView.Adapter<AdminSliderAdapter.ViewHolder> {

    private List<String> imageUrlList; // Adjust the data type based on your needs
    private Context context;
    private String selectedCollege = "Ambalika Institute of Management and Technology";



    public AdminSliderAdapter(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.admin_slider_img_items, parent, false);
        return new ViewHolder(view);
    }

    @NonNull


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String imageUrl = imageUrlList.get(position);

        // Use Glide to load the image into ImageView
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher) // Placeholder image while loading
                .error(R.mipmap.ic_launcher) // Error image if loading fails
                .into(holder.imageView);

        // Set click listener for the delete button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click
                // You can add your logic to delete the corresponding item from the list or Firebase

                // Delete the item from the database
                showDeleteConfirmationDialog(imageUrl);
            }
        });
    }
    @Override
    public int getItemCount() {
        return imageUrlList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.cardImageView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }



    private void deleteItemFromDatabase(String imageUrl) {
        // Use Firebase Firestore to delete the corresponding document
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Assuming your Firestore structure is like: collection("collage_name").document(selectedCollege).collection("slider")
        firestore.collection("collage_name")
                .document(selectedCollege)
                .collection("slider")
                .whereEqualTo("img_url", imageUrl)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Get the filename from the document to construct the storage path
                            String fileName = document.getString("img_url");

                            // Log to check if the document is found
                            Log.d("Delete", "Document found for deletion: " + document.getId());

                            // Delete the document from Firestore
                            document.getReference().delete();

                            // Log to check if the document is deleted
                            Log.d("Delete", "Document deleted from Firestore: " + document.getId());

                            // Delete the corresponding image from Firebase Storage
                            deleteImageFromStorage(fileName);
                        }
                    } else {
                        // Handle failures
                        Log.e("Delete", "Error getting documents: ", task.getException());
                    }
                });
    }

    // Add this method to show the confirmation dialog
    private void showDeleteConfirmationDialog(String imageUrl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this slider notification?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User confirmed, proceed with deletion
                deleteItemFromDatabase(imageUrl);
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

    private void deleteImageFromStorage(String fileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Construct the StorageReference for the image
        StorageReference imageRef = storageRef.child(fileName);

        // Delete the image from Firebase Storage
        imageRef.delete().addOnSuccessListener(aVoid -> {
            // Image deleted successfully
            // You can add any additional logic here if needed
        }).addOnFailureListener(exception -> {
            // Handle failures
        });
    }


}

