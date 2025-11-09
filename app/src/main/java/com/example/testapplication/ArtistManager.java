package com.example.testapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class ArtistManager {
    private Context context;
    private DatabaseHelper dbHelper;
    private long artistId;

    // UI components
    private TextView tvName, tvGenres, tvStars, tvRating, tvPrice, tvBio;
    private GridView gridSlots;
    private LinearLayout reviewsContainer;

    public ArtistManager(Context context, long artistId, TextView tvName, TextView tvGenres,
                        TextView tvStars, TextView tvRating, TextView tvPrice, TextView tvBio,
                        GridView gridSlots, LinearLayout reviewsContainer) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
        this.artistId = artistId;
        this.tvName = tvName;
        this.tvGenres = tvGenres;
        this.tvStars = tvStars;
        this.tvRating = tvRating;
        this.tvPrice = tvPrice;
        this.tvBio = tvBio;
        this.gridSlots = gridSlots;
        this.reviewsContainer = reviewsContainer;
    }

    public void loadArtistData() {
        loadArtistProfile();
        loadArtistAvailabilities();
        loadArtistReviews();
    }

    private void loadArtistProfile() {
        String[] profile = dbHelper.getArtistProfile(artistId);
        if (profile != null && profile.length >= 6) {
            tvName.setText(profile[1] != null ? profile[1] : "Unknown Artist"); // stage_name
            tvGenres.setText(profile[2] != null ? profile[2] : "Various"); // genres

            // Generate star rating
            try {
                double rating = Double.parseDouble(profile[4] != null ? profile[4] : "0.0");
                StringBuilder stars = new StringBuilder();
                int fullStars = (int) rating;
                for (int i = 0; i < 5; i++) {
                    if (i < fullStars) {
                        stars.append("★");
                    } else {
                        stars.append("☆");
                    }
                }
                tvStars.setText(stars.toString());
                tvRating.setText(" " + String.format("%.1f", rating));
            } catch (NumberFormatException e) {
                tvStars.setText("☆☆☆☆☆");
                tvRating.setText(" 0.0");
            }

            // Format price
            try {
                double price = Double.parseDouble(profile[3] != null ? profile[3] : "0");
                tvPrice.setText(String.format("%.0f", price) + " đ/giờ");
            } catch (NumberFormatException e) {
                tvPrice.setText("0 đ/giờ");
            }

            tvBio.setText(profile[5] != null ? profile[5] : "No bio available"); // bio
        } else {
            // Set default values if profile not found
            tvName.setText("Artist Not Found");
            tvGenres.setText("Unknown");
            tvStars.setText("☆☆☆☆☆");
            tvRating.setText(" 0.0");
            tvPrice.setText("0 đ/giờ");
            tvBio.setText("No information available");
        }
    }

    private void loadArtistAvailabilities() {
        List<String> availabilities = dbHelper.getArtistAvailabilities(artistId);
        SlotGridAdapter adapter = new SlotGridAdapter(context, availabilities);
        gridSlots.setAdapter(adapter);
    }

    private void loadArtistReviews() {
        List<String> reviews = dbHelper.getArtistReviews(artistId);

        // Clear existing reviews
        reviewsContainer.removeAllViews();

        // Add reviews manually without adapter
        for (String reviewData : reviews) {
            View reviewView = createReviewView(reviewData);
            if (reviewView != null) {
                reviewsContainer.addView(reviewView);
            }
        }
    }

    private View createReviewView(String reviewData) {
        try {
            String[] parts = reviewData.split("\\|");
            if (parts.length >= 4) {
                // Inflate the review item layout
                View view = LayoutInflater.from(context).inflate(R.layout.item_review, reviewsContainer, false);

                TextView tvReviewerName = view.findViewById(R.id.tvReviewerName);
                TextView tvReviewDate = view.findViewById(R.id.tvReviewDate);
                TextView tvStars = view.findViewById(R.id.tvStars);
                TextView tvRating = view.findViewById(R.id.tvRating);
                TextView tvComment = view.findViewById(R.id.tvComment);

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

                return view;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
        loadArtistData();
    }
}
