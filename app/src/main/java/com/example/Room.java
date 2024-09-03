package com.example;

public class Room {
    private int id;
    private String name;
    private int capacity;

    public Room(int id, String name, int capacity) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
}

