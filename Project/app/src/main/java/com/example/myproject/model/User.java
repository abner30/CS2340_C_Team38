package com.example.myproject.model;

/**
 * User Class
 */
public class User {
    private String uid;
    private String email;
    private int duration;
    private String startDate;
    private String endDate;
    private int destinationCounter;

    /**
     * User Constructor
     * @param uid the user ID
     * @param email the user's email
     */
    public User(String uid, String email) {
        this.uid = uid;
        this.email = email;
        this.destinationCounter = 0;
    }

    /**
     * Gets the user's email.
     * @return the email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email.
     * @param email the email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the user ID.
     * @return the user ID.
     */
    public String getUid() {
        return uid;
    }

    /**
     * Sets the user ID.
     * @param uid the user ID to set.
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Gets the duration.
     * @return the duration.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration.
     * @param duration the duration to set.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets the end date.
     * @return the end date.
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date.
     * @param endDate the end date to set.
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * Gets the start date.
     * @return the start date.
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date.
     * @param startDate the start date to set.
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
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
