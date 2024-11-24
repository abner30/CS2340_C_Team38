package com.example.myproject.model;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Represents a dining event with a location, website, date, time, and expiration status.
 * The class provides utility methods for handling date and time validation, expiration checks,
 * and comparisons between different dining events.
 * It uses both legacy `java.util.Date` and modern `java.time` APIs for handling date and time.
 * This class is compatible with Firebase data structure.
 */
public class Dining {
    private String location;
    private String website;
    private String date;
    private String time;
    private boolean expired;

    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static final String TIME_FORMAT = "HH:mm";
    private static final ZoneId ZONE_ID = ZoneId.of("America/New_York");

    /**
     * Default no-argument constructor required for Firebase
     */
    public Dining() {
        // Default constructor required for calls to DataSnapshot.getValue(Dining.class)
    }

    /**
     * Creates a new Dining event with the given parameters.
     *
     * @param location the location of the dining event
     * @param website  the website associated with the dining event
     * @param time     the time of the dining event in HH:mm format
     * @param date     the date of the dining event in MM/dd/yyyy format
     */
    public Dining(String location, String website, String time, String date) {
        this.location = location;
        this.website = website;
        this.time = time;
        this.date = date;
    }

    // Getters and setters remain the same
    /**
     * Gets the location of the dining event.
     *
     * @return the location of the dining event
     */
    public String getLocation() {
        return location; }
    /**
     * Sets the location of the dining event.
     *
     * @param location the location of the dining event
     */
    public void setLocation(String location) {
        this.location = location; }
    /**
     * Gets the website of the dining event.
     *
     * @return the website of the dining event
     */
    public String getWebsite() {
        return website; }
    /**
     * Sets the website of the dining event.
     *
     * @param website the website of the dining event
     */
    public void setWebsite(String website) {
        this.website = website; }
    /**
     * Gets the date of the dining event.
     *
     * @return the date of the dining event in MM/dd/yyyy format
     */
    public String getDate() {
        return date; }
    /**
     * Sets the date of the dining event.
     *
     * @param date the date of the dining event in MM/dd/yyyy format
     */
    public void setDate(String date) {
        this.date = date; }
    /**
     * Gets the time of the dining event.
     *
     * @return the time of the dining event in HH:mm format
     */
    public String getTime() {
        return time; }
    /**
     * Sets the time of the dining event.
     *
     * @param time the time of the dining event in HH:mm format
     */
    public void setTime(String time) {
        this.time = time; }
    /**
     * Checks if the dining event has expired.
     *
     * @return {@code true} if the event has expired, otherwise {@code false}
     */
    public boolean isExpired() {
        return expired; }

    /**
     * Determines if a given input string follows a valid time format.
     *
     * @param input the string to validate
     * @return {@code true} if the string matches the time format (HH:mm), otherwise {@code false}
     */
    private boolean isTimeFormat(String input) {
        return input != null && input.matches("\\d{1,2}:\\d{2}");
    }

    /**
     * Updates the expiration status of the dining event.
     * The expiration status is determined based on the current date and time in the
     * "America/New_York" time zone.
     * If the event date is today, it compares the event time with the current time.
     * If the event is in the past, it is marked as expired.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setExpired() {
        // If date field contains a time format, swap it with the time field
        if (isTimeFormat(date) && !isTimeFormat(time)) {
            String temp = date;
            date = time;
            time = temp;
        }

        try {
            // If we still don't have a valid date, use today's date
            if (date == null || date.trim().isEmpty() || isTimeFormat(date)) {
                LocalDate today = LocalDate.now(ZONE_ID);
                date = today.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
            }

            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            Date eventDate = sdf.parse(date);
            if (eventDate == null) {
                throw new ParseException("Failed to parse date: " + date, 0);
            }

            // Get current date
            LocalDate today = LocalDate.now(ZONE_ID);
            Date currentDate = sdf.parse(today.format(DateTimeFormatter.ofPattern(DATE_FORMAT)));

            if (currentDate == null) {
                throw new ParseException("Failed to parse current date", 0);
            }

            // Compare dates
            long diffInMillies = eventDate.getTime() - currentDate.getTime();
            long diffDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (diffDays < 0) {
                this.expired = true;
                return;
            }

            if (diffDays == 0) {
                // Handle case where time might be null or invalid
                if (time == null || !isTimeFormat(time)) {
                    this.expired = true;
                    return;
                }

                // If same day, compare times
                try {
                    LocalTime currentTime = LocalTime.now(ZONE_ID);
                    LocalTime eventTime = LocalTime.parse(time,
                            DateTimeFormatter.ofPattern(TIME_FORMAT));
                    this.expired = currentTime.compareTo(eventTime) >= 0;
                } catch (DateTimeParseException e) {
                    // If we can't parse the time, consider it expired
                    this.expired = true;
                }
            } else {
                this.expired = false;
            }
        } catch (ParseException e) {
            // If we can't parse the date at all, consider it expired
            this.expired = true;
        }
    }

    /**
     * Compares this dining event with another dining event to determine if it is greater (later).
     * The comparison is based on the event's date and time. If the event's date is later, or
     * the date is the same but the time is later, it is considered greater.
     *
     * @param other the other dining event to compare against
     * @return {@code true} if this dining event is later than the other, otherwise {@code false}
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isGreater(Dining other) {
        // Handle null cases
        if (other == null) {
            return true;
        }

        try {
            // If either date is in time format, use today's date for comparison
            LocalDate today = LocalDate.now(ZONE_ID);
            String thisDate = isTimeFormat(this.date) ? today.format(DateTimeFormatter.
                    ofPattern(DATE_FORMAT)) : this.date;
            String otherDate = isTimeFormat(other.date) ? today.format(DateTimeFormatter.
                    ofPattern(DATE_FORMAT)) : other.date;

            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            Date date1 = sdf.parse(thisDate);
            Date date2 = sdf.parse(otherDate);

            if (date1 == null || date2 == null) {
                return false;
            }

            long diffInMillies = date1.getTime() - date2.getTime();
            long diffDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            if (diffDays != 0) {
                return diffDays > 0;
            }

            // If same day, compare times (if available)
            String time1 = isTimeFormat(this.time) ? this.time : this.date;
            String time2 = isTimeFormat(other.time) ? other.time : other.date;

            if (!isTimeFormat(time1) || !isTimeFormat(time2)) {
                return false;
            }

            LocalTime thisTime = LocalTime.parse(time1, DateTimeFormatter.ofPattern(TIME_FORMAT));
            LocalTime otherTime = LocalTime.parse(time2, DateTimeFormatter.ofPattern(TIME_FORMAT));
            return thisTime.compareTo(otherTime) > 0;

        } catch (Exception e) {
            // If any parsing fails, return false for safety
            return false;
        }
    }
}