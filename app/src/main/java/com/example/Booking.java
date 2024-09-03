package com.example;

public class Booking {
    private int id;
    private int roomId;
    private String userName;
    private String date;
    private String startTime;
    private String endTime;

    public Booking(int id, int roomId, String userName, String date, String startTime, String endTime) {
        this.id = id;
        this.roomId = roomId;
        this.userName = userName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters
    public int getId() { return id; }
    public int getRoomId() { return roomId; }
    public String getUserName() { return userName; }
    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
}

