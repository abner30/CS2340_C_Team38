package com.example.myproject.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SortByRooms implements SortingStrategy {
    @Override
    public void sort(ArrayList<Accommodation> accommodations) {
        Collections.sort(accommodations, Comparator.comparingInt(Accommodation::getRooms));
    }
}
