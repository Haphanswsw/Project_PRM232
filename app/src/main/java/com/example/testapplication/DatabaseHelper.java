package com.example.testapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "funet.db";
    private static final int DATABASE_VERSION = 2;

    // Dates table
    public static final String TABLE_DATES = "dates";
    public static final String COLUMN_DATE_ID = "id";
    public static final String COLUMN_DATE = "date";

    // Slots table
    public static final String TABLE_SLOTS = "slots";
    public static final String COLUMN_SLOT_ID = "id";
    public static final String COLUMN_SLOT_DATE_ID = "date_id";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_TIME = "end_time";

    // Artist tables
    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "id";
    public static final String COLUMN_ARTIST_NAME = "name";
    public static final String COLUMN_ARTIST_STAGE_NAME = "stage_name";
    public static final String COLUMN_ARTIST_GENRES = "genres";
    public static final String COLUMN_ARTIST_PRICE = "price_per_hour";
    public static final String COLUMN_ARTIST_RATING = "rating";
    public static final String COLUMN_ARTIST_BIO = "bio";

    public static final String TABLE_ARTIST_AVAILABILITIES = "artist_availabilities";
    public static final String COLUMN_AVAILABILITY_ID = "id";
    public static final String COLUMN_AVAILABILITY_ARTIST_ID = "artist_id";
    public static final String COLUMN_AVAILABILITY_DATE = "date";
    public static final String COLUMN_AVAILABILITY_START_TIME = "start_time";
    public static final String COLUMN_AVAILABILITY_END_TIME = "end_time";
    public static final String COLUMN_AVAILABILITY_STATUS = "status";

    public static final String TABLE_REVIEWS = "reviews";
    public static final String COLUMN_REVIEW_ID = "id";
    public static final String COLUMN_REVIEW_ARTIST_ID = "artist_id";
    public static final String COLUMN_REVIEWER_NAME = "reviewer_name";
    public static final String COLUMN_REVIEW_RATING = "rating";
    public static final String COLUMN_REVIEW_COMMENT = "comment";
    public static final String COLUMN_REVIEW_DATE = "review_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create dates table
        String createDatesTable = "CREATE TABLE " + TABLE_DATES + " (" +
                COLUMN_DATE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT UNIQUE NOT NULL)";
        db.execSQL(createDatesTable);

        // Create slots table
        String createSlotsTable = "CREATE TABLE " + TABLE_SLOTS + " (" +
                COLUMN_SLOT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SLOT_DATE_ID + " INTEGER NOT NULL, " +
                COLUMN_START_TIME + " TEXT NOT NULL, " +
                COLUMN_END_TIME + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_SLOT_DATE_ID + ") REFERENCES " + TABLE_DATES + "(" + COLUMN_DATE_ID + "))";
        db.execSQL(createSlotsTable);

        // Create artists table
        String createArtistsTable = "CREATE TABLE " + TABLE_ARTISTS + " (" +
                COLUMN_ARTIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ARTIST_NAME + " TEXT NOT NULL, " +
                COLUMN_ARTIST_STAGE_NAME + " TEXT NOT NULL, " +
                COLUMN_ARTIST_GENRES + " TEXT, " +
                COLUMN_ARTIST_PRICE + " REAL NOT NULL, " +
                COLUMN_ARTIST_RATING + " REAL DEFAULT 0.0, " +
                COLUMN_ARTIST_BIO + " TEXT)";
        db.execSQL(createArtistsTable);

        // Create artist availabilities table
        String createAvailabilitiesTable = "CREATE TABLE " + TABLE_ARTIST_AVAILABILITIES + " (" +
                COLUMN_AVAILABILITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_AVAILABILITY_ARTIST_ID + " INTEGER NOT NULL, " +
                COLUMN_AVAILABILITY_DATE + " TEXT NOT NULL, " +
                COLUMN_AVAILABILITY_START_TIME + " TEXT NOT NULL, " +
                COLUMN_AVAILABILITY_END_TIME + " TEXT NOT NULL, " +
                COLUMN_AVAILABILITY_STATUS + " TEXT DEFAULT 'available', " +
                "FOREIGN KEY(" + COLUMN_AVAILABILITY_ARTIST_ID + ") REFERENCES " + TABLE_ARTISTS + "(" + COLUMN_ARTIST_ID + "))";
        db.execSQL(createAvailabilitiesTable);

        // Create reviews table
        String createReviewsTable = "CREATE TABLE " + TABLE_REVIEWS + " (" +
                COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_REVIEW_ARTIST_ID + " INTEGER NOT NULL, " +
                COLUMN_REVIEWER_NAME + " TEXT NOT NULL, " +
                COLUMN_REVIEW_RATING + " INTEGER NOT NULL, " +
                COLUMN_REVIEW_COMMENT + " TEXT, " +
                COLUMN_REVIEW_DATE + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_REVIEW_ARTIST_ID + ") REFERENCES " + TABLE_ARTISTS + "(" + COLUMN_ARTIST_ID + "))";
        db.execSQL(createReviewsTable);

        // Insert sample data
        insertSampleDates(db);
        insertSampleArtists(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Create new artist tables
            String createArtistsTable = "CREATE TABLE " + TABLE_ARTISTS + " (" +
                    COLUMN_ARTIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ARTIST_NAME + " TEXT NOT NULL, " +
                    COLUMN_ARTIST_STAGE_NAME + " TEXT NOT NULL, " +
                    COLUMN_ARTIST_GENRES + " TEXT, " +
                    COLUMN_ARTIST_PRICE + " REAL NOT NULL, " +
                    COLUMN_ARTIST_RATING + " REAL DEFAULT 0.0, " +
                    COLUMN_ARTIST_BIO + " TEXT)";
            db.execSQL(createArtistsTable);

            String createAvailabilitiesTable = "CREATE TABLE " + TABLE_ARTIST_AVAILABILITIES + " (" +
                    COLUMN_AVAILABILITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_AVAILABILITY_ARTIST_ID + " INTEGER NOT NULL, " +
                    COLUMN_AVAILABILITY_DATE + " TEXT NOT NULL, " +
                    COLUMN_AVAILABILITY_START_TIME + " TEXT NOT NULL, " +
                    COLUMN_AVAILABILITY_END_TIME + " TEXT NOT NULL, " +
                    COLUMN_AVAILABILITY_STATUS + " TEXT DEFAULT 'available', " +
                    "FOREIGN KEY(" + COLUMN_AVAILABILITY_ARTIST_ID + ") REFERENCES " + TABLE_ARTISTS + "(" + COLUMN_ARTIST_ID + "))";
            db.execSQL(createAvailabilitiesTable);

            String createReviewsTable = "CREATE TABLE " + TABLE_REVIEWS + " (" +
                    COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_REVIEW_ARTIST_ID + " INTEGER NOT NULL, " +
                    COLUMN_REVIEWER_NAME + " TEXT NOT NULL, " +
                    COLUMN_REVIEW_RATING + " INTEGER NOT NULL, " +
                    COLUMN_REVIEW_COMMENT + " TEXT, " +
                    COLUMN_REVIEW_DATE + " TEXT NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_REVIEW_ARTIST_ID + ") REFERENCES " + TABLE_ARTISTS + "(" + COLUMN_ARTIST_ID + "))";
            db.execSQL(createReviewsTable);

            insertSampleArtists(db);
        }
    }

    private void insertSampleDates(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        String[] dates = {"2024-12-20", "2024-12-21", "2024-12-22", "2024-12-23", "2024-12-24"};
        for (String date : dates) {
            values.put(COLUMN_DATE, date);
            db.insert(TABLE_DATES, null, values);
        }
    }

    private void insertSampleArtists(SQLiteDatabase db) {
        // Insert sample artist
        ContentValues artistValues = new ContentValues();
        artistValues.put(COLUMN_ARTIST_NAME, "Luna Nguyễn");
        artistValues.put(COLUMN_ARTIST_STAGE_NAME, "Luna");
        artistValues.put(COLUMN_ARTIST_GENRES, "Pop • Ballad");
        artistValues.put(COLUMN_ARTIST_PRICE, 2000000.0);
        artistValues.put(COLUMN_ARTIST_RATING, 4.8);
        artistValues.put(COLUMN_ARTIST_BIO, "Nghệ sĩ pop nổi tiếng với giọng hát ngọt ngào");
        long artistId = db.insert(TABLE_ARTISTS, null, artistValues);

        // Insert sample availabilities
        ContentValues availabilityValues = new ContentValues();
        availabilityValues.put(COLUMN_AVAILABILITY_ARTIST_ID, artistId);
        availabilityValues.put(COLUMN_AVAILABILITY_DATE, "2024-12-20");
        availabilityValues.put(COLUMN_AVAILABILITY_START_TIME, "14:00");
        availabilityValues.put(COLUMN_AVAILABILITY_END_TIME, "16:00");
        availabilityValues.put(COLUMN_AVAILABILITY_STATUS, "available");
        db.insert(TABLE_ARTIST_AVAILABILITIES, null, availabilityValues);

        availabilityValues.put(COLUMN_AVAILABILITY_DATE, "2024-12-21");
        availabilityValues.put(COLUMN_AVAILABILITY_START_TIME, "19:00");
        availabilityValues.put(COLUMN_AVAILABILITY_END_TIME, "21:00");
        db.insert(TABLE_ARTIST_AVAILABILITIES, null, availabilityValues);

        // Insert sample review
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(COLUMN_REVIEW_ARTIST_ID, artistId);
        reviewValues.put(COLUMN_REVIEWER_NAME, "Nguyễn Văn A");
        reviewValues.put(COLUMN_REVIEW_RATING, 5);
        reviewValues.put(COLUMN_REVIEW_COMMENT, "Buổi biểu diễn rất tuyệt vời! Luna hát rất hay và chuyên nghiệp.");
        reviewValues.put(COLUMN_REVIEW_DATE, "2024-12-15");
        db.insert(TABLE_REVIEWS, null, reviewValues);
    }

    // Date operations
    public List<String> getAllDates() {
        List<String> dates = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DATES, new String[]{COLUMN_DATE}, null, null, null, null, COLUMN_DATE);
        if (cursor.moveToFirst()) {
            do {
                dates.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return dates;
    }

    public long getDateId(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DATES, new String[]{COLUMN_DATE_ID}, COLUMN_DATE + "=?", new String[]{date}, null, null, null);
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }

    // Slot operations
    public List<String> getSlotsForDate(String date) {
        List<String> slots = new ArrayList<>();
        long dateId = getDateId(date);
        if (dateId == -1) return slots;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SLOTS, new String[]{COLUMN_START_TIME, COLUMN_END_TIME},
                COLUMN_SLOT_DATE_ID + "=?", new String[]{String.valueOf(dateId)}, null, null, COLUMN_START_TIME);
        if (cursor.moveToFirst()) {
            do {
                String slot = cursor.getString(0) + " - " + cursor.getString(1);
                slots.add(slot);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return slots;
    }

    public long addSlot(String date, String startTime, String endTime) {
        long dateId = getDateId(date);
        if (dateId == -1) return -1;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SLOT_DATE_ID, dateId);
        values.put(COLUMN_START_TIME, startTime);
        values.put(COLUMN_END_TIME, endTime);
        return db.insert(TABLE_SLOTS, null, values);
    }

    public int deleteSlot(String date, String startTime, String endTime) {
        long dateId = getDateId(date);
        if (dateId == -1) return 0;

        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_SLOTS, COLUMN_SLOT_DATE_ID + "=? AND " + COLUMN_START_TIME + "=? AND " + COLUMN_END_TIME + "=?",
                new String[]{String.valueOf(dateId), startTime, endTime});
    }

    // Artist operations
    public String[] getArtistProfile(long artistId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ARTISTS,
                new String[]{COLUMN_ARTIST_NAME, COLUMN_ARTIST_STAGE_NAME, COLUMN_ARTIST_GENRES,
                        COLUMN_ARTIST_PRICE, COLUMN_ARTIST_RATING, COLUMN_ARTIST_BIO},
                COLUMN_ARTIST_ID + "=?", new String[]{String.valueOf(artistId)}, null, null, null);

        if (cursor.moveToFirst()) {
            String[] profile = new String[6];
            profile[0] = cursor.getString(0); // name
            profile[1] = cursor.getString(1); // stage_name
            profile[2] = cursor.getString(2); // genres
            profile[3] = cursor.getString(3); // price
            profile[4] = cursor.getString(4); // rating
            profile[5] = cursor.getString(5); // bio
            cursor.close();
            return profile;
        }
        cursor.close();
        return null;
    }

    public List<String> getArtistAvailabilities(long artistId) {
        List<String> availabilities = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ARTIST_AVAILABILITIES,
                new String[]{COLUMN_AVAILABILITY_DATE, COLUMN_AVAILABILITY_START_TIME, COLUMN_AVAILABILITY_END_TIME, COLUMN_AVAILABILITY_STATUS},
                COLUMN_AVAILABILITY_ARTIST_ID + "=? AND " + COLUMN_AVAILABILITY_STATUS + "=?",
                new String[]{String.valueOf(artistId), "available"}, null, null, COLUMN_AVAILABILITY_DATE + ", " + COLUMN_AVAILABILITY_START_TIME);

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                String startTime = cursor.getString(1);
                String endTime = cursor.getString(2);
                String status = cursor.getString(3);
                availabilities.add(date + "|" + startTime + "|" + endTime + "|" + status);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return availabilities;
    }

    public List<String> getArtistReviews(long artistId) {
        List<String> reviews = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REVIEWS,
                new String[]{COLUMN_REVIEWER_NAME, COLUMN_REVIEW_RATING, COLUMN_REVIEW_COMMENT, COLUMN_REVIEW_DATE},
                COLUMN_REVIEW_ARTIST_ID + "=?", new String[]{String.valueOf(artistId)},
                null, null, COLUMN_REVIEW_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                String reviewerName = cursor.getString(0);
                int rating = cursor.getInt(1);
                String comment = cursor.getString(2);
                String date = cursor.getString(3);
                reviews.add(reviewerName + "|" + rating + "|" + comment + "|" + date);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return reviews;
    }

    public long getFirstArtistId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ARTISTS, new String[]{COLUMN_ARTIST_ID}, null, null, null, null, null, "1");
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }
}
