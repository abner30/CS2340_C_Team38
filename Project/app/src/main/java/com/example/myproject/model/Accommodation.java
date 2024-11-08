package com.example.myproject.model;

public class Accommodation {
    private String checkIn;
    private String checkOut;
    private String location;
    private int rooms;
    private String type;
    private boolean expired;
    public Accommodation(String checkIn, String checkOut, String location, int rooms, String type) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.location = location;
        this.rooms = rooms;
        this.type = type;
    }

    public Accommodation(String checkIn, String checkOut, String location, int rooms, boolean expired, String type) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.location = location;
        this.rooms = rooms;
        this.expired = expired;
        this.type = type;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}
