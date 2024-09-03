package com.example;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.conf.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView roomListView;
    private Button addRoomButton;
    private List<String> roomList;
    private ArrayAdapter<String> roomAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);
        roomListView = findViewById(R.id.roomListView);
        addRoomButton = findViewById(R.id.addRoomButton);

        loadRooms();

        roomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedRoom = roomList.get(position);

                // Options for the user to choose: Book Room or Check Availability
                CharSequence[] options = {"Book Room", "Check Availability"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Select an Option");
                builder.setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        // Book Room
                        Intent intent = new Intent(MainActivity.this, BookingActivity.class);
                        intent.putExtra("roomName", selectedRoom);
                        startActivity(intent);
                    } else if (which == 1) {
                        // Check Availability
                        Intent intent = new Intent(MainActivity.this, AvailabilityActivity.class);
                        intent.putExtra("roomName", selectedRoom);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });


        addRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddRoomDialog();
            }
        });
    }

    private void loadRooms() {
        roomList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_ROOMS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String roomName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOM_NAME));
                roomList.add(roomName);
            } while (cursor.moveToNext());
        }
        cursor.close();

        roomAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, roomList);
        roomListView.setAdapter(roomAdapter);
    }

    private void showAddRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Room");

        // Inflate the custom layout for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_room, null);
        builder.setView(dialogView);

        // Get the EditText fields from the custom layout
        final EditText roomNameEditText = dialogView.findViewById(R.id.roomNameEditText);
        final EditText roomCapacityEditText = dialogView.findViewById(R.id.roomCapacityEditText);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String roomName = roomNameEditText.getText().toString().trim();
            String roomCapacityStr = roomCapacityEditText.getText().toString().trim();

            if (!roomName.isEmpty() && !roomCapacityStr.isEmpty()) {
                int roomCapacity = Integer.parseInt(roomCapacityStr);
                addRoom(roomName, roomCapacity);
            } else {
                Toast.makeText(MainActivity.this, "Please enter both name and capacity", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addRoom(String name, int capacity) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ROOM_NAME, name);
        values.put(DatabaseHelper.COLUMN_CAPACITY, capacity);

        long newRowId = db.insert(DatabaseHelper.TABLE_ROOMS, null, values);

        if (newRowId != -1) {
            roomList.add(name);
            roomAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Room added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error adding room", Toast.LENGTH_SHORT).show();
        }
    }
}
