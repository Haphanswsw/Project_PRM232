package com.example.myapplication.ui.bookings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.model.Booking;

import java.util.List;

public class PendingBookingsAdapter extends RecyclerView.Adapter<PendingBookingsAdapter.BookingViewHolder> {

    private List<Booking> bookings;
    private OnItemActionListener listener; // Interface for handling actions

    public interface OnItemActionListener {
        void onAcceptClick(int position);
        void onRejectClick(int position);
    }

    public PendingBookingsAdapter(List<Booking> bookings, OnItemActionListener listener) {
        this.bookings = bookings;
        this.listener = listener;
    }

    public void updateData(List<Booking> newBookings) {
        this.bookings = newBookings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_pending, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.eventTitle.setText(booking.getEventTitle());
        holder.eventLocation.setText(booking.getEventLocation());
        holder.startTime.setText(booking.getStartTime() != null ? booking.getStartTime().toString() : "N/A"); // Format as needed
        holder.endTime.setText(booking.getEndTime() != null ? booking.getEndTime().toString() : "N/A");     // Format as needed

        holder.btnAccept.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAcceptClick(position);
            }
        });

        holder.btnReject.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRejectClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    // Public getter to safely access Booking objects by position
    public Booking getBookingAtPosition(int position) {
        if (position >= 0 && position < bookings.size()) {
            return bookings.get(position);
        }
        return null;
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle;
        TextView eventLocation;
        TextView startTime;
        TextView endTime;
        Button btnAccept;
        Button btnReject;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.tvEventTitle);
            eventLocation = itemView.findViewById(R.id.tvEventLocation);
            startTime = itemView.findViewById(R.id.tvStartTime);
            endTime = itemView.findViewById(R.id.tvEndTime);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
