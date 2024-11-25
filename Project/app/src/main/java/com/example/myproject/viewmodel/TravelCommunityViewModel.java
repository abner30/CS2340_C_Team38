package com.example.myproject.viewmodel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * ViewModel class for handling Travel Community business logic.
 * This class manages data validation and processing for travel posts.
 */
public class TravelCommunityViewModel {

    /**
     * Validates if a date string matches the MM/DD/YYYY format and represents a valid date.
     *
     * @param dateStr The date string to validate
     * @return true if the date is valid and matches the format, false otherwise
     */
    public boolean isValidDateFormat(String dateStr) {
        // Check format
        String datePattern = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/([12][0-9]{3})$";
        if (!Pattern.matches(datePattern, dateStr)) {
            return false;
        }

        // Verify it's a valid date
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Validates that the end date occurs after the start date.
     *
     * @param startDate The start date string in MM/DD/YYYY format
     * @param endDate The end date string in MM/DD/YYYY format
     * @return true if end date is after start date, false otherwise
     */
    public boolean isValidDateRange(String startDate, String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            return end.after(start);
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Callback interface for handling travel post operations.
     */
    public interface TravelPostCallback {
        /**
         * Called when a travel post operation is successful.
         */
        void onSuccess();

        /**
         * Called when a travel post operation fails.
         *
         * @param error The error message describing what went wrong
         */
        void onFailure(String error);
    }
}