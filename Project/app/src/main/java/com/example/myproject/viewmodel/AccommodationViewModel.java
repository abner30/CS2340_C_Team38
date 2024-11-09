package com.example.myproject.viewmodel;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.example.myproject.database.DatabaseManager;
import com.example.myproject.model.Accommodation;
import com.example.myproject.model.Destination;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AccommodationViewModel extends ViewModel {
    /**
     * reference to database. Initiates once.
     */
    private DatabaseReference database = DatabaseManager.getInstance().getReference();

    /**
     * No args constructor.
     */
    public AccommodationViewModel() {
    }

    public interface CompletionCallback {
        /**
         * Called when the operation is complete.
         */
        void onComplete();
    }

    public void addAccommodation(Accommodation accommodation, String uid, DestinationViewModel.CompletionCallback callback) {
        database.child("accommodations").child("counter")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String lodging = "lodging";
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
                        map.put("check-in",accommodation.getCheckIn());
                        map.put("check-out", accommodation.getCheckOut());
                        map.put("type", accommodation.getType());
                        map.put("rooms", accommodation.getRooms());
                        map.put("user", uid);
                        database.child("accommodations").child(lodging).setValue(map)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        callback.onComplete(); // Call onComplete when the data is saved
                                    } else {
                                        // Handle failure, e.g., log error or show a message to the user
                                    }
                                });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
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

    public void getAccommodations(String uid, AccommodationViewModel.AccommodationsCallback callback) {
        ArrayList<Accommodation> list = new ArrayList<>();
        database.child("accommodations").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String user = userSnapshot.child("user").getValue(String.class);
                    if (user != null && user.equals(uid)) {
                        String location = userSnapshot.child("location").getValue(String.class);
                        String checkIn = userSnapshot.child("check-in").getValue(String.class);
                        String checkOut = userSnapshot.child("check-out").getValue(String.class);
                        String type = userSnapshot.child("type").getValue(String.class);
                        Integer rooms = userSnapshot.child("rooms").getValue(Integer.class);
                        list.add(new Accommodation(checkIn, checkOut, location, rooms, type));
                    }

                    //sort using helper merge sort
                    sortAccommodation(list, 0, list.size() - 1);

                    //Check if each accommodation is expired.
                    for (Accommodation a : list) {
                        a.setExpired();
                    }
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

    private void mergeAccommodation(ArrayList<Accommodation> a, int l, int m , int r) {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        // Create temp arrays
        ArrayList<Accommodation> a1 = new ArrayList<Accommodation>();
        ArrayList<Accommodation> a2 = new ArrayList<Accommodation>();

        // Copy data to temp arrays
        for (int i = 0; i < n1; ++i)
            a1.set(i, a.get(l+i));
        for (int j = 0; j < n2; ++j)
            a2.set(j, a.get(m + 1 + j));

        // Merge the temp arrays

        // Initial indices of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarray array
        int k = l;
        while (i < n1 && j < n2) {
            if (a1.get(i).isGreater(a2.get(j))) {
                a.set(k, a1.get(i));
                i++;
            }
            else {
                a.set(k, a2.get(j));
                j++;
            }
            k++;
        }

        // Copy remaining elements of L[] if any
        while (i < n1) {
            a.set(k, a1.get(i));
            i++;
            k++;
        }

        // Copy remaining elements of R[] if any
        while (j < n2) {
            a.set(k, a2.get(j));
            j++;
            k++;
        }
    }

    private void sortAccommodation(ArrayList<Accommodation> a, int l, int r)
    {
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

}
