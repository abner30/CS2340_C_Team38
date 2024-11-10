package com.example.myproject.model;

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

    public void setExpired() {
        //implementation
    }

    public boolean isGreater(Dining a) {
        //implementation
        return true;
    }
}
