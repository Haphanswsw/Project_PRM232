package com.example.testapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import java.util.List;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.SlotViewHolder> {
    private List<String> slots;
    private OnSlotClickListener listener;

    public interface OnSlotClickListener {
        void onSlotClick(String slot);
    }

    public SlotAdapter(List<String> slots, OnSlotClickListener listener) {
        this.slots = slots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slot_chip, parent, false);
        return new SlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlotViewHolder holder, int position) {
        String slot = slots.get(position);
        holder.chip.setText(slot);
        holder.chip.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSlotClick(slot);
            }
        });
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }

    public void updateSlots(List<String> newSlots) {
        this.slots = newSlots;
        notifyDataSetChanged();
    }

    static class SlotViewHolder extends RecyclerView.ViewHolder {
        Chip chip;

        SlotViewHolder(View itemView) {
            super(itemView);
            chip = (Chip) itemView;
        }
    }
}
