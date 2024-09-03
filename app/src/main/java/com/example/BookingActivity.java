package com.example;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.conf.R;

public class BookingActivity extends AppCompatActivity {

    private TextView roomNameTextView;
    private EditText userNameEditText;
    private EditText dateEditText;
    private EditText startTimeEditText;
    private EditText endTimeEditText;
    private Button bookNowButton;
    private DatabaseHelper databaseHelper;
    private String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        databaseHelper = new DatabaseHelper(this);

        roomNameTextView = findViewById(R.id.roomNameTextView);
        userNameEditText = findViewById(R.id.userNameEditText);
        dateEditText = findViewById(R.id.dateEditText);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);
        bookNowButton = findViewById(R.id.bookNowButton);

        roomName = getIntent().getStringExtra("roomName");
        roomNameTextView.setText(roomName);

        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookRoom();
            }
        });
    }

    private void bookRoom() {
        String userName = userNameEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String startTime = startTimeEditText.getText().toString().trim();
        String endTime = endTimeEditText.getText().toString().trim();

        if (userName.isEmpty() || date.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues bookingValues = new ContentValues();
        bookingValues.put(DatabaseHelper.COLUMN_BOOKING_ROOM_ID, getRoomId(roomName));
        bookingValues.put(DatabaseHelper.COLUMN_USER_NAME, userName);
        bookingValues.put(DatabaseHelper.COLUMN_DATE, date);
        bookingValues.put(DatabaseHelper.COLUMN_START_TIME, startTime);
        bookingValues.put(DatabaseHelper.COLUMN_END_TIME, endTime);

        long newBookingId = db.insert(DatabaseHelper.TABLE_BOOKINGS, null, bookingValues);

        if (newBookingId != -1) {
            Toast.makeText(this, "Room booked successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error booking room", Toast.LENGTH_SHORT).show();
        }
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
