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

public class Dining {
    private String location;
    private String website;
    private String date;
    private String time;
    private boolean expired;

    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static final String TIME_FORMAT = "HH:mm";
    private static final ZoneId ZONE_ID = ZoneId.of("America/New_York");

    public Dining(String location, String website, String time, String date) {
        this.location = location;
        this.website = website;
        this.time = time;
        this.date = date;
    }

    // Getters and setters remain the same
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public boolean isExpired() { return expired; }

    private boolean isTimeFormat(String input) {
        return input != null && input.matches("\\d{1,2}:\\d{2}");
    }

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
                    LocalTime eventTime = LocalTime.parse(time, DateTimeFormatter.ofPattern(TIME_FORMAT));
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isGreater(Dining other) {
        // Handle null cases
        if (other == null) return true;

        try {
            // If either date is in time format, use today's date for comparison
            LocalDate today = LocalDate.now(ZONE_ID);
            String thisDate = isTimeFormat(this.date) ? today.format(DateTimeFormatter.ofPattern(DATE_FORMAT)) : this.date;
            String otherDate = isTimeFormat(other.date) ? today.format(DateTimeFormatter.ofPattern(DATE_FORMAT)) : other.date;

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