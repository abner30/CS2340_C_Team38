package com.example.myproject.model;

import java.util.List;

public class User {
    private String uid;
    private String email;
    private int duration;
    private String startDate;
    private String endDate;
    private int destinationCounter;

    public User(String uid, String email) {
        this.uid = uid;
        this.email = email;
        this.destinationCounter = 0;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getDestinationCounter() {
        return destinationCounter;
    }

    public void setDestinationCounter(int destinationCounter) {
        this.destinationCounter = destinationCounter;
    }
}
