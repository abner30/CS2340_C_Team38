package com.example.myproject.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Accommodation {
    private String checkIn;
    private String checkOut;
    private String location;
    private int rooms;
    private String type;
    private boolean expired;
    public Accommodation(String checkIn, String checkOut, String location, int rooms, String type) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.location = location;
        this.rooms = rooms;
        this.type = type;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isExpired() {
        return expired;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setExpired() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        Date firstDate = null;
        try {
            firstDate = sdf.parse(getCheckOut());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        ZoneId zonedId = ZoneId.of( "-05:00" );
        LocalDate today = LocalDate.now( zonedId );
        Date secondDate = null;
        try {
            secondDate = sdf2.parse(today.toString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long diffInMillies = firstDate.getTime() - secondDate.getTime();
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diff < 0) {
            this.expired = true;
        } else {
            this.expired = false;
        }
    }
    public boolean isGreater(Accommodation a) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date firstDate;
        Date secondDate;
        try {
            firstDate = sdf.parse(this.getCheckOut());
            secondDate = sdf.parse(a.getCheckOut());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long diffInMillies = firstDate.getTime() - secondDate.getTime();
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diff > 0) {
            return true;
        }
        return false;
    }
}
