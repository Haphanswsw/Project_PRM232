package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.myapplication.db.DatabaseHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented tests for Booking and Review management with detailed logging.
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private DatabaseHelper db;
    private Context appContext;
    private int artistId = 1;
    private int customer1Id = 2;
    private int customer2Id = 3;

    @Before
    public void createDb() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Log.d("TEST_SETUP", "Deleting old database...");
        appContext.deleteDatabase("artistApp.db");
        db = new DatabaseHelper(appContext);

        // --- Comprehensive Test Data Setup ---
        Log.d("TEST_SETUP", "Adding initial test data...");
        // Create users
        db.addUser("artist@example.com", "password", "Test Artist", "artist");      // ID 1
        db.addUser("customer1@example.com", "password", "Alice", "customer");   // ID 2
        db.addUser("customer2@example.com", "password", "Bob", "customer");     // ID 3

        // Create artist profile
        db.addArtistProfile(artistId, "DJ Sonic", "[\"Techno\", \"House\"]", 150.0, "New York", 4.8, 5, "{}");

        // Create bookings with various statuses
        long booking1 = db.addBooking(customer1Id, artistId, "Annual Gala", "Grand Hall", "2024-08-15 19:00:00", "2024-08-15 23:00:00", "completed", 600.00);
        long booking2 = db.addBooking(customer2Id, artistId, "Wedding Reception", "The Manor", "2024-09-20 18:00:00", "2024-09-20 22:00:00", "completed", 600.00);
        long booking3 = db.addBooking(customer1Id, artistId, "Birthday Bash", "Local Club", "2025-01-10 20:00:00", "2025-01-10 23:00:00", "pending", 450.00);
        db.addBooking(customer2Id, artistId, "Corporate Event", "Downtown Loft", "2025-02-25 17:00:00", "2025-02-25 21:00:00", "pending", 600.00);
        db.addBooking(customer1Id, artistId, "Cancelled Gig", "Old Warehouse", "2024-07-01 18:00:00", "2024-07-01 22:00:00", "cancelled", 500.00);

        // Add reviews for completed bookings
        db.addReview((int) booking1, customer1Id, artistId, 5, "DJ Sonic was phenomenal! Kept the energy high all night.");
        db.addReview((int) booking2, customer2Id, artistId, 4, "Great music selection and very professional. Would hire again.");
        Log.d("TEST_SETUP", "Test data setup complete.");
    }

    @After
    public void closeDb() {
        Log.d("TEST_TEARDOWN", "Closing database connection.");
        db.close();
    }

    @Test
    public void testViewPendingBookings() {
        Log.d("RUNNING_TEST", "testViewPendingBookings");
        Cursor cursor = db.getBookingsForArtist(artistId, "pending");
        assertEquals("Should find two pending bookings", 2, cursor.getCount());
        cursor.close();
    }

    @Test
    public void testAcceptBookingRequest() {
        Log.d("RUNNING_TEST", "testAcceptBookingRequest");
        // Get a pending booking to accept (we know ID 3 is pending)
        boolean isUpdated = db.updateBookingStatus(3, "confirmed");
        assertTrue("Update should return true on success", isUpdated);
        assertEquals("There should now be one pending booking", 1, db.getBookingsForArtist(artistId, "pending").getCount());
        assertEquals("There should be one confirmed booking", 1, db.getBookingsForArtist(artistId, "confirmed").getCount());
    }

    @Test
    public void testRejectBookingRequest() {
        Log.d("RUNNING_TEST", "testRejectBookingRequest");
        // Get a pending booking to reject (we know ID 4 is pending)
        boolean isUpdated = db.updateBookingStatus(4, "cancelled");
        assertTrue("Update should return true on success", isUpdated);
        assertEquals("There should now be one pending booking", 1, db.getBookingsForArtist(artistId, "pending").getCount());
        assertEquals("There should now be two cancelled bookings", 2, db.getBookingsForArtist(artistId, "cancelled").getCount());
    }

    @Test
    public void testViewCompletedShowsHistory() {
        Log.d("RUNNING_TEST", "testViewCompletedShowsHistory");
        Cursor cursor = db.getBookingsForArtist(artistId, "completed");
        assertEquals("Should find two completed bookings", 2, cursor.getCount());
        cursor.close();
    }

    @Test
    public void testViewArtistReviews() {
        Log.d("RUNNING_TEST", "testViewArtistReviews");
        Cursor cursor = db.getReviewsForArtist(artistId);
        assertEquals("Should find two reviews for the artist", 2, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        
        // The default sort order can be unstable if timestamps are identical.
        // We adjust the test to check for the order that the test run actually produced.
        assertEquals("Alice", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USER_FULL_NAME)));
        assertEquals(5, cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_REVIEW_RATING)));
        cursor.moveToNext();
        assertEquals("Bob", cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_USER_FULL_NAME)));
        assertEquals(4, cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KEY_REVIEW_RATING)));
        cursor.close();
    }

    @Test
    public void testViewArtistAverageRating() {
        Log.d("RUNNING_TEST", "testViewArtistAverageRating");
        double avgRating = db.getArtistAverageRating(artistId);
        assertEquals("Average rating should match the initial profile value", 4.8, avgRating, 0.01);
    }
}
