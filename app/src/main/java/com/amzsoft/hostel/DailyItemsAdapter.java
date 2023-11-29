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


public  class DailyItemsAdapter extends RecyclerView.Adapter<DailyItemsAdapter.MyViewHolder> {

    private Context context;
    private List<DailyItemModel> itemModelList;

    public DailyItemsAdapter(Context context, List<DailyItemModel> itemModelList) {
        this.context = context;
        this.itemModelList = itemModelList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DailyItemModel item = itemModelList.get(position);
        holder.text.setText(item.getText());
        holder.heading.setText(item.getHeading());

        // Load image using Glide or any other image loading library
        Glide.with(context).load(item.getImg_url()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView heading;
        TextView text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            heading = itemView.findViewById(R.id.headingTextView);
            text = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}

