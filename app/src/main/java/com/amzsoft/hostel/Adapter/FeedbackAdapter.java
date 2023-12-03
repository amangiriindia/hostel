package com.amzsoft.hostel.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amzsoft.hostel.Model.FeedbackModel;
import com.amzsoft.hostel.R;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private List<FeedbackModel> feedbackList;

    public FeedbackAdapter(List<FeedbackModel> feedbackList) {
        this.feedbackList = feedbackList;
    }



    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_feedback_items, parent, false);
        return new FeedbackViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        FeedbackModel feedback = feedbackList.get(position);

        // Set data to CardView
        holder.problemTextView.setText("Problem: " + feedback.getHeading());
        holder.descriptionTextView.setText(feedback.getDescription());
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {

        TextView problemTextView;
        TextView descriptionTextView;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);

            problemTextView = itemView.findViewById(R.id.admin_feedback_problem);
            descriptionTextView = itemView.findViewById(R.id.admin_feedback_description);
        }
    }
}

