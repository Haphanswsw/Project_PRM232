package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AvailabilityManagerActivity extends AppCompatActivity {

    private RecyclerView rvDates, rvSlots;
    private TextView tvSlotsTitle;
    private MaterialButton btnAddSlot, btnCancelAdd, btnConfirmAdd;
    private LinearLayout addSlotContainer;
    private TextInputEditText etStartTime, etEndTime;

    private DateAdapter dateAdapter;
    private SlotManagerAdapter slotManagerAdapter;
    private List<String> dates;
    private Map<String, List<String>> slotsByDate;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability_manager);

        initializeViews();
        setupData();
        setupRecyclerViews();
        setupListeners();
    }

    private void initializeViews() {
        rvDates = findViewById(R.id.rvDates);
        rvSlots = findViewById(R.id.rvSlots);
        tvSlotsTitle = findViewById(R.id.tvSlotsTitle);
        btnAddSlot = findViewById(R.id.btnAddSlot);
        btnCancelAdd = findViewById(R.id.btnCancelAdd);
        btnConfirmAdd = findViewById(R.id.btnConfirmAdd);
        addSlotContainer = findViewById(R.id.addSlotContainer);

        etStartTime = findViewById(R.id.etStartTime);
        etEndTime = findViewById(R.id.etEndTime);
    }

    private void setupData() {
        dates = new ArrayList<>();
        slotsByDate = new HashMap<>();

        // Sample dates for next 7 days - in real app, this would be dynamic
        for (int i = 0; i < 7; i++) {
            String date = "2024-12-" + (20 + i);
            dates.add(date);
            slotsByDate.put(date, new ArrayList<>());
        }

        // Load existing availability slots from database (artist_availabilities table)
        // In real implementation, this would query: SELECT * FROM artist_availabilities WHERE artist_id = ? AND date = ?
        loadExistingAvailabilities();

        selectedDate = dates.get(0);
        updateSlotsTitle();
    }

    /**
     * Load existing availability slots from artist_availabilities table
     * This would typically be called from a database helper or API
     */
    private void loadExistingAvailabilities() {
        // Sample data - in real app, this would come from database
        // Example query: SELECT start_time, end_time FROM artist_availabilities WHERE artist_id = currentArtistId AND date = selectedDate

        slotsByDate.get("2024-12-20").add("19:00 - 20:00");
        slotsByDate.get("2024-12-20").add("20:00 - 21:00");
        slotsByDate.get("2024-12-20").add("21:00 - 22:00");

        slotsByDate.get("2024-12-21").add("18:00 - 19:00");
        slotsByDate.get("2024-12-21").add("19:00 - 20:00");
    }

    private void setupRecyclerViews() {
        // Setup dates RecyclerView
        rvDates.setLayoutManager(new LinearLayoutManager(this));
        dateAdapter = new DateAdapter(this, dates, slotsByDate, new DateAdapter.OnDateClickListener() {
            @Override
            public void onDateClick(String date) {
                selectedDate = date;
                updateSlotsTitle();
                updateSlotsList();
            }
        });
        rvDates.setAdapter(dateAdapter);

        // Setup slots RecyclerView
        rvSlots.setLayoutManager(new LinearLayoutManager(this));
        updateSlotsList();
    }

    private void setupListeners() {
        btnAddSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSlotContainer.setVisibility(View.VISIBLE);
            }
        });

        btnCancelAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSlotContainer.setVisibility(View.GONE);
                clearAddSlotForm();
            }
        });

        btnConfirmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewSlot();
            }
        });
    }

    private void updateSlotsTitle() {
        tvSlotsTitle.setText("Slots - " + selectedDate);
    }

    private void updateSlotsList() {
        List<String> currentSlots = slotsByDate.get(selectedDate);
        slotManagerAdapter = new SlotManagerAdapter(this, currentSlots, new SlotManagerAdapter.OnSlotRemoveListener() {
            @Override
            public void onSlotRemove(int position) {
                currentSlots.remove(position);
                slotManagerAdapter.notifyDataSetChanged();
                dateAdapter.notifyDataSetChanged(); // Update slot count in dates list
            }
        });
        rvSlots.setAdapter(slotManagerAdapter);
    }

    private void addNewSlot() {
        String startTime = etStartTime.getText().toString().trim();
        String endTime = etEndTime.getText().toString().trim();

        if (!startTime.isEmpty() && !endTime.isEmpty()) {
            String newSlot = startTime + " - " + endTime;

            // In real implementation, this would insert into artist_availabilities table
            // Example: INSERT INTO artist_availabilities (artist_id, date, start_time, end_time) VALUES (?, ?, ?, ?)

            slotsByDate.get(selectedDate).add(newSlot);
            updateSlotsList();
            dateAdapter.notifyDataSetChanged(); // Update slot count in dates list

            addSlotContainer.setVisibility(View.GONE);
            clearAddSlotForm();
        }
    }

    private void clearAddSlotForm() {
        etStartTime.setText("");
        etEndTime.setText("");
    }
}
