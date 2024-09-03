package com.example;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.conf.R;

import java.util.ArrayList;
import java.util.List;

public class AvailabilityActivity extends AppCompatActivity {

    private TextView availabilityRoomNameTextView;
    private ListView bookingsListView;
    private DatabaseHelper databaseHelper;
    private String roomName;
    private int roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        availabilityRoomNameTextView = findViewById(R.id.availabilityRoomNameTextView);
        bookingsListView = findViewById(R.id.bookingsListView);
        databaseHelper = new DatabaseHelper(this);

        roomName = getIntent().getStringExtra("roomName");
        roomId = getRoomId(roomName);
        availabilityRoomNameTextView.setText(roomName);

        loadBookings();
    }

    private void loadBookings() {
        List<String> bookingList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_BOOKINGS,
                new String[]{DatabaseHelper.COLUMN_USER_NAME, DatabaseHelper.COLUMN_DATE, DatabaseHelper.COLUMN_START_TIME, DatabaseHelper.COLUMN_END_TIME},
                DatabaseHelper.COLUMN_BOOKING_ROOM_ID + "=?",
                new String[]{String.valueOf(roomId)}, null, null, DatabaseHelper.COLUMN_DATE + ", " + DatabaseHelper.COLUMN_START_TIME);

        if (cursor.moveToFirst()) {
            do {
                String userName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_START_TIME));
                String endTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_END_TIME));
                bookingList.add(userName + " | " + date + " | " + startTime + " - " + endTime);
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> bookingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookingList);
        bookingsListView.setAdapter(bookingAdapter);
    }

    private int getRoomId(String roomName) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_ROOMS, new String[]{DatabaseHelper.COLUMN_ID},
                DatabaseHelper.COLUMN_ROOM_NAME + "=?", new String[]{roomName}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int roomId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            cursor.close();
            return roomId;
        } else {
            return -1;
        }
    }
}
