package com.example.myapplication.ui.bookings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.model.Booking;

import java.util.List;

public class CompletedBookingsAdapter extends RecyclerView.Adapter<CompletedBookingsAdapter.BookingViewHolder> {

    private List<Booking> bookings;

    public CompletedBookingsAdapter(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public void updateData(List<Booking> newBookings) {
        this.bookings = newBookings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking_completed, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        holder.eventTitle.setText(booking.getEventTitle());
        holder.eventLocation.setText(booking.getEventLocation());
        holder.startTime.setText(booking.getStartTime().toString()); // Format as needed
        holder.endTime.setText(booking.getEndTime().toString());   // Format as needed
        holder.price.setText(String.format("Price: %s", booking.getPrice().toPlainString()));
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle;
        TextView eventLocation;
        TextView startTime;
        TextView endTime;
        TextView price;

        BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.tvEventTitle);
            eventLocation = itemView.findViewById(R.id.tvEventLocation);
            startTime = itemView.findViewById(R.id.tvStartTime);
            endTime = itemView.findViewById(R.id.tvEndTime);
            price = itemView.findViewById(R.id.tvPrice);
        }
    }
}
