package com.example.testapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class ReviewAdapter extends BaseAdapter {
    private Context context;
    private List<String> reviews;

    public ReviewAdapter(Context context, List<String> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        }

        String reviewData = reviews.get(position);
        String[] parts = reviewData.split("\\|");

        TextView tvReviewerName = convertView.findViewById(R.id.tvReviewerName);
        TextView tvReviewDate = convertView.findViewById(R.id.tvReviewDate);
        TextView tvStars = convertView.findViewById(R.id.tvStars);
        TextView tvRating = convertView.findViewById(R.id.tvRating);
        TextView tvComment = convertView.findViewById(R.id.tvComment);

        if (parts.length >= 4) {
            tvReviewerName.setText(parts[0]); // reviewer name
            int rating = Integer.parseInt(parts[1]);
            tvRating.setText(" " + rating + ".0");
            tvComment.setText(parts[2]); // comment
            tvReviewDate.setText(parts[3]); // date

            // Generate star rating
            StringBuilder stars = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                if (i < rating) {
                    stars.append("★");
                } else {
                    stars.append("☆");
                }
            }
            tvStars.setText(stars.toString());
        }

        return convertView;
    }

    public void updateReviews(List<String> newReviews) {
        this.reviews = newReviews;
        notifyDataSetChanged();
    }
}
