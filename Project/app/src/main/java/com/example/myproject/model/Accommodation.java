package com.example.myproject.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Represents an accommodation booking with details such as check-in and check-out dates,
 * location, number of rooms, type, rating, and expiration status.
 */
public class Accommodation {
    private String checkIn;
    private String checkOut;
    private String location;
    private int rooms;
    private String type;
    private String rating;
    private boolean expired;

    /**
     * Default no-argument constructor required for Firebase deserialization.
     */
    public Accommodation() {
        // Default constructor required for calls to DataSnapshot.getValue(Accommodation.class)
    }

    /**
     * Constructs an Accommodation object with the specified details.
     *
     * @param checkIn  The check-in date in MM/dd/yyyy format.
     * @param checkOut The check-out date in MM/dd/yyyy format.
     * @param location The location of the accommodation.
     * @param rooms    The number of rooms booked.
     * @param type     The type of accommodation (e.g., hotel, apartment).
     * @param rating   The rating of the accommodation.
     */
    public Accommodation(String checkIn, String checkOut, String location, int rooms, String type,
                         String rating) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.location = location;
        this.rooms = rooms;
        this.type = type;
        this.rating = rating;
    }

    /**
     * @return The check-in date.
     */
    public String getCheckIn() {
        return checkIn;
    }

    /**
     * Sets the check-in date.
     *
     * @param checkIn The check-in date in MM/dd/yyyy format.
     */
    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    /**
     * @return The check-out date.
     */
    public String getCheckOut() {
        return checkOut;
    }

    /**
     * Sets the check-out date.
     *
     * @param checkOut The check-out date in MM/dd/yyyy format.
     */
    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    /**
     * @return The location of the accommodation.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the accommodation.
     *
     * @param location The location of the accommodation.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return The number of rooms booked.
     */
    public int getRooms() {
        return rooms;
    }

    /**
     * Sets the number of rooms booked.
     *
     * @param rooms The number of rooms booked.
     */
    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    /**
     * @return The type of accommodation.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of accommodation.
     *
     * @param type The type of accommodation (e.g., hotel, apartment).
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return The rating of the accommodation.
     */
    public String getRating() {
        return rating;
    }

    /**
     * Sets the rating of the accommodation.
     *
     * @param rating The rating of the accommodation.
     */
    public void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * @return True if the booking has expired, otherwise false.
     */
    public boolean isExpired() {
        return expired;
    }

    /**
     * Sets the expiration status of the booking based on the current date and the check-out date.
     * Requires API level O (Android 8.0) or higher.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setExpired() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Date firstDate = null;
        try {
            firstDate = sdf.parse(getCheckOut());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        ZoneId zonedId = ZoneId.of("-05:00");
        LocalDate today = LocalDate.now(zonedId);
        Date secondDate = null;
        try {
            secondDate = sdf2.parse(today.toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long diffInMillies = firstDate.getTime() - secondDate.getTime();
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        this.expired = diff < 0;
    }

    /**
     * Compares the check-out date of this accommodation with another accommodation.
     *
     * @param a Another Accommodation object.
     * @return True if this accommodation's check-out date is after the other accommodation's
     * check-out date, otherwise false.
     */
    public boolean isGreater(Accommodation a) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date firstDate;
        Date secondDate;
        try {
            firstDate = sdf.parse(this.getCheckOut());
            secondDate = sdf.parse(a.getCheckOut());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long diffInMillies = firstDate.getTime() - secondDate.getTime();
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff > 0;
    }
}
