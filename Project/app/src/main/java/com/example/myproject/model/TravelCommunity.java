package com.example.myproject.model;

import java.util.ArrayList;

/**
 * TravelCommunity class represents a travel post in the Community Travel Database.
 * This class stores comprehensive information about a user's travel experience including
 * trip duration, destinations visited in order, accommodations, dining experiences,
 * transportation details, and personal notes/reflections about the trip.
 *
 * The class is designed to work with Firebase Realtime Database, storing travel posts
 * that can be shared with the travel community.
 */
public class TravelCommunity {

    /** Duration of the trip in days. */
    private int duration;

    /** Unique identifier of the user who created the post. */
    private String userId;

    /** Comma-separated string of destinations in chronological visit order. */
    private String destinationOrder;

    /** List of accommodations used during the trip. */
    private ArrayList<Accommodation> accommodations;

    /** List of dining experiences during the trip. */
    private ArrayList<Dining> dinings;

    /** Transportation methods used during the trip. */
    private String transportation;

    /** User's notes, reflections, likes, and dislikes about the trip. */
    private String notes;

    /**
     * Default constructor required for Firebase Realtime Database deserialization.
     */
    public TravelCommunity() {
    }

    /**
     * Constructs a new TravelCommunity object with all necessary trip information.
     *
     * @param userId The unique identifier of the user creating the post
     * @param duration The total duration of the trip in days
     * @param destinationOrder Comma-separated string of destinations in visit order
     * @param accommodations List of accommodations used during the trip
     * @param dinings List of dining experiences during the trip
     * @param transportation Transportation methods used during the trip
     * @param notes User's reflections and notes about the trip
     */
    public TravelCommunity(String userId, int duration, String destinationOrder,
                           ArrayList<Accommodation> accommodations,
                           ArrayList<Dining> dinings,
                           String transportation, String notes) {
        this.userId = userId;
        this.duration = duration;
        this.destinationOrder = destinationOrder;
        this.accommodations = accommodations;
        this.dinings = dinings;
        this.transportation = transportation;
        this.notes = notes;
    }

    /**
     * Gets the duration of the trip in days.
     *
     * @return The trip duration in days
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the trip.
     *
     * @param duration The number of days the trip lasted
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets the user ID of the post creator.
     *
     * @return The unique identifier of the user who created the post
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user ID of the post creator.
     *
     * @param userId The unique identifier of the user creating the post
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the ordered list of destinations.
     *
     * @return Comma-separated string of destinations in chronological order
     */
    public String getDestinationOrder() {
        return destinationOrder;
    }

    /**
     * Sets the ordered list of destinations.
     *
     * @param destinationOrder Comma-separated string of destinations in visit order
     */
    public void setDestinationOrder(String destinationOrder) {
        this.destinationOrder = destinationOrder;
    }

    /**
     * Gets the list of accommodations used during the trip.
     *
     * @return ArrayList of Accommodation objects
     */
    public ArrayList<Accommodation> getAccommodations() {
        return accommodations;
    }

    /**
     * Sets the list of accommodations used during the trip.
     *
     * @param accommodations ArrayList of Accommodation objects
     */
    public void setAccommodations(ArrayList<Accommodation> accommodations) {
        this.accommodations = accommodations;
    }

    /**
     * Gets the list of dining experiences during the trip.
     *
     * @return ArrayList of Dining objects
     */
    public ArrayList<Dining> getDinings() {
        return dinings;
    }

    /**
     * Sets the list of dining experiences during the trip.
     *
     * @param dinings ArrayList of Dining objects
     */
    public void setDinings(ArrayList<Dining> dinings) {
        this.dinings = dinings;
    }

    /**
     * Gets the transportation methods used during the trip.
     *
     * @return String describing transportation methods used
     */
    public String getTransportation() {
        return transportation;
    }

    /**
     * Sets the transportation methods used during the trip.
     *
     * @param transportation String describing transportation methods used
     */
    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    /**
     * Gets the user's notes and reflections about the trip.
     *
     * @return String containing user's notes and reflections
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the user's notes and reflections about the trip.
     *
     * @param notes String containing user's notes and reflections
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
}