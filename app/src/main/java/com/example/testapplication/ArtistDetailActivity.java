package com.example.testapplication;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ArtistDetailActivity extends AppCompatActivity {
    private ArtistManager artistManager;
    private long artistId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_artist_detail);

        // Get artist ID from intent or use first artist
        artistId = getIntent().getLongExtra("artist_id", -1);
        if (artistId == -1) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            artistId = dbHelper.getFirstArtistId();
        }

        // Initialize views
        TextView tvName = findViewById(R.id.tvName);
        TextView tvGenres = findViewById(R.id.tvGenres);
        TextView tvStars = findViewById(R.id.tvStars);
        TextView tvRating = findViewById(R.id.tvRating);
        TextView tvPrice = findViewById(R.id.tvPrice);
        TextView tvBio = findViewById(R.id.tvBio);
        GridView gridSlots = findViewById(R.id.gridSlots);
        LinearLayout reviewsContainer = findViewById(R.id.reviewsContainer);

        // Initialize artist manager
        artistManager = new ArtistManager(this, artistId, tvName, tvGenres, tvStars, tvRating,
                                        tvPrice, tvBio, gridSlots, reviewsContainer);

        // Setup back button
        TextView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Load artist data
        artistManager.loadArtistData();
    }
}
