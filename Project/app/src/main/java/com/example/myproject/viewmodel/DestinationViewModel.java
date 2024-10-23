package com.example.myproject.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.myproject.model.Destination;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DestinationViewModel extends ViewModel{
    /**
     * reference to database. Initiates once.
     */
    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    /**
     * Given two dates, calculate difference.
     * @param first date
     * @param second date
     * @return duraiton
     * @throws ParseException
     */
    public int calculateDuration(String first, String second) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date firstDate = sdf.parse(first);
        Date secondDate = sdf.parse(second);
        long diffInMillies = Math.abs(firstDate.getTime() - secondDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return (int) diff;
    }

    /**
     * Given start date, and duration, find end date.
     * @param startDate date
     * @param duration days
     * @return end date
     * @throws ParseException
     */
    public String calculateEnd(String startDate, int duration) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = sdf.parse(startDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, duration);
        String output = sdf.format(c.getTime());
        return output;
    }

    /**
     * Given end date, and duration, find start date.
     * @param endDate date
     * @param duration days
     * @return start date
     * @throws ParseException
     */
    public String calculateStart(String endDate, int duration) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = sdf.parse(endDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -duration);
        String output = sdf.format(c.getTime());
        return output;
    }

    /**
     * Adds a destination to database in following format:
     * destination(counter)
     *      "location" = String
     *      "start date" = String
     *      "end date" = String
     *      "duration" = int
     *      "uid" = String
     *
     * @param destination
     * @param uid
     */
    public void addDestination(Destination destination, String uid) {
        final String[] location = new String[1];
        database.child("destinations").child("counter").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Integer firebaseCounter = dataSnapshot.getValue(Integer.class);
                    if (firebaseCounter != null) {
                        location[0] = location[0] + firebaseCounter;
                        database.child("destinations").child("counter").setValue(firebaseCounter + 1);
                    }
                } else {
                    database.child("destinations").child("counter").setValue(1);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.child("destinations").setValue(location[0]);
        HashMap<String, Object> map = new HashMap<>();
        map.put("location", destination.getLocation());
        map.put("duration", destination.getDuration());
        map.put("start date", destination.getStartDate());
        map.put("end date", destination.getEndDate());
        map.put("user", uid);
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
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String user = userSnapshot.child("user").getValue(String.class);
                    if (user != null && user.equals(uid)) {
                        String location = userSnapshot.child("location").getValue(String.class);
                        String startDate = userSnapshot.child("start Date").getValue(String.class);
                        String endDate = userSnapshot.child("end Date").getValue(String.class);
                        Integer duration = userSnapshot.child("duration").getValue(Integer.class);
                        list.add( new Destination(location, duration, startDate, endDate));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return list;
    }
}
