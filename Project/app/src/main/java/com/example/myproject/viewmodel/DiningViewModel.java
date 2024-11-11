package com.example.myproject.viewmodel;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.myproject.database.DatabaseManager;
import com.example.myproject.model.Dining;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DiningViewModel {
    /**
     * reference to database. Initiates once.
     */
    private DatabaseReference database = DatabaseManager.getInstance().getReference();

    /**
     * No args constructor.
     */
    public DiningViewModel() {
    }

    public void addDining(Dining dining, String uid, DiningViewModel.CompletionCallback callback) {
        database.child("dinings").child("counter")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String restaurant = "restaurant";
                        if (dataSnapshot.exists()) {
                            Integer firebaseCounter = dataSnapshot.getValue(Integer.class);
                            if (firebaseCounter != null) {
                                restaurant += firebaseCounter;
                                database.child("dinings").child("counter")
                                        .setValue(firebaseCounter + 1);
                            }
                        } else {
                            database.child("dinings").child("counter")
                                    .setValue(1);
                            restaurant = "dining1";
                        }
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("location", dining.getLocation());
                        map.put("website", dining.getWebsite());
                        map.put("time", dining.getTime());
                        map.put("date", dining.getDate());
                        map.put("user", uid);
                        database.child("dinings").child(restaurant).setValue(map)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        callback.onComplete();
                                    }
                                });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void mergeDining(ArrayList<Dining> a, int l, int m, int r) {
        // Create temp arrays
        ArrayList<Dining> left = new ArrayList<>();
        ArrayList<Dining> right = new ArrayList<>();

        // Copy data to temp arrays
        for (int i = l; i <= m; i++) {
            left.add(a.get(i));
        }
        for (int j = m + 1; j <= r; j++) {
            right.add(a.get(j));
        }

        int i = 0;
        int j = 0;
        int k = l;

        // Merge the temp arrays
        while (i < left.size() && j < right.size()) {
            if (left.get(i).isGreater(right.get(j))) {
                a.set(k, left.get(i));
                i++;
            } else {
                a.set(k, right.get(j));
                j++;
            }
            k++;
        }

        // Copy remaining elements of left array if any
        while (i < left.size()) {
            a.set(k, left.get(i));
            i++;
            k++;
        }

        // Copy remaining elements of right array if any
        while (j < right.size()) {
            a.set(k, right.get(j));
            j++;
            k++;
        }
    }

    private void sortDining(ArrayList<Dining> a, int l, int r) {
        // Don't try to sort if the list is empty or has only one element
        if (a == null || a.size() <= 1) {
            return;
        }

        if (l < r) {
            int m = l + (r - l) / 2;
            sortDining(a, l, m);
            sortDining(a, m + 1, r);
            mergeDining(a, l, m, r);
        }
    }

    public void getDining(String uid, DiningViewModel.DiningCallback callback) {
        ArrayList<Dining> list = new ArrayList<>();
        database.child("dinings").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    // Skip the counter node
                    if (userSnapshot.getKey().equals("counter")) {
                        continue;
                    }

                    String user = userSnapshot.child("user").getValue(String.class);
                    if (user != null && user.equals(uid)) {
                        String location = userSnapshot.child("location").getValue(String.class);
                        String website = userSnapshot.child("website").getValue(String.class);
                        String date = userSnapshot.child("date").getValue(String.class);
                        String time = userSnapshot.child("time").getValue(String.class);
                        list.add(new Dining(location, website, time, date));
                    }
                }

                // Only sort if there are items to sort
                if (!list.isEmpty()) {
                    sortDining(list, 0, list.size() - 1);
                }

                // Check if each dining is expired
                for (Dining d : list) {
                    d.setExpired();
                }

                // Pass the filled list to the callback
                callback.onCallback(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error if necessary
                callback.onCallback(new ArrayList<>()); // Return empty list on error
            }
        });
    }

    public interface CompletionCallback {
        /**
         * Called when the operation is complete.
         */
        void onComplete();
    }

    /**
     * Define a callback interface for asynchronous data retrieval
     */
    public interface DiningCallback {
        /**
         * Callback method to handle retrieved destinations.
         *
         * @param dinings the list of retrieved Destination objects
         */
        void onCallback(ArrayList<Dining> dinings);
    }
}
