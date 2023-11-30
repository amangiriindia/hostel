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

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.ViewHolder> {

    private List<ServiceItemsModel> serviceItemList;
    private Context context;

    public ServiceItemAdapter(List<ServiceItemsModel> serviceItemList, Context context) {
        this.serviceItemList = serviceItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.near_service_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceItemsModel serviceItem = serviceItemList.get(position);

        // Load image using Glide
        Glide.with(context)
                .load(serviceItem.getImg_url())
                .placeholder(R.mipmap.ic_launcher) // Placeholder image while loading
                .error(R.mipmap.ic_launcher) // Error image if loading fails
                .into(holder.imageView);

        holder.headingTextView.setText(serviceItem.getHeading());
        holder.descriptionTextView.setText(serviceItem.getDescription());
        holder.distanceTextView.setText("Distance: " + serviceItem.getDistance());
//        holder.contactNumberTextView.setText("Contact Number: " + serviceItem.getContactNumber());
//        holder.otherOptionTextView.setText("Other Option: " + serviceItem.getOtherOption());
    }

    @Override
    public int getItemCount() {
        return serviceItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView headingTextView;
        public TextView descriptionTextView;
        public TextView distanceTextView;
        public TextView contactNumberTextView;
        public TextView otherOptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            headingTextView = itemView.findViewById(R.id.headingTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            distanceTextView = itemView.findViewById(R.id.distanceTextView);
            contactNumberTextView = itemView.findViewById(R.id.contactNumberTextView);
            otherOptionTextView = itemView.findViewById(R.id.otherOptionTextView);
        }
    }
}