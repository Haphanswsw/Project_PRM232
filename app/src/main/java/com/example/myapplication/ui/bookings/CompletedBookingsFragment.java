package com.example.myapplication.ui.bookings;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.model.Booking;
import com.example.myapplication.db.DatabaseHelper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CompletedBookingsFragment extends Fragment {

    private static final String TAG = "CompletedBookingsFragment";
    private RecyclerView recyclerView;
    private CompletedBookingsAdapter adapter;
    private DatabaseHelper dbHelper;
    private int artistId = 1; // TODO: Replace with actual artist ID from session/login

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_bookings, container, false);

        dbHelper = new DatabaseHelper(getContext());

        recyclerView = view.findViewById(R.id.rvCompletedBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CompletedBookingsAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        loadCompletedBookingsFromDb();

        return view;
    }

    private void loadCompletedBookingsFromDb() {
        List<Booking> bookings = new ArrayList<>();
        try (Cursor cursor = dbHelper.getBookingsForArtist(artistId, "completed")) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String updatedAtString = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_BOOKING_UPDATED_AT));
                    LocalDateTime updatedAt = updatedAtString != null ? Booking.parseDateTimeString(updatedAtString) : null;

                    bookings.add(new Booking(
                            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_BOOKING_CUSTOMER_ID)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_BOOKING_ARTIST_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_BOOKING_EVENT_TITLE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_BOOKING_EVENT_LOCATION)),
                            Booking.parseDateTimeString(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_BOOKING_START_TIME))),
                            Booking.parseDateTimeString(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_BOOKING_END_TIME))),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_BOOKING_STATUS)),
                            new BigDecimal(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_BOOKING_PRICE))),
                            Booking.parseDateTimeString(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_CREATED_AT))),
                            updatedAt
                    ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading bookings from database", e);
            Toast.makeText(getContext(), "Failed to load bookings", Toast.LENGTH_SHORT).show();
        }

        adapter.updateData(bookings);
        Log.d(TAG, "Loaded " + bookings.size() + " completed bookings from the database.");
    }
}