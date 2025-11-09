# Add project specific ProGuard rules here.

# Keep the booking fragments from being removed by code shrinking.
-keep class com.example.myapplication.ui.bookings.CompletedBookingsFragment
-keep class com.example.myapplication.ui.bookings.PendingBookingsFragment

# Keep all public members of these classes
-keep public class * extends androidx.fragment.app.Fragment {
    public <init>();
}
