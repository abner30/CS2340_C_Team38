package com.example.myproject;

import static org.junit.Assert.*;
import org.junit.Test;
import com.example.myproject.model.User;
import com.example.myproject.model.Destination;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ComprehensiveTest {

    // Sprint 1 - Authentication Tests
    @Test
    public void testLoginEmptyEmail() {
        User user = new User("123", "");
        assertTrue("Empty email should be detected", user.getEmail().isEmpty());
    }

    @Test
    public void testLoginWhitespaceEmail() {
        User user = new User("123", "   ");
        assertTrue("Whitespace-only email should be detected", user.getEmail().trim().isEmpty());
    }

    // Sprint 2 - Travel Location Tests
    @Test
    public void testEmptyTravelLocation() {
        Destination destination = new Destination("", "01/01/2024", "01/05/2024", 1);
        assertTrue("Empty location should be detected", destination.getLocation().isEmpty());
    }

    @Test
    public void testWhitespaceTravelLocation() {
        Destination destination = new Destination("   ", "01/01/2024", "01/05/2024", 1);
        assertTrue("Whitespace-only location should be detected", destination.getLocation().trim().isEmpty());
    }

    // Sprint 2 - Date Validation Tests
    @Test
    public void testValidDateFormat() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date date = sdf.parse("01/01/2024");
        assertNotNull("Valid date should be parsed", date);
    }

    @Test
    public void testInvalidDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        try {
            sdf.parse("invalid");
            fail("Should throw ParseException for invalid date");
        } catch (ParseException expected) {
            // Test passes if ParseException is thrown
        }
    }

    // Sprint 2 - Duration Calculations
    @Test
    public void testDurationCalculation() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date start = sdf.parse("01/01/2024");
        Date end = sdf.parse("01/05/2024");
        long diffInMillies = Math.abs(end.getTime() - start.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        assertEquals("Duration should be 4 days", 4, diff);
    }

    @Test
    public void testZeroDaysDuration() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date start = sdf.parse("01/01/2024");
        Date end = sdf.parse("01/01/2024");
        long diffInMillies = Math.abs(end.getTime() - start.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        assertEquals("Duration should be 0 days", 0, diff);
    }

    // Sprint 2 - Destination Management
    @Test
    public void testDestinationCounter() {
        Destination destination = new Destination("Paris", "01/01/2024", "01/05/2024", 0);
        destination.setDestinationCounter(1);
        assertEquals("Counter should be incremented", 1, destination.getDestinationCounter());
    }

    @Test
    public void testDestinationAttributes() {
        String location = "Paris";
        String startDate = "01/01/2024";
        String endDate = "01/05/2024";
        Destination destination = new Destination(location, startDate, endDate, 1);

        assertEquals("Location should match", location, destination.getLocation());
        assertEquals("Start date should match", startDate, destination.getStartDate());
        assertEquals("End date should match", endDate, destination.getEndDate());
    }

    // Sprint 2 - User Trip Management
    @Test
    public void testUserTripDates() {
        User user = new User("123", "test@email.com");
        String startDate = "01/01/2024";
        String endDate = "01/05/2024";

        user.setStartDate(startDate);
        user.setEndDate(endDate);

        assertEquals("Start date should be set", startDate, user.getStartDate());
        assertEquals("End date should be set", endDate, user.getEndDate());
    }

    @Test
    public void testUserDuration() {
        User user = new User("123", "test@email.com");
        user.setDuration(5);
        assertEquals("Duration should be set", 5, user.getDuration());
    }
}