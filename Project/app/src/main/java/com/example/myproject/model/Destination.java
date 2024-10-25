package com.example.myproject.model;

public class Destination {
    private String location;
    private String startDate;
    private String endDate;
    private int destinationCounter;

    public Destination() {
    }

    public Destination (String location, String start, String end, int destinationCounter) {
        this.location = location;
        this.startDate = start;
        this.endDate = end;
        this.destinationCounter = destinationCounter;
    }

    public String getLocation () {
        return location;
    }

    public String getStartDate () {
        return startDate;
    }

    public String getEndDate () {
        return endDate;
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

    public int getDestinationCounter() {
        return destinationCounter;
    }

    public void setDestinationCounter(int destinationCounter) {
        this.destinationCounter = destinationCounter;
    }
}
