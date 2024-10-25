package com.example.myproject.model;

/**
 * Destination Class
 */
public class Destination {
    private String location;
    private String startDate;
    private String endDate;
    private int destinationCounter;

    /**
     * Destination Constructor
     */
    public Destination() {
    }

    /**
     * Destination Constructor with parameters
     * @param location the location
     * @param start the start date
     * @param end the end date
     * @param destinationCounter the destination counter
     */
    public Destination(String location, String start, String end, int destinationCounter) {
        this.location = location;
        this.startDate = start;
        this.endDate = end;
        this.destinationCounter = destinationCounter;
    }

    /**
     * Gets the location.
     * @return the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets the start date.
     * @return the start date.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Gets the end date.
     * @return the end date.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the location.
     * @param location the location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the start date.
     * @param startDate the start date to set.
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Sets the end date.
     * @param endDate the end date to set.
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the destination counter.
     * @return the destination counter.
     */
    public int getDestinationCounter() {
        return destinationCounter;
    }

    /**
     * Sets the destination counter.
     * @param destinationCounter the destination counter to set.
     */
    public void setDestinationCounter(int destinationCounter) {
        this.destinationCounter = destinationCounter;
    }
}
