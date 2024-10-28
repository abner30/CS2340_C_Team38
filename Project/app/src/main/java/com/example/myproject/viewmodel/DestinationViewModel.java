package com.example.myproject.viewmodel;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.myproject.database.DatabaseManager;
import com.example.myproject.model.Destination;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public class DestinationViewModel extends ViewModel{
    /**
     * reference to database. Initiates once.
     */
    private DatabaseReference database = DatabaseManager.getInstance().getReference();

    /**
     * No args constructor.
     */
    public DestinationViewModel() {
    }


    /**
     * Adds a destination to database in following format:
     * destination(counter)
     *      "location" = String
     *      "start date" = String
     *      "end date" = String
     *      "uid" = String
     *
     * @param destination
     * @param uid
     */
    public void addDestination(Destination destination, String uid) {
        database.child("destinations").child("counter")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String location = "location";
                        if (dataSnapshot.exists()) {
                            Integer firebaseCounter = dataSnapshot.getValue(Integer.class);
                            if (firebaseCounter != null) {
                                location += firebaseCounter;
                                database.child("destinations").child("counter")
                                        .setValue(firebaseCounter + 1);
                            }
                        } else {
                            database.child("destinations").child("counter")
                                    .setValue(1);
                            location = "location1";
                        }
                        String finalLocation = location;
                        database.child("users").child(uid).child("destinationCounter")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        int counter = 0;
                                        if (dataSnapshot.exists()) {
                                            counter = dataSnapshot.getValue(Integer.class);
                                            database.child("users").child(uid).child("destinationCounter")
                                                    .setValue(counter + 1);
                                        } else {
                                            database.child("users").child(uid).child("destinationCounter")
                                                    .setValue(0);
                                        }
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("location", destination.getLocation());
                                        map.put("start date", destination.getStartDate());
                                        map.put("end date", destination.getEndDate());
                                        map.put("user", uid);
                                        map.put("destinationCounter", counter);
                                        database.child("destinations").child(finalLocation).setValue(map);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    // Define a callback interface for asynchronous data retrieval
    public interface DestinationsCallback {
        void onCallback(ArrayList<Destination> destinations);
    }

// Modify getDestinations to use a callback
    /**
     * Asynchronously fetches all destinations associated with a given user and passes them to a callback.
     * @param uid User ID
     * @param callback Callback to handle the list of destinations after retrieval
     */
    public void getDestinations(String uid, DestinationsCallback callback) {
        ArrayList<Destination> list = new ArrayList<>();
        database.child("destinations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String user = userSnapshot.child("user").getValue(String.class);
                    if (user != null && user.equals(uid)) {
                        String location = userSnapshot.child("location").getValue(String.class);
                        String startDate = userSnapshot.child("start date").getValue(String.class); // Ensure field names match
                        String endDate = userSnapshot.child("end date").getValue(String.class);    // Ensure field names match
                        Integer counter = userSnapshot.child("destinationCounter").getValue(Integer.class);
                        list.add(new Destination(location, startDate, endDate, counter));
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

    /**
     * Find the five most recent destinations out of all destinations.
     * @param destinations
     * @return arraylist of five destinations.
     */
    public ArrayList<Destination> getRecentDestinations(ArrayList<Destination> destinations) {

        if (destinations.size() < 5) {
            while(destinations.size() < 5) {
                destinations.add(new Destination("Default",
                        "01/01/2024", "01/01/2024", 1));
            }
            return destinations;
        }
        ArrayList<Destination> output = new ArrayList<Destination>();
        for (Destination destination: destinations) {
            if (destination.getDestinationCounter() >= destinations.size() - 5) {
                output.add(destination);
            }
            if (output.size() == 5) {
                return output;
            }
        }
        return output;
    }

    /**
     * Calculate total days of five recent destinations.
     * @param destinationArrayList
     * @return total days of five recent destinations.
     * @throws ParseException
     */
    public int calculateTotalDays(ArrayList<Destination> destinationArrayList) throws ParseException {;
        int sum = 0;
        UserViewModel userViewModel = new UserViewModel();
        for (Destination destination: destinationArrayList) {
            int curr = userViewModel.calculateDuration(destination.getStartDate(), destination.getEndDate());
            sum += curr;
        }
        return sum;
    }
}