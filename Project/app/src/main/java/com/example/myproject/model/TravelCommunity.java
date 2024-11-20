package com.example.myproject.model;

import static java.security.AccessController.getContext;

import android.widget.Toast;

import com.example.myproject.viewmodel.UserViewModel;

import java.text.ParseException;
import java.util.ArrayList;

public class TravelCommunity {

    private int duration;

    private String destination;

    private ArrayList<Dining> dinings;

    private ArrayList<Accommodation> accommodations;
    private String notes;

    public TravelCommunity(int duration, String destination,
                           ArrayList<Dining> dinings, ArrayList<Accommodation> accommodations,
                           String notes) {
        this.duration = duration;
        this.destination = destination;
        this.dinings = dinings;
        this.accommodations = accommodations;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public ArrayList<Dining> getDinings() {
        return dinings;
    }

    public void setDinings(ArrayList<Dining> dinings) {
        this.dinings = dinings;
    }

    public ArrayList<Accommodation> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(ArrayList<Accommodation> accommodations) {
        this.accommodations = accommodations;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
