package com.example.myproject.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.myproject.model.Destination;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DestinationViewModel extends ViewModel{
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public int calculateDuration(String first, String second) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date firstDate = sdf.parse(first);
        Date secondDate = sdf.parse(second);
        long diffInMillies = Math.abs(firstDate.getTime() - secondDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return (int) diff;
    }

    public String calculateStart(String startDate, int duration) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = sdf.parse(startDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, duration);
        String output = sdf.format(c.getTime());
        return output;
    }

    public String calculateEnd(String endDate, int duration) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date date = sdf.parse(endDate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, -duration);
        String output = sdf.format(c.getTime());
        return output;
    }

    public void addDestination (Destination destination) {
        String location = destination.getLocation();
        database.child("destinations").child(location)
                .setValue(destination.getLocation());
        database.child("destinations").child(location)
                .child("start").setValue(destination.getStartDate());
        database.child("destinations").child(location)
                .child("end").setValue(destination.getEndDate());
        database.child("destinations").child(location)
                .child("duration").setValue(destination.getDuration());
    }

}
