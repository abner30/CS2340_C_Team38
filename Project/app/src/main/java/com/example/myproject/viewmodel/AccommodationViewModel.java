package com.example.myproject.viewmodel;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.myproject.database.DatabaseManager;
import com.example.myproject.model.Accommodation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

public class AccommodationViewModel {

    /**
     * reference to database. Initiates once.
     */
    private DatabaseReference database = DatabaseManager.getInstance().getReference();

    /**
     * No args constructor.
     */
    public AccommodationViewModel() {
    }

    public void addAccommodation(Accommodation accommodation, String uid,
                                 AccommodationViewModel.CompletionCallback callback) {
        database.child("accommodations").child("counter")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String lodging = "lodging";
                        // Check for previous lodging
                        if (dataSnapshot.exists()) {
                            Integer firebaseCounter = dataSnapshot.getValue(Integer.class);
                            if (firebaseCounter != null) {
                                lodging += firebaseCounter;
                                database.child("accommodations").child("counter")
                                        .setValue(firebaseCounter + 1);
                            }
                        } else {
                            database.child("accommodations").child("counter")
                                    .setValue(1);
                            lodging = "lodging1";
                        }
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("location", accommodation.getLocation());
                        map.put("check-in", accommodation.getCheckIn());
                        map.put("check-out", accommodation.getCheckOut());
                        map.put("type", accommodation.getType());
                        map.put("rooms", accommodation.getRooms());
                        map.put("rating", accommodation.getRating());
                        map.put("user", uid);
                        database.child("accommodations").child(lodging).setValue(map)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        callback.onComplete();
                                    }
                                });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error if necessary
                    }
                });
    }

    public void getAccommodations(String uid,
                                  AccommodationViewModel.AccommodationsCallback callback) {
        ArrayList<Accommodation> list = new ArrayList<>();
        database.child("accommodations").addListenerForSingleValueEvent(
                new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String user = userSnapshot.child("user").getValue(String.class);
                        if (user != null && user.equals(uid)) {
                            String location = userSnapshot.child("location").getValue(String.class);
                            String checkIn = userSnapshot.child("check-in").getValue(String.class);
                            String checkOut = userSnapshot.child("check-out").
                                    getValue(String.class);
                            String type = userSnapshot.child("type").getValue(String.class);
                            Integer rooms = userSnapshot.child("rooms").getValue(Integer.class);
                            String rating = userSnapshot.child("rating").getValue(String.class);
                            list.add(new Accommodation(checkIn, checkOut, location, rooms, type,
                                    rating));
                        }
                    }

                    // Only sort if the list is not empty
                    if (!list.isEmpty()) {
                        sortAccommodation(list, 0, list.size() - 1);
                    }

                    // Check if each accommodation is expired
                    for (Accommodation a : list) {
                        a.setExpired();
                    }

                    // Pass the filled list to the callback once data retrieval is complete
                    callback.onCallback(list);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error if necessary
                }
            });
    }

    private void mergeAccommodation(ArrayList<Accommodation> a, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        // Create temp arrays with initial capacity
        ArrayList<Accommodation> a1 = new ArrayList<>(n1);
        ArrayList<Accommodation> a2 = new ArrayList<>(n2);

        // Copy data to temp arrays
        for (int i = 0; i < n1; i++) {
            a1.add(a.get(l + i));
        }
        for (int j = 0; j < n2; j++) {
            a2.add(a.get(m + 1 + j));
        }

        int i = 0; // Initial index of first subarray
        int j = 0; // Initial index of second subarray
        int k = l; // Initial index of merged subarray

        // Compare and merge
        while (i < n1 && j < n2) {
            if (a1.get(i).isGreater(a2.get(j))) {
                a.set(k, a1.get(i));
                i++;
            } else {
                a.set(k, a2.get(j));
                j++;
            }
            k++;
        }

        // Copy remaining elements of a1[]
        while (i < n1) {
            a.set(k, a1.get(i));
            i++;
            k++;
        }

        // Copy remaining elements of a2[]
        while (j < n2) {
            a.set(k, a2.get(j));
            j++;
            k++;
        }
    }

    private void sortAccommodation(ArrayList<Accommodation> a, int l, int r) {
        // Add check for empty or null list
        if (a == null || a.isEmpty()) {
            return;
        }

        if (l < r) {
            // Find the middle point
            int m = l + (r - l) / 2;

            // Sort first and second halves
            sortAccommodation(a, l, m);
            sortAccommodation(a, m + 1, r);

            // Merge the sorted halves
            mergeAccommodation(a, l, m, r);
        }
    }

    /**
     * Validates if a date string matches the MM/DD/YYYY format and is a valid date
     * @param dateStr The date string to validate
     * @return true if the date is valid and matches the format, false otherwise
     */
    public boolean isValidDate(String dateStr) {
        // First check the format using regex
        String datePattern = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/([12][0-9]{3})$";
        if (!Pattern.matches(datePattern, dateStr)) {
            return false;
        }

        // Then verify it's a valid date using SimpleDateFormat
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        sdf.setLenient(false);  // This will make the date parser strict
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Method to validate the rating format
    public boolean isValidRating(String rating) {
        try {
            String[] parts = rating.split("/");
            if (parts.length == 2) {
                int num = Integer.parseInt(parts[0]);
                int denom = Integer.parseInt(parts[1]);
                return denom == 5 && num >= 1 && num <= 5;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }

    /**
     * Validates that checkout date is after checkin date
     * @param checkIn The check-in date string
     * @param checkOut The check-out date string
     * @return true if checkout is after checkin, false otherwise
     */
    public boolean isValidDateRange(String checkIn, String checkOut) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date checkInDate = sdf.parse(checkIn);
            Date checkOutDate = sdf.parse(checkOut);
            return checkOutDate.after(checkInDate);
        } catch (ParseException e) {
            return false;
        }
    }



    /**
     * Define a callback interface for asynchronous data retrieval
     */
    public interface AccommodationsCallback {
        /**
         * Callback method to handle retrieved destinations.
         *
         * @param accommodations the list of retrieved Destination objects
         */
        void onCallback(ArrayList<Accommodation> accommodations);
    }

    public interface CompletionCallback {
        void onComplete();
    }
}