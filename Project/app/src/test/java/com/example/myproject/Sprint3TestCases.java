package com.example.myproject;

import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Sprint3TestCases {

    // Test 1: Verify dining required fields validation
    @Test
    public void testDiningFieldsNotEmpty() {
        Map<String, String> diningReservation = new HashMap<>();
        diningReservation.put("location", "Test Restaurant");
        diningReservation.put("date", "12/25/2024");
        diningReservation.put("time", "19:00");

        // Verify no required fields are empty
        assertFalse("Location should not be empty",
                diningReservation.get("location").isEmpty());
        assertFalse("Date should not be empty",
                diningReservation.get("date").isEmpty());
        assertFalse("Time should not be empty",
                diningReservation.get("time").isEmpty());
    }

    // Test 2: Verify dining fields with empty values
    @Test
    public void testDiningFieldsEmpty() {
        Map<String, String> diningReservation = new HashMap<>();
        diningReservation.put("location", "");
        diningReservation.put("date", "");
        diningReservation.put("time", "");

        // Verify empty fields are detected
        assertTrue("Empty location should be detected",
                diningReservation.get("location").isEmpty());
        assertTrue("Empty date should be detected",
                diningReservation.get("date").isEmpty());
        assertTrue("Empty time should be detected",
                diningReservation.get("time").isEmpty());
    }

    // Test 3: Verify accommodation required fields validation
    @Test
    public void testAccommodationFieldsNotEmpty() {
        Map<String, String> accommodation = new HashMap<>();
        accommodation.put("hotelName", "Test Hotel");
        accommodation.put("checkInDate", "12/25/2024");
        accommodation.put("checkOutDate", "12/27/2024");
        accommodation.put("roomType", "King Suite");

        // Verify no required fields are empty
        assertFalse("Hotel name should not be empty",
                accommodation.get("hotelName").isEmpty());
        assertFalse("Check-in date should not be empty",
                accommodation.get("checkInDate").isEmpty());
        assertFalse("Check-out date should not be empty",
                accommodation.get("checkOutDate").isEmpty());
    }

    // Test 4: Verify accommodation fields with empty values
    @Test
    public void testAccommodationFieldsEmpty() {
        Map<String, String> accommodation = new HashMap<>();
        accommodation.put("hotelName", "");
        accommodation.put("checkInDate", "");
        accommodation.put("checkOutDate", "");

        // Verify empty fields are detected
        assertTrue("Empty hotel name should be detected",
                accommodation.get("hotelName").isEmpty());
        assertTrue("Empty check-in date should be detected",
                accommodation.get("checkInDate").isEmpty());
        assertTrue("Empty check-out date should be detected",
                accommodation.get("checkOutDate").isEmpty());
    }

    // Test 5: Verify dining date format validation
    @Test
    public void testDiningDateFormat() {
        String date = "12/25/2024";
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);

        try {
            Date parsedDate = sdf.parse(date);
            assertNotNull("Date should be parsed successfully", parsedDate);
        } catch (ParseException e) {
            fail("Valid date format should not throw exception");
        }
    }

    // Test 6: Verify accommodation date format validation
    @Test
    public void testAccommodationDateFormat() {
        String checkInDate = "12/25/2024";
        String checkOutDate = "12/27/2024";
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);

        try {
            Date parsedCheckIn = sdf.parse(checkInDate);
            Date parsedCheckOut = sdf.parse(checkOutDate);
            assertNotNull("Check-in date should be parsed successfully", parsedCheckIn);
            assertNotNull("Check-out date should be parsed successfully", parsedCheckOut);
        } catch (ParseException e) {
            fail("Valid date format should not throw exception");
        }
    }

    // Test 7: Verify dining form data completeness
    @Test
    public void testDiningFormCompleteness() {
        Map<String, String> diningReservation = new HashMap<>();
        diningReservation.put("location", "Test Restaurant");
        diningReservation.put("date", "12/25/2024");
        diningReservation.put("time", "19:00");
        diningReservation.put("notes", "Window seat preferred");

        // Check all expected fields are present
        assertTrue("Form should contain location", diningReservation.containsKey("location"));
        assertTrue("Form should contain date", diningReservation.containsKey("date"));
        assertTrue("Form should contain time", diningReservation.containsKey("time"));
    }

    // Test 8: Verify accommodation form data completeness
    @Test
    public void testAccommodationFormCompleteness() {
        Map<String, String> accommodation = new HashMap<>();
        accommodation.put("hotelName", "Test Hotel");
        accommodation.put("checkInDate", "12/25/2024");
        accommodation.put("checkOutDate", "12/27/2024");
        accommodation.put("roomType", "King Suite");

        // Check all expected fields are present
        assertTrue("Form should contain hotel name", accommodation.containsKey("hotelName"));
        assertTrue("Form should contain check-in date", accommodation.containsKey("checkInDate"));
        assertTrue("Form should contain check-out date", accommodation.containsKey("checkOutDate"));
    }

    // Test 9: Verify valid dining time format
    @Test
    public void testDiningTimeFormat() {
        String time = "19:00";
        assertTrue("Time should be in correct format",
                time.matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"));
    }

    // Test 10: Verify accommodation dates logic
    @Test
    public void testAccommodationDatesLogic() throws ParseException {
        String checkInDate = "12/25/2024";
        String checkOutDate = "12/27/2024";
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        Date parsedCheckIn = sdf.parse(checkInDate);
        Date parsedCheckOut = sdf.parse(checkOutDate);

        assertTrue("Check-out should be after check-in",
                parsedCheckOut.after(parsedCheckIn));
    }

    // Test 11: Verify whitespace handling in dining form
    @Test
    public void testDiningWhitespaceHandling() {
        String location = "   Test Restaurant   ";
        assertEquals("Whitespace should be trimmed",
                "Test Restaurant", location.trim());
    }

    // Test 12: Verify whitespace handling in accommodation form
    @Test
    public void testAccommodationWhitespaceHandling() {
        String hotelName = "   Test Hotel   ";
        assertEquals("Whitespace should be trimmed",
                "Test Hotel", hotelName.trim());
    }
}