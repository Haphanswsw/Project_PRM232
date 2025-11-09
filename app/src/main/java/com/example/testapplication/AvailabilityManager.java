package com.example.testapplication;

import android.content.Context;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.List;

public class AvailabilityManager {
    private Context context;
    private DatabaseHelper dbHelper;
    private DateAdapter dateAdapter;
    private SlotAdapter slotAdapter;
    private String selectedDate = "2024-12-20";

    // UI components that need to be passed from activity
    private RecyclerView rvDates, rvSlots;
    private TextInputEditText etStartTime, etEndTime;

    public AvailabilityManager(Context context, RecyclerView rvDates, RecyclerView rvSlots,
                             TextInputEditText etStartTime, TextInputEditText etEndTime) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
        this.rvDates = rvDates;
        this.rvSlots = rvSlots;
        this.etStartTime = etStartTime;
        this.etEndTime = etEndTime;

        setupRecyclerViews();
        loadInitialData();
    }

    private void setupRecyclerViews() {
        // Dates RecyclerView
        rvDates.setLayoutManager(new LinearLayoutManager(context));
        dateAdapter = new DateAdapter(dbHelper.getAllDates(), date -> {
            loadSlotsForDate(date);
        });
        rvDates.setAdapter(dateAdapter);

        // Slots RecyclerView
        rvSlots.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        slotAdapter = new SlotAdapter(dbHelper.getSlotsForDate(selectedDate), slot -> {
            deleteSlot(slot);
        });
        rvSlots.setAdapter(slotAdapter);
    }

    private void loadInitialData() {
        loadDates();
        loadSlotsForDate(selectedDate);
    }

    public void loadDates() {
        List<String> dates = dbHelper.getAllDates();
        dateAdapter = new DateAdapter(dates, date -> {
            loadSlotsForDate(date);
        });
        rvDates.setAdapter(dateAdapter);
        dateAdapter.setSelectedDate(selectedDate);
    }

    public void loadSlotsForDate(String date) {
        selectedDate = date;
        List<String> slots = dbHelper.getSlotsForDate(date);
        slotAdapter.updateSlots(slots);
    }

    public boolean addSlot() {
        String startTime = etStartTime.getText().toString().trim();
        String endTime = etEndTime.getText().toString().trim();

        if (startTime.isEmpty() || endTime.isEmpty()) {
            Toast.makeText(context, "Please enter both start and end time", Toast.LENGTH_SHORT).show();
            return false;
        }

        long result = dbHelper.addSlot(selectedDate, startTime, endTime);
        if (result != -1) {
            Toast.makeText(context, "Slot added successfully", Toast.LENGTH_SHORT).show();
            loadSlotsForDate(selectedDate);
            return true;
        } else {
            Toast.makeText(context, "Failed to add slot", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void deleteSlot(String slot) {
        // Parse slot to get start and end time
        String[] parts = slot.split(" - ");
        if (parts.length == 2) {
            String startTime = parts[0];
            String endTime = parts[1];

            int deleted = dbHelper.deleteSlot(selectedDate, startTime, endTime);
            if (deleted > 0) {
                Toast.makeText(context, "Slot deleted", Toast.LENGTH_SHORT).show();
                loadSlotsForDate(selectedDate);
            } else {
                Toast.makeText(context, "Failed to delete slot", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String date) {
        this.selectedDate = date;
        loadSlotsForDate(date);
    }

    public void clearInputFields() {
        etStartTime.setText("");
        etEndTime.setText("");
    }
}
