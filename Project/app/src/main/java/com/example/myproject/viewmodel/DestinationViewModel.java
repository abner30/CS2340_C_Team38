package com.example.myproject.viewmodel;

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

        String[] location = new String[1];
        location[0] = "location";
        database.child("destinations").child("counter")
                .addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Integer firebaseCounter = dataSnapshot.getValue(Integer.class);
                    if (firebaseCounter != null) {
                        location[0] = "location" + firebaseCounter;
                        database.child("destinations").child("counter")
                                .setValue(firebaseCounter + 1);
                    } else {
                        database.child("destinations").child("counter")
                                .setValue(1);
                        location[0] = "location1";
                    }
                } else {
                    database.child("destinations").child("counter")
                            .setValue(1);
                    location[0] = "location1";
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        int[] counter = new int[1];
        counter[0] = 0;
        database.child("users").child(uid).child("destinationCounter")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uid = DatabaseManager.getInstance().getCurrentUser().getUid();
                if (dataSnapshot.exists()) {
                    counter[0] = dataSnapshot.getValue(Integer.class);
                    database.child("users").child(uid).child("destinationCounter")
                            .setValue(counter[0] + 1);
                } else {
                    database.child("users").child(uid).child("destinationCounter")
                                           .setValue(0);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.child("destinations").setValue(location[0]);
        HashMap<String, Object> map = new HashMap<>();
        map.put("location", destination.getLocation());
        map.put("start date", destination.getStartDate());
        map.put("end date", destination.getEndDate());
        map.put("user", uid);
        map.put("destinationCounter", counter[0]);
        database.child("destinations").child(location[0]).setValue(map);
    }

    /**
     * Returns an ArrayList of all destinations associate with a given user.
     * @param uid
     * @return list of destinations
     */
    public ArrayList<Destination> getDestinations(String uid) {
        ArrayList<Destination> list = new ArrayList<>();
        database.child("destinations").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String user = userSnapshot.child("user").getValue(String.class);
                    if (user != null && user.equals(uid)) {
                        String location = userSnapshot.child("location").getValue(String.class);
                        String startDate = userSnapshot.child("start Date").getValue(String.class);
                        String endDate = userSnapshot.child("end Date").getValue(String.class);
                        Integer counter = userSnapshot.child("destinationCounter").getValue(Integer.class);
                        list.add(new Destination(location, startDate, endDate, counter));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return list;
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
     * @param uid
     * @return total days of five recent destinations.
     * @throws ParseException
     */
    public int calculateTotalDays(String uid) throws ParseException {
        ArrayList<Destination> destinationArrayList = getRecentDestinations(getDestinations(uid));
        int sum = 0;
        UserViewModel userViewModel = new UserViewModel();
        for (Destination destination: destinationArrayList) {
            int curr = userViewModel.calculateDuration(destination.getStartDate(), destination.getEndDate());
            sum += curr;
        }
        return sum;
    }
}
