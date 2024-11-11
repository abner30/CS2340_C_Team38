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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

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

    public interface CompletionCallback {
        /**
         * Called when the operation is complete.
         */
        void onComplete();
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
                        database.child("accommodations").child(restaurant).setValue(map)
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

    public void getDining(String uid, DiningViewModel.DiningCallback callback) {
        ArrayList<Dining> list = new ArrayList<>();
        database.child("dinings").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String user = userSnapshot.child("user").getValue(String.class);
                    if (user != null && user.equals(uid)) {
                        String location = userSnapshot.child("location").getValue(String.class);
                        String website = userSnapshot.child("website").getValue(String.class);
                        String date = userSnapshot.child("date").getValue(String.class);
                        String time = userSnapshot.child("time").getValue(String.class);
                        list.add(new Dining(location, website, date, time));
                    }


                }
                //sort using helper merge sort
                sortDining(list, 0, list.size() - 1);

                //Check if each accommodation is expired.
                for (Dining a : list) {
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

    /**
     * Validates if a time string matches the HH:MM format (24-hour)
     * @param timeStr The time string to validate
     * @return true if the time matches HH:MM format, false otherwise
     */
    public boolean isValidTime(String timeStr) {
        // Check format using regex for HH:MM (24-hour format)
        String timePattern = "^([01][0-9]|2[0-3]):([0-5][0-9])$";
        return Pattern.matches(timePattern, timeStr);
    }

    /**
     * Extracts time portion from datetime string
     * @param dateTimeStr The full datetime string
     * @return The time portion or empty string if no time found
     */
    private String extractTimeFromDateTime(String dateTimeStr) {
        String[] parts = dateTimeStr.split(" ");
        return parts.length > 1 ? parts[1] : "";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void mergeDining(ArrayList<Dining> a, int l, int m , int r) {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        // Create temp arrays
        ArrayList<Dining> a1 = new ArrayList<Dining>();
        ArrayList<Dining> a2 = new ArrayList<Dining>();

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sortDining(ArrayList<Dining> a, int l, int r)
    {
        if (l < r) {

            // Find the middle point
            int m = l + (r - l) / 2;

            // Sort first and second halves
            sortDining(a, l, m);
            sortDining(a, m + 1, r);

            // Merge the sorted halves
            mergeDining(a, l, m, r);
        }
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
