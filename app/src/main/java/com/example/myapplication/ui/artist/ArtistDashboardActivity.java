package com.example.myapplication.ui.artist;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.bookings.ArtistBookingsActivity;
import com.google.android.material.card.MaterialCardView;

public class ArtistDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_dashboard);

        MaterialCardView cardManageBookings = findViewById(R.id.cardManageBookings);
        MaterialCardView cardArtistProfile = findViewById(R.id.cardArtistProfile);

        // Navigate to Booking Management
        cardManageBookings.setOnClickListener(v -> {
            Intent intent = new Intent(ArtistDashboardActivity.this, ArtistBookingsActivity.class);
            startActivity(intent);
        });

        // Navigate to Profile Editing
        cardArtistProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ArtistDashboardActivity.this, ArtistDetailActivity.class);
            startActivity(intent);
        });
    }
}
