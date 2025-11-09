package com.example.testapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    private AvailabilityManager availabilityManager;

    private RecyclerView rvDates, rvSlots;
    private MaterialButton btnAddSlot, btnCancelAdd, btnConfirmAdd, btnViewArtist;
    private TextInputEditText etStartTime, etEndTime;
    private View addSlotContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_availability_manager);

        // Initialize views
        initializeViews();

        // Initialize availability manager
        availabilityManager = new AvailabilityManager(this, rvDates, rvSlots, etStartTime, etEndTime);

        // Setup button listeners
        setupButtonListeners();

        // Hide add slot form initially
        hideAddSlotForm();
    }

    private void initializeViews() {
        rvDates = findViewById(R.id.rvDates);
        rvSlots = findViewById(R.id.rvSlots);
        btnAddSlot = findViewById(R.id.btnAddSlot);
        btnCancelAdd = findViewById(R.id.btnCancelAdd);
        btnConfirmAdd = findViewById(R.id.btnConfirmAdd);
        btnViewArtist = findViewById(R.id.btnViewArtist);
        etStartTime = findViewById(R.id.etStartTime);
        etEndTime = findViewById(R.id.etEndTime);
        addSlotContainer = findViewById(R.id.addSlotContainer);
    }

    private void setupButtonListeners() {
        btnAddSlot.setOnClickListener(v -> showAddSlotForm());
        btnCancelAdd.setOnClickListener(v -> hideAddSlotForm());
        btnConfirmAdd.setOnClickListener(v -> addSlot());
        btnViewArtist.setOnClickListener(v -> navigateToArtistDetail());
    }

    private void navigateToArtistDetail() {
        Intent intent = new Intent(this, ArtistDetailActivity.class);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        long artistId = dbHelper.getFirstArtistId();
        if (artistId != -1) {
            intent.putExtra("artist_id", artistId);
        }
        startActivity(intent);
    }

    private void showAddSlotForm() {
        addSlotContainer.setVisibility(View.VISIBLE);
        availabilityManager.clearInputFields();
    }

    private void hideAddSlotForm() {
        addSlotContainer.setVisibility(View.GONE);
    }

    private void addSlot() {
        if (availabilityManager.addSlot()) {
            hideAddSlotForm();
        }
    }
}
