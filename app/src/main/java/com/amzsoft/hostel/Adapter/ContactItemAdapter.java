package com.amzsoft.hostel.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amzsoft.hostel.Model.ContactItemModel;
import com.amzsoft.hostel.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class ContactItemAdapter extends RecyclerView.Adapter<ContactItemAdapter.ViewHolder> {

    private List<ContactItemModel> contactList;
    private Context context;

    // Constants
    static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 123;

    // Variable to store the phone number temporarily
    private String phoneNumberToCall;

    public ContactItemAdapter(List<ContactItemModel> contactList, Context context) {
        this.contactList = contactList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ContactItemModel contact = contactList.get(position);

        // Load image using Glide
        Glide.with(context)
                .load(contact.getImg_url())
                .placeholder(R.mipmap.ic_launcher) // Placeholder image while loading
                .error(R.mipmap.ic_launcher) // Error image if loading fails
                .into(holder.imageView);

        holder.nameTextView.setText(contact.getName());
        holder.phoneNumberTextView.setText("Phone: " + contact.getPhone_number());

        // Set up click listener for the phone icon
        holder.phoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the phone number from the ContactItemModel
                String phoneNumber = contact.getPhone_number();

                // Check if the user has a dialer app installed
                if (isDialerAppInstalled(context)) {
                    // Open the dialer app with the phone number pre-filled
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    context.startActivity(intent);
                } else {
                    // Show a message indicating no dialer app is installed
                    Toast.makeText(context, "No dialer app found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView nameTextView;
        public TextView phoneNumberTextView;
        public ImageView phoneIcon;  // Add this line

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_url);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);
            phoneIcon = itemView.findViewById(R.id.phone_icon);  // Initialize phoneIcon
        }
    }


    private boolean isDialerAppInstalled(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo("com.android.dialer", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}
