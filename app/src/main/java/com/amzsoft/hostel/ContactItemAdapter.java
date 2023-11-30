package com.amzsoft.hostel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ContactItemAdapter extends RecyclerView.Adapter<ContactItemAdapter.ViewHolder> {

    private List<ContactItemModel> contactList;
    private Context context;

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
                .into(holder.imageView);  // This is missing in your ViewHolder

        holder.nameTextView.setText(contact.getName());
        holder.phoneNumberTextView.setText("Phone: " + contact.getPhone_number());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView nameTextView;
        public TextView phoneNumberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_url);  // This is missing
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);
        }
    }
}
