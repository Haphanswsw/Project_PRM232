package com.example.myapplication.ui.bookings;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class BookingsPagerAdapter extends FragmentStateAdapter {

    public BookingsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PendingBookingsFragment();
            case 1:
                return new CompletedBookingsFragment();
            default:
                return new Fragment(); // Should not happen
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs: Pending and Completed
    }
}
