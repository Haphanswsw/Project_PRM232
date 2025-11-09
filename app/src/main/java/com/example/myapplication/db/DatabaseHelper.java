package com.example.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "artistApp.db";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ARTIST_PROFILES = "artist_profiles";
    public static final String TABLE_ARTIST_AVAILABILITIES = "artist_availabilities";
    public static final String TABLE_BOOKINGS = "bookings";
    public static final String TABLE_REVIEWS = "reviews";

    // Common Columns
    public static final String KEY_ID = "id";
    public static final String KEY_CREATED_AT = "created_at";

    // Users Table
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_PASSWORD_HASH = "password_hash";
    public static final String KEY_USER_FULL_NAME = "full_name";
    public static final String KEY_USER_PHONE = "phone";
    public static final String KEY_USER_ROLE = "role";
    public static final String KEY_USER_BIO = "bio";
    public static final String KEY_USER_AVATAR_URL = "avatar_url";
    public static final String KEY_USER_STATUS = "status";

    // Artist Profiles Table
    public static final String KEY_PROFILE_USER_ID = "user_id";
    public static final String KEY_PROFILE_STAGE_NAME = "stage_name";
    public static final String KEY_PROFILE_GENRES = "genres";
    public static final String KEY_PROFILE_PRICE_PER_HOUR = "price_per_hour";
    public static final String KEY_PROFILE_LOCATION = "location";
    public static final String KEY_PROFILE_RATING_AVG = "rating_avg";
    public static final String KEY_PROFILE_EXPERIENCE_YEARS = "experience_years";
    public static final String KEY_PROFILE_SOCIAL_LINKS = "social_links";

    // Bookings Table
    public static final String KEY_BOOKING_CUSTOMER_ID = "customer_id";
    public static final String KEY_BOOKING_ARTIST_ID = "artist_id";
    public static final String KEY_BOOKING_EVENT_TITLE = "event_title";
    public static final String KEY_BOOKING_EVENT_LOCATION = "event_location";
    public static final String KEY_BOOKING_START_TIME = "start_time";
    public static final String KEY_BOOKING_END_TIME = "end_time";
    public static final String KEY_BOOKING_STATUS = "status";
    public static final String KEY_BOOKING_PRICE = "price";
    public static final String KEY_BOOKING_UPDATED_AT = "updated_at";

    // Reviews Table
    public static final String KEY_REVIEW_BOOKING_ID = "booking_id";
    public static final String KEY_REVIEW_CUSTOMER_ID = "customer_id";
    public static final String KEY_REVIEW_ARTIST_ID = "artist_id";
    public static final String KEY_REVIEW_RATING = "rating";
    public static final String KEY_REVIEW_COMMENT = "comment";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating database tables...");
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_EMAIL + " TEXT UNIQUE NOT NULL," + KEY_USER_PASSWORD_HASH + " TEXT NOT NULL," + KEY_USER_FULL_NAME + " TEXT NOT NULL," + KEY_USER_PHONE + " TEXT UNIQUE," + KEY_USER_ROLE + " TEXT NOT NULL DEFAULT 'customer' CHECK(" + KEY_USER_ROLE + " IN ('artist', 'customer', 'admin'))," + KEY_USER_BIO + " TEXT," + KEY_USER_AVATAR_URL + " TEXT," + KEY_CREATED_AT + " TEXT DEFAULT CURRENT_TIMESTAMP," + KEY_USER_STATUS + " TEXT NOT NULL DEFAULT 'active' CHECK(" + KEY_USER_STATUS + " IN ('active', 'inactive', 'banned'))" + ");";
        String CREATE_ARTIST_PROFILES_TABLE = "CREATE TABLE " + TABLE_ARTIST_PROFILES + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_PROFILE_USER_ID + " INTEGER UNIQUE NOT NULL," + KEY_PROFILE_STAGE_NAME + " TEXT NOT NULL," + KEY_PROFILE_GENRES + " TEXT," + KEY_PROFILE_PRICE_PER_HOUR + " REAL NOT NULL CHECK(" + KEY_PROFILE_PRICE_PER_HOUR + " >= 0)," + KEY_PROFILE_LOCATION + " TEXT," + KEY_PROFILE_RATING_AVG + " REAL DEFAULT 0.0," + KEY_PROFILE_EXPERIENCE_YEARS + " INTEGER DEFAULT 0," + KEY_PROFILE_SOCIAL_LINKS + " TEXT," + "FOREIGN KEY(" + KEY_PROFILE_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + ") ON DELETE CASCADE" + ");";
        String CREATE_ARTIST_AVAILABILITIES_TABLE = "CREATE TABLE artist_availabilities (id INTEGER PRIMARY KEY, artist_id INTEGER NOT NULL, start_time TEXT NOT NULL, end_time TEXT NOT NULL, is_booked INTEGER NOT NULL DEFAULT 0, created_at TEXT DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (artist_id) REFERENCES users(id) ON DELETE CASCADE);";
        String CREATE_BOOKINGS_TABLE = "CREATE TABLE bookings (id INTEGER PRIMARY KEY, customer_id INTEGER NOT NULL, artist_id INTEGER NOT NULL, event_title TEXT NOT NULL, event_location TEXT NOT NULL, start_time TEXT NOT NULL, end_time TEXT NOT NULL, status TEXT NOT NULL DEFAULT 'pending', price REAL NOT NULL, created_at TEXT DEFAULT CURRENT_TIMESTAMP, updated_at TEXT, FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE, FOREIGN KEY (artist_id) REFERENCES users(id) ON DELETE CASCADE);";
        String CREATE_REVIEWS_TABLE = "CREATE TABLE reviews (id INTEGER PRIMARY KEY, booking_id INTEGER UNIQUE NOT NULL, customer_id INTEGER NOT NULL, artist_id INTEGER NOT NULL, rating INTEGER NOT NULL CHECK(rating BETWEEN 1 AND 5), comment TEXT, created_at TEXT DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE, FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE, FOREIGN KEY (artist_id) REFERENCES users(id) ON DELETE CASCADE);";
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ARTIST_PROFILES_TABLE);
        db.execSQL(CREATE_ARTIST_AVAILABILITIES_TABLE);
        db.execSQL(CREATE_BOOKINGS_TABLE);
        db.execSQL(CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTIST_AVAILABILITIES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTIST_PROFILES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }

    public boolean addUser(String email, String hashedPassword, String fullName, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_EMAIL, email);
        values.put(KEY_USER_PASSWORD_HASH, hashedPassword);
        values.put(KEY_USER_FULL_NAME, fullName);
        values.put(KEY_USER_ROLE, role);
        long result = db.insertWithOnConflict(TABLE_USERS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d(TAG, "addUser: " + fullName + " | Success: " + (result != -1));
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = { KEY_ID };
        String selection = KEY_USER_EMAIL + " = ?" + " AND " + KEY_USER_PASSWORD_HASH + " = ?";
        String[] selectionArgs = { email, password };
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public long addArtistProfile(int userId, String stageName, String genres, double price, String location, double rating, int experience, String social) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PROFILE_USER_ID, userId);
        values.put(KEY_PROFILE_STAGE_NAME, stageName);
        values.put(KEY_PROFILE_GENRES, genres);
        values.put(KEY_PROFILE_PRICE_PER_HOUR, price);
        values.put(KEY_PROFILE_LOCATION, location);
        values.put(KEY_PROFILE_RATING_AVG, rating);
        values.put(KEY_PROFILE_EXPERIENCE_YEARS, experience);
        values.put(KEY_PROFILE_SOCIAL_LINKS, social);
        long id = db.insertWithOnConflict(TABLE_ARTIST_PROFILES, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d(TAG, "addArtistProfile for user: " + userId + " | Success: " + (id != -1));
        return id;
    }

    public long addBooking(int customerId, int artistId, String title, String location, String startTime, String endTime, String status, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BOOKING_CUSTOMER_ID, customerId);
        values.put(KEY_BOOKING_ARTIST_ID, artistId);
        values.put(KEY_BOOKING_EVENT_TITLE, title);
        values.put(KEY_BOOKING_EVENT_LOCATION, location);
        values.put(KEY_BOOKING_START_TIME, startTime);
        values.put(KEY_BOOKING_END_TIME, endTime);
        values.put(KEY_BOOKING_STATUS, status);
        values.put(KEY_BOOKING_PRICE, price);
        long id = db.insertWithOnConflict(TABLE_BOOKINGS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d(TAG, "addBooking: " + title + " for artist " + artistId + " | ID: " + id);
        return id;
    }

    public long addReview(int bookingId, int customerId, int artistId, int rating, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_REVIEW_BOOKING_ID, bookingId);
        values.put(KEY_REVIEW_CUSTOMER_ID, customerId);
        values.put(KEY_REVIEW_ARTIST_ID, artistId);
        values.put(KEY_REVIEW_RATING, rating);
        values.put(KEY_REVIEW_COMMENT, comment);
        long id = db.insertWithOnConflict(TABLE_REVIEWS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d(TAG, "addReview for booking: " + bookingId + " | Rating: " + rating + " | ID: " + id);
        return id;
    }
    
    public Cursor getReviewsForArtist(int artistUserId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT r.id, r.rating, r.comment, r.created_at, u.full_name " + "FROM " + TABLE_REVIEWS + " r " + "JOIN " + TABLE_USERS + " u ON r.customer_id = u.id " + "WHERE r.artist_id = ? ORDER BY r.created_at DESC";
        return db.rawQuery(query, new String[]{String.valueOf(artistUserId)});
    }

    public double getArtistAverageRating(int artistUserId) {
        SQLiteDatabase db = this.getReadableDatabase();
        double avgRating = 0.0;
        Cursor cursor = db.query(TABLE_ARTIST_PROFILES, new String[]{KEY_PROFILE_RATING_AVG}, KEY_PROFILE_USER_ID + " = ?", new String[]{String.valueOf(artistUserId)}, null, null, null);
        if (cursor.moveToFirst()) {
            avgRating = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PROFILE_RATING_AVG));
        }
        cursor.close();
        return avgRating;
    }

    public Cursor getBookingsForArtist(int artistId, String status) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_BOOKINGS, null, KEY_BOOKING_ARTIST_ID + " = ? AND " + KEY_BOOKING_STATUS + " = ?", new String[]{String.valueOf(artistId), status}, null, null, KEY_BOOKING_START_TIME + " DESC");
    }
    public boolean updateBookingStatus(int bookingId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_BOOKING_STATUS, status);
        int rowsAffected = db.update(TABLE_BOOKINGS, values, KEY_ID + " = ?", new String[]{String.valueOf(bookingId)});
        Log.d(TAG, "updateBookingStatus for booking: " + bookingId + " to " + status + " | Success: " + (rowsAffected > 0));
        return rowsAffected > 0;
    }

    public Cursor getArtistProfile(int artistUserId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT ap." + KEY_PROFILE_STAGE_NAME + ", u." + KEY_USER_BIO + ", ap." + KEY_PROFILE_EXPERIENCE_YEARS + " FROM " + TABLE_ARTIST_PROFILES + " ap " +
                       "JOIN " + TABLE_USERS + " u ON ap." + KEY_PROFILE_USER_ID + " = u." + KEY_ID +
                       " WHERE ap." + KEY_PROFILE_USER_ID + " = ?";
        return db.rawQuery(query, new String[]{String.valueOf(artistUserId)});
    }

    public boolean updateArtistProfile(int artistUserId, String stageName, String bio, int experienceYears) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        ContentValues profileValues = new ContentValues();
        profileValues.put(KEY_PROFILE_STAGE_NAME, stageName);
        profileValues.put(KEY_PROFILE_EXPERIENCE_YEARS, experienceYears);
        int profileRowsAffected = db.update(TABLE_ARTIST_PROFILES, profileValues, KEY_PROFILE_USER_ID + " = ?", new String[]{String.valueOf(artistUserId)});

        ContentValues userValues = new ContentValues();
        userValues.put(KEY_USER_BIO, bio);
        int userRowsAffected = db.update(TABLE_USERS, userValues, KEY_ID + " = ?", new String[]{String.valueOf(artistUserId)});
        
        Log.d(TAG, "updateArtistProfile for user: " + artistUserId + " | Success: " + (profileRowsAffected > 0 || userRowsAffected > 0));
        return profileRowsAffected > 0 || userRowsAffected > 0;
    }
}
