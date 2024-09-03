package com.example;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "conference_room_booking.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ROOMS = "rooms";
    public static final String TABLE_BOOKINGS = "bookings";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ROOM_NAME = "name";
    public static final String COLUMN_CAPACITY = "capacity";

    public static final String COLUMN_BOOKING_ROOM_ID = "room_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_END_TIME = "end_time";

    private static final String TABLE_CREATE_ROOMS =
            "CREATE TABLE " + TABLE_ROOMS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ROOM_NAME + " TEXT, " +
                    COLUMN_CAPACITY + " INTEGER" + ");";

    private static final String TABLE_CREATE_BOOKINGS =
            "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_BOOKING_ROOM_ID + " INTEGER, " +
                    COLUMN_USER_NAME + " TEXT, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_START_TIME + " TEXT, " +
                    COLUMN_END_TIME + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_BOOKING_ROOM_ID + ") REFERENCES " + TABLE_ROOMS + "(" + COLUMN_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_ROOMS);
        db.execSQL(TABLE_CREATE_BOOKINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }
}

