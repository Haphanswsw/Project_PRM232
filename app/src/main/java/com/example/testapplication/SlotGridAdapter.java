package com.example.testapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class SlotGridAdapter extends BaseAdapter {
    private Context context;
    private List<String> slots;

    public SlotGridAdapter(Context context, List<String> slots) {
        this.context = context;
        this.slots = slots;
    }

    @Override
    public int getCount() {
        return slots.size();
    }

    @Override
    public Object getItem(int position) {
        return slots.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_slot_grid, parent, false);
        }

        String slotData = slots.get(position);
        String[] parts = slotData.split("\\|");

        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvTime = convertView.findViewById(R.id.tvTime);
        TextView tvStatus = convertView.findViewById(R.id.tvStatus);

        if (parts.length >= 4) {
            tvDate.setText(parts[0]); // date
            tvTime.setText(parts[1] + " - " + parts[2]); // start - end time
            tvStatus.setText("Available");
            tvStatus.setTextColor(0xFF4CAF50); // green
        }

        return convertView;
    }

    public void updateSlots(List<String> newSlots) {
        this.slots = newSlots;
        notifyDataSetChanged();
    }
}
