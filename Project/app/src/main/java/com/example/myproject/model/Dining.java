package com.example.myproject.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Dining {
    private String location;
    private String website;
    private String date;
    private String time;
    private boolean expired;


    public Dining(String location, String website, String time, String date) {
        this.location = location;
        this.website = website;
        this.time = time;
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
            firstDate = sdf.parse(getDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        ZoneId zonedId = ZoneId.of( "America/Georgia" );
        LocalDate today = LocalDate.now(zonedId);
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
        } else if (diff == 0) {
            LocalTime time1 = LocalTime.now(zonedId);
            LocalTime time2 = LocalTime.parse(getTime());
            if (time1.compareTo(time2) < 0) {
                this.expired = false;
            } else {
                this.expired = true;
            }
        } else {
            this.expired = false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isGreater(Dining a) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Date firstDate;
        Date secondDate;
        try {
            firstDate = sdf.parse(this.getDate());
            secondDate = sdf.parse(a.getDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        long diffInMillies = firstDate.getTime() - secondDate.getTime();
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diff > 0) {
            return true;
        } else if (diff == 0) {
            LocalTime time1 = LocalTime.parse(getTime());
            LocalTime time2 = LocalTime.parse(a.getTime());
            if (time1.compareTo(time2) < 0) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}
