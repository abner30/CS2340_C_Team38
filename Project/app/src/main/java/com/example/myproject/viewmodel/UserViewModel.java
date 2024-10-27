package com.example.myproject.viewmodel;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.myproject.database.DatabaseManager;
import com.example.myproject.model.Destination;
import com.example.myproject.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class UserViewModel {
    /**
     * reference to database. Initiates once.
     */
    private DatabaseReference database = DatabaseManager.getInstance().getReference();

    /**
     * No args constructor.
     */
    public UserViewModel() {
    }

    /**
     * Adds a user to user database.
     * @param user
     */
    public void addUser(User user) {
        database.child("users").child(user.getUid()).setValue(user);
    }


    /**
     * Adds trip to user.
     * @param uid
     * @param duration
     * @param startDate
     * @param endDate
     */
    public void addTrip(String uid, int duration, String startDate, String endDate ) {
        database.child("users").child(uid).child("duration")
                .setValue(duration);
        database.child("users").child(uid).child("start date")
                .setValue(startDate);
        database.child("users").child(uid).child("end date")
                .setValue(endDate);
    }


    /**
     * Given two dates, calculate difference.
     * @param first date
     * @param second date
     * @return duration
     * @throws ParseException
     */
    public int calculateDuration(String first, String second) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/DD/YYYY", Locale.ENGLISH);
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
        SimpleDateFormat sdf = new SimpleDateFormat("MM/DD/YYYY");
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
        SimpleDateFormat sdf = new SimpleDateFormat("MM/DD/YYYY");
        Date date = sdf.parse(endDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -duration);
        String output = sdf.format(c.getTime());
        return output;
    }

}
