package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class SlotAdapter extends BaseAdapter {

    private Context context;
    private List<String> slots;

    public SlotAdapter(Context context, List<String> slots) {
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
        TextView tvSlot;
        if (convertView == null) {
            tvSlot = new TextView(context);
            tvSlot.setLayoutParams(new android.widget.AbsListView.LayoutParams(
                android.widget.AbsListView.LayoutParams.MATCH_PARENT,
                120)); // Height in pixels
            tvSlot.setGravity(android.view.Gravity.CENTER);
            tvSlot.setPadding(16, 16, 16, 16);
            tvSlot.setTextSize(14);
            tvSlot.setTextColor(context.getColor(android.R.color.white));
            tvSlot.setBackgroundColor(context.getColor(R.color.primary));
        } else {
            tvSlot = (TextView) convertView;
        }

        String slot = slots.get(position);
        tvSlot.setText(slot);

        return tvSlot;
    }
}
