package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SlotManagerAdapter extends RecyclerView.Adapter<SlotManagerAdapter.SlotViewHolder> {

    private Context context;
    private List<String> slots;
    private OnSlotRemoveListener listener;

    public interface OnSlotRemoveListener {
        void onSlotRemove(int position);
    }

    public SlotManagerAdapter(Context context, List<String> slots, OnSlotRemoveListener listener) {
        this.context = context;
        this.slots = slots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_slot_chip, parent, false);
        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {
        String slot = slots.get(position);
        holder.tvSlot.setText(slot);

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // In real implementation, this would also delete from artist_availabilities table
                // Example: DELETE FROM artist_availabilities WHERE artist_id = ? AND date = ? AND start_time = ? AND end_time = ?
                listener.onSlotRemove(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    public static class SlotViewHolder extends RecyclerView.ViewHolder {
        TextView tvSlot, btnRemove;

        public SlotViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSlot = itemView.findViewById(R.id.tvSlot);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
