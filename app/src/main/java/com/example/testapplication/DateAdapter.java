package com.example.testapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {
    private List<String> dates;
    private int selectedPosition = 0;
    private OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClick(String date);
    }

    public DateAdapter(List<String> dates, OnDateClickListener listener) {
        this.dates = dates;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_row, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        String date = dates.get(position);
        holder.textView.setText(date);
        holder.textView.setBackgroundColor(position == selectedPosition ?
                android.graphics.Color.LTGRAY : android.graphics.Color.TRANSPARENT);

        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                int previousPosition = selectedPosition;
                selectedPosition = adapterPosition;
                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);
                if (listener != null) {
                    listener.onDateClick(dates.get(adapterPosition));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public void setSelectedDate(String date) {
        int position = dates.indexOf(date);
        if (position != -1) {
            int previousPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);
        }
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        DateViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}
