package com.example.myproject;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.myproject.database.DatabaseManager;
import com.example.myproject.model.Accommodation;
import com.example.myproject.model.Dining;
import com.example.myproject.model.TravelCommunity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Instrumented test cases for TravelCommunity Firebase database operations.
 * These tests verify the actual database connections and operations.
 */
@RunWith(AndroidJUnit4.class)
public class TravelCommunityFirebaseTests {
    private DatabaseReference testDbRef;
    private TravelCommunity testPost;
    private String testPostId;

    /**
     * Set up the test environment before each test.
     */
    @Before
    public void setUp() {
        // Initialize Firebase test reference
        testDbRef = FirebaseDatabase.getInstance().getReference()
                .child("communityTravel").child("test");

        // Create test data
        ArrayList<Accommodation> accommodations = new ArrayList<>();
        accommodations.add(new Accommodation(
                "12/01/2024",
                "12/10/2024",
                "Test Hotel 2",
                1,
                "Suite",
                "5/5"
        ));

        ArrayList<Dining> dinings = new ArrayList<>();
        dinings.add(new Dining(
                "Test Restaurant",
                "www.test.com",
                "19:00",
                "12/02/2024"
        ));

        testPost = new TravelCommunity(
                "testUser",
                5,
                "TestCity1,TestCity2",
                accommodations,
                dinings,
                "Test Transport",
                "Test Notes"
        );
    }

    /**
     * Clean up test data after each test.
     */
    @After
    public void tearDown() {
        if (testPostId != null) {
            testDbRef.child(testPostId).removeValue();
        }
    }

    /**
     * Test writing a TravelCommunity post to Firebase.
     */
    @Test
    public void testWriteToFirebase() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final boolean[] writeSuccess = {false};

        testPostId = testDbRef.push().getKey();
        testDbRef.child(testPostId).setValue(testPost)
                .addOnSuccessListener(aVoid -> {
                    writeSuccess[0] = true;
                    latch.countDown();
                })
                .addOnFailureListener(e -> {
                    writeSuccess[0] = false;
                    latch.countDown();
                });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue("Data should be written to Firebase", writeSuccess[0]);
    }

    /**
     * Test reading a TravelCommunity post from Firebase.
     */
    @Test
    public void testReadFromFirebase() throws InterruptedException {
        // First write data
        final CountDownLatch writeLatch = new CountDownLatch(1);
        testPostId = testDbRef.push().getKey();
        testDbRef.child(testPostId).setValue(testPost)
                .addOnCompleteListener(task -> writeLatch.countDown());
        writeLatch.await(5, TimeUnit.SECONDS);

        // Then read and verify
        final CountDownLatch readLatch = new CountDownLatch(1);
        final TravelCommunity[] readPost = {null};

        testDbRef.child(testPostId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                readPost[0] = snapshot.getValue(TravelCommunity.class);
                readLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                readLatch.countDown();
            }
        });

        readLatch.await(5, TimeUnit.SECONDS);
        assertNotNull("Retrieved post should not be null", readPost[0]);
        assertEquals("Duration should match", testPost.getDuration(), readPost[0].getDuration());
        assertEquals("Destinations should match", testPost.getDestinationOrder(),
                readPost[0].getDestinationOrder());
    }

    /**
     * Test updating a TravelCommunity post in Firebase.
     */
    @Test
    public void testUpdateInFirebase() throws InterruptedException {
        // First write initial data
        final CountDownLatch writeLatch = new CountDownLatch(1);
        testPostId = testDbRef.push().getKey();
        testDbRef.child(testPostId).setValue(testPost)
                .addOnCompleteListener(task -> writeLatch.countDown());
        writeLatch.await(5, TimeUnit.SECONDS);

        // Update the post
        final CountDownLatch updateLatch = new CountDownLatch(1);
        testPost.setDuration(10);
        testPost.setNotes("Updated notes");

        testDbRef.child(testPostId).setValue(testPost)
                .addOnCompleteListener(task -> updateLatch.countDown());
        updateLatch.await(5, TimeUnit.SECONDS);

        // Verify update
        final CountDownLatch verifyLatch = new CountDownLatch(1);
        final TravelCommunity[] updatedPost = {null};

        testDbRef.child(testPostId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                updatedPost[0] = snapshot.getValue(TravelCommunity.class);
                verifyLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                verifyLatch.countDown();
            }
        });

        verifyLatch.await(5, TimeUnit.SECONDS);
        assertNotNull("Updated post should not be null", updatedPost[0]);
        assertEquals("Duration should be updated", 10, updatedPost[0].getDuration());
        assertEquals("Notes should be updated", "Updated notes", updatedPost[0].getNotes());
    }

    /**
     * Test deleting a TravelCommunity post from Firebase.
     */
    @Test
    public void testDeleteFromFirebase() throws InterruptedException {
        // First write data
        final CountDownLatch writeLatch = new CountDownLatch(1);
        testPostId = testDbRef.push().getKey();
        testDbRef.child(testPostId).setValue(testPost)
                .addOnCompleteListener(task -> writeLatch.countDown());
        writeLatch.await(5, TimeUnit.SECONDS);

        // Delete the post
        final CountDownLatch deleteLatch = new CountDownLatch(1);
        testDbRef.child(testPostId).removeValue()
                .addOnCompleteListener(task -> deleteLatch.countDown());
        deleteLatch.await(5, TimeUnit.SECONDS);

        // Verify deletion
        final CountDownLatch verifyLatch = new CountDownLatch(1);
        final boolean[] postExists = {true};

        testDbRef.child(testPostId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                postExists[0] = snapshot.exists();
                verifyLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                verifyLatch.countDown();
            }
        });

        verifyLatch.await(5, TimeUnit.SECONDS);
        assertFalse("Post should be deleted", postExists[0]);
    }
}