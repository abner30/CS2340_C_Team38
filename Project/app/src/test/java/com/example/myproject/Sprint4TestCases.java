package com.example.myproject;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.example.myproject.model.Accommodation;
import com.example.myproject.model.Dining;
import com.example.myproject.model.TravelCommunity;

import java.util.ArrayList;

/**
 * Test class for Sprint 4 functionality focusing on the Travel Community features.
 * These tests verify the data model and validation logic without Firebase dependencies.
 */
public class Sprint4TestCases {

    private TravelCommunity testPost;
    private ArrayList<Accommodation> accommodations;
    private ArrayList<Dining> dinings;

    /**
     * Set up test data before each test.
     */
    @Before
    public void setUp() {
        // Create test accommodations
        accommodations = new ArrayList<>();
        accommodations.add(new Accommodation(
                "12/01/2024",  // checkIn
                "12/05/2024",  // checkOut
                "Hilton Paris",  // location
                1,  // rooms
                "King Suite",  // type
                "4/5"  // rating
        ));

        // Create test dining reservations
        dinings = new ArrayList<>();
        dinings.add(new Dining(
                "Le Restaurant",  // location
                "www.restaurant.com",  // website
                "19:00",  // time
                "12/02/2024"  // date
        ));

        // Create test travel post
        testPost = new TravelCommunity(
                "testUserId",
                5,  // duration
                "Paris,London,Rome",  // destinations in order
                accommodations,
                dinings,
                "Air France 123, Eurostar, Trenitalia",  // transportation
                "Loved Paris! Would spend more time in Rome next time."  // notes
        );
    }

    /**
     * Test the construction and initialization of a TravelCommunity post.
     */
    @Test
    public void testTravelPostConstruction() {
        assertNotNull("Travel post should not be null", testPost);
        assertEquals("Duration should be 5 days", 5, testPost.getDuration());
        assertEquals("Should have correct user ID", "testUserId", testPost.getUserId());
    }

    /**
     * Test validation of destinations in the travel post.
     */
    @Test
    public void testDestinationValidation() {
        String[] destinations = testPost.getDestinationOrder().split(",");
        assertEquals("Should have 3 destinations", 3, destinations.length);
        assertEquals("First destination should be Paris", "Paris", destinations[0]);
        assertEquals("Second destination should be London", "London", destinations[1]);
        assertEquals("Third destination should be Rome", "Rome", destinations[2]);
    }

    /**
     * Test accommodation details in the travel post.
     */
    @Test
    public void testAccommodationDetails() {
        ArrayList<Accommodation> testAccommodations = testPost.getAccommodations();
        assertNotNull("Accommodations list should not be null", testAccommodations);
        assertFalse("Accommodations list should not be empty", testAccommodations.isEmpty());

        Accommodation firstAccommodation = testAccommodations.get(0);
        assertEquals("Should have correct location", "Hilton Paris", firstAccommodation.getLocation());
        assertEquals("Should have correct check-in date", "12/01/2024", firstAccommodation.getCheckIn());
        assertEquals("Should have correct room type", "King Suite", firstAccommodation.getType());
    }

    /**
     * Test dining details in the travel post.
     */
    @Test
    public void testDiningDetails() {
        ArrayList<Dining> testDinings = testPost.getDinings();
        assertNotNull("Dining list should not be null", testDinings);
        assertFalse("Dining list should not be empty", testDinings.isEmpty());

        Dining firstDining = testDinings.get(0);
        assertEquals("Should have correct restaurant", "Le Restaurant", firstDining.getLocation());
        assertEquals("Should have correct time", "19:00", firstDining.getTime());
        assertEquals("Should have correct date", "12/02/2024", firstDining.getDate());
    }

    /**
     * Test transportation and notes fields.
     */
    @Test
    public void testTransportationAndNotes() {
        assertNotNull("Transportation should not be null", testPost.getTransportation());
        assertTrue("Transportation should contain flight info",
                testPost.getTransportation().contains("Air France"));

        assertNotNull("Notes should not be null", testPost.getNotes());
        assertTrue("Notes should contain valid content",
                testPost.getNotes().contains("Paris"));
    }

    /**
     * Test data modifications.
     */
    @Test
    public void testDataModification() {
        testPost.setDuration(7);
        assertEquals("Duration should be updated", 7, testPost.getDuration());

        String newTransportation = "Updated transportation info";
        testPost.setTransportation(newTransportation);
        assertEquals("Transportation should be updated",
                newTransportation, testPost.getTransportation());
    }

    /**
     * Test empty/null validations.
     */
    @Test
    public void testEmptyValidations() {
        TravelCommunity emptyPost = new TravelCommunity();
        assertEquals("Default duration should be 0", 0, emptyPost.getDuration());
        assertNull("Default user ID should be null", emptyPost.getUserId());

        ArrayList<Accommodation> emptyAccommodations = new ArrayList<>();
        emptyPost.setAccommodations(emptyAccommodations);
        assertTrue("Empty accommodations list should be empty",
                emptyPost.getAccommodations().isEmpty());
    }

    /**
     * Test adding new accommodations and dining entries.
     */
    @Test
    public void testAddingEntries() {
        Accommodation newAccommodation = new Accommodation(
                "12/06/2024", "12/10/2024", "Hotel Rome", 1, "Double", "5/5");
        accommodations.add(newAccommodation);
        testPost.setAccommodations(accommodations);

        assertEquals("Should have 2 accommodations",
                2, testPost.getAccommodations().size());

        Dining newDining = new Dining(
                "Roman Restaurant", "www.roman.com", "20:00", "12/07/2024");
        dinings.add(newDining);
        testPost.setDinings(dinings);

        assertEquals("Should have 2 dining entries",
                2, testPost.getDinings().size());
    }
}