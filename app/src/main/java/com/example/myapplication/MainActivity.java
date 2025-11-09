package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.db.DatabaseHelper;
import com.example.myapplication.ui.artist.ArtistDashboardActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);

        // Clear and populate database with dummy data for testing
        addDummyData();

        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        TextInputEditText etEmail = findViewById(R.id.etEmail);
        TextInputEditText etPassword = findViewById(R.id.etPassword);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString(); // This should be hashed and compared

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.checkUser(email, password)) { // Insecure check - for demo only
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                if (email.contains("artist")) {
                    Intent intent = new Intent(MainActivity.this, ArtistDashboardActivity.class);
                    startActivity(intent);
                }
                // TODO: Add navigation for 'customer' role

            } else {
                Toast.makeText(this, "Login Failed: Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addDummyData() {
        // This is not efficient and is for demo purposes only.
        // In a real app, this data would be pre-populated or come from a server.
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 1, 2); // Clear the database

        // Add Users (user IDs will be 1, 2, 3)
        dbHelper.addUser("artist1@example.com", "password123", "DJ Sparkle", "artist");
        dbHelper.addUser("customer1@example.com", "password456", "John Doe", "customer");
        dbHelper.addUser("customer2@example.com", "password789", "Jane Smith", "customer");

        // Add Artist Profile for User 1
        dbHelper.addArtistProfile(1, "DJ Sparkle", "[\"House\", \"Techno\"]", 150.00, "New York, NY", 4.7, 5, "{\"spotify\": \"...\"}");

        // Add Bookings
        long booking1 = dbHelper.addBooking(2, 1, "Corporate Event", "Midtown Hall", "2024-12-01 18:00:00", "2024-12-01 22:00:00", "completed", 600.00);
        long booking2 = dbHelper.addBooking(3, 1, "Wedding Reception", "Grand Ballroom", "2024-11-25 19:00:00", "2024-11-25 23:00:00", "completed", 600.00);
        dbHelper.addBooking(2, 1, "Birthday Party", "Local Bar", "2025-01-15 20:00:00", "2025-01-15 23:00:00", "pending", 450.00);

        // Add Reviews for completed bookings
        if (booking1 != -1) {
            dbHelper.addReview((int) booking1, 2, 1, 5, "Absolutely amazing! DJ Sparkle made our event unforgettable. The music was perfect and the energy was high all night.");
        }
        if (booking2 != -1) {
            dbHelper.addReview((int) booking2, 3, 1, 4, "Great selection of music and very professional. Arrived on time and was a pleasure to work with.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
