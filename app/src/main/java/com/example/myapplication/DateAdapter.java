package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.List;
import java.util.Map;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {

    private Context context;
    private List<String> dates;
    private Map<String, List<String>> slotsByDate;
    private OnDateClickListener listener;
    private int selectedPosition = 0;

    public interface OnDateClickListener {
        void onDateClick(String date);
    }

    public DateAdapter(Context context, List<String> dates, Map<String, List<String>> slotsByDate, OnDateClickListener listener) {
        this.context = context;
        this.dates = dates;
        this.slotsByDate = slotsByDate;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_date_row, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        String date = dates.get(position);
        List<String> slots = slotsByDate.get(date);

        holder.tvDate.setText(date);
        holder.tvSlotCount.setText(slots.size() + " slot");

        // Highlight selected date
        holder.cardView.setCardBackgroundColor(
            position == selectedPosition ?
            context.getColor(R.color.primary) :
            context.getColor(R.color.background_card)
        );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(previousPosition);
                notifyItemChanged(selectedPosition);
                listener.onDateClick(date);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView tvDate, tvSlotCount;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            tvDate = itemView.findViewById(R.id.tvDate);
            tvSlotCount = itemView.findViewById(R.id.tvSlotCount);
        }
    }
}
