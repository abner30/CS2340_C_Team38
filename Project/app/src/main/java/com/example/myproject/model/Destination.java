package com.example.myproject.model;

import java.util.Date;

public class Destination {
    private String location;
    private int duration;
    private String startDate;
    private String endDate;

    public Destination() {
    }

    public Destination (String location, int duration, String start, String end) {
        this.location = location;
        this.duration = duration;
        this.startDate = start;
        this.endDate = end;
    }

    public String getLocation () {
        return location;
    }

    public int getDuration () {
        return duration;
    }

    public String getStartDate () {
        return startDate;
    }

    public String getEndDate () {
        return endDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
