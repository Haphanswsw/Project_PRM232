package com.example.myapplication.ui.reviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.model.Review;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ArtistReviewsAdapter extends RecyclerView.Adapter<ArtistReviewsAdapter.ReviewViewHolder> {

    private List<Review> reviews;

    public ArtistReviewsAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    public void updateData(List<Review> newReviews) {
        this.reviews = newReviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        
        // The user object is not joined in this query, so we use what we have.
        // In a real app, you'''d likely want to join the users table to get the customer'''s name.
        if (review.getCustomerName() != null && !review.getCustomerName().isEmpty()) {
            holder.customerName.setText(review.getCustomerName());
        } else {
            holder.customerName.setText("Customer ID: " + review.getCustomerId());
        }
        
        holder.rating.setRating(review.getRating());
        holder.comment.setText(review.getComment());
        
        // Format the date for display
        if (review.getCreatedAt() != null) {
            holder.createdAt.setText(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } else if (review.getCreatedAtString() != null) {
            String dateTime = review.getCreatedAtString();
            holder.createdAt.setText(dateTime.split(" ")[0]);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView customerName;
        RatingBar rating;
        TextView comment;
        TextView createdAt;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.tvCustomerName);
            rating = itemView.findViewById(R.id.rbReviewRating);
            comment = itemView.findViewById(R.id.tvReviewComment);
            createdAt = itemView.findViewById(R.id.tvReviewDate);
        }
    }
}
