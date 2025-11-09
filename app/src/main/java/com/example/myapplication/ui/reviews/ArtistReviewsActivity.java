package com.example.myapplication.ui.reviews;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.model.Review;
import com.example.myapplication.db.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class ArtistReviewsActivity extends AppCompatActivity {

    private static final String TAG = "ArtistReviewsActivity";
    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private TextView tvOverallRating;
    private RatingBar ratingBarOverall;
    private DatabaseHelper dbHelper;
    private int artistId = 1; // For testing, we'll use the artist with user_id = 1

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_reviews);

        dbHelper = new DatabaseHelper(this);

        tvOverallRating = findViewById(R.id.tvOverallRating);
        ratingBarOverall = findViewById(R.id.ratingBarOverall);
        recyclerView = findViewById(R.id.rvReviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadOverallRating();
        loadArtistReviews();
    }

    private void loadOverallRating() {
        double averageRating = dbHelper.getArtistAverageRating(artistId);
        tvOverallRating.setText(String.format("%.1f", averageRating));
        ratingBarOverall.setRating((float) averageRating);
    }

    private void loadArtistReviews() {
        List<Review> reviews = new ArrayList<>();
        try (Cursor cursor = dbHelper.getReviewsForArtist(artistId)) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    reviews.add(new Review(
                            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_REVIEW_RATING)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_REVIEW_COMMENT)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USER_FULL_NAME)), // Joined from users table
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CREATED_AT))
                    ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading reviews from database", e);
            Toast.makeText(this, "Failed to load reviews.", Toast.LENGTH_SHORT).show();
        }

        adapter = new ReviewAdapter(reviews);
        recyclerView.setAdapter(adapter);

        Log.d(TAG, "Loaded " + reviews.size() + " reviews from the database.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
