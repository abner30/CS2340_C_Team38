package com.example.myproject.model;

public class Destination {
    /**
     * location.
     */
    private String location;
    /**
     * startDate.
     */
    private String startDate;
    /**
     * endDate.
     */
    private String endDate;
    /**
     * destinationCounter.
     */
    private int destinationCounter;

    /**
     * No args constructor.
     */
    public Destination() {
    }

    /**
     * All args constructor.
     * @param location
     * @param start
     * @param end
     * @param destinationCounter
     */
    public Destination(final String location, final String start,
                        final String end, final int destinationCounter) {
        this.location = location;
        this.startDate = start;
        this.endDate = end;
        this.destinationCounter = destinationCounter;
    }

    /**
     * Getter for location.
     * @return location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Getter for startDate.
     * @return startDate
     */
    public String getStartDate() {
        return startDate;
    }
    /**
     * Getter for endDate.
     * @return endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Setter for location.
     * @param location
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * Setter for startDate.
     * @param startDate
     */
    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }

    /**
     * Setter for endDate.
     * @param endDate
     */
    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }

    /**
     * Getter for destinationCounter.
     * @return destinationCounter
     */
    public int getDestinationCounter() {
        return destinationCounter;
    }

    /**
     * Setter for destinationCounter.
     * @param destinationCounter
     */
    public void setDestinationCounter(final int destinationCounter) {
        this.destinationCounter = destinationCounter;
    }
}
