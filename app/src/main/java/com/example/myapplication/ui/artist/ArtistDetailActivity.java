package com.example.myapplication.ui.artist;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.db.DatabaseHelper;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ArtistDetailActivity extends AppCompatActivity {

    private static final String TAG = "ArtistDetailActivity";

    private TextInputEditText etArtistName;
    private TextInputEditText etArtistBio;
    private TextInputEditText etYearsOfExperience;
    private MaterialButton btnSaveChanges;

    private DatabaseHelper dbHelper;
    private int artistId = 1; // You'd get this from the intent or session

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        dbHelper = new DatabaseHelper(this);

        etArtistName = findViewById(R.id.etArtistName);
        etArtistBio = findViewById(R.id.etArtistBio);
        etYearsOfExperience = findViewById(R.id.etYearsOfExperience);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        loadArtistProfile();

        btnSaveChanges.setOnClickListener(v -> {
            saveArtistProfile();
        });
    }

    private void loadArtistProfile() {
        try (Cursor cursor = dbHelper.getArtistProfile(artistId)) {
            if (cursor != null && cursor.moveToFirst()) {
                String stageName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PROFILE_STAGE_NAME));
                String bio = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USER_BIO)); // Assuming bio is in users table or artist_profiles
                int experienceYears = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_PROFILE_EXPERIENCE_YEARS));

                etArtistName.setText(stageName);
                etArtistBio.setText(bio);
                etYearsOfExperience.setText(String.valueOf(experienceYears));
            } else {
                Log.d(TAG, "Artist profile not found for ID: " + artistId);
                Toast.makeText(this, "Artist profile not found.", Toast.LENGTH_SHORT).show();
                // Set placeholder or default values if not found
                etArtistName.setText("Unknown Artist");
                etArtistBio.setText("No bio available.");
                etYearsOfExperience.setText("0");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading artist profile from database", e);
            Toast.makeText(this, "Failed to load artist profile.", Toast.LENGTH_SHORT).show();
            // Set placeholder or default values on error
            etArtistName.setText("Error loading");
            etArtistBio.setText("Error loading bio.");
            etYearsOfExperience.setText("0");
        }
    }

    private void saveArtistProfile() {
        String name = etArtistName.getText().toString().trim();
        String bio = etArtistBio.getText().toString().trim();
        String yearsStr = etYearsOfExperience.getText().toString().trim();

        if (name.isEmpty() || bio.isEmpty() || yearsStr.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int years = Integer.parseInt(yearsStr);

        boolean success = dbHelper.updateArtistProfile(artistId, name, bio, years);
        if (success) {
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Go back to the dashboard
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
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
