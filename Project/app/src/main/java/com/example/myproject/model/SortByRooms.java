package com.example.myproject.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Sorting strategy that sorts Accommodation objects by the number of rooms.
 * Sorts in ascending order based on the rooms field.
 */
public class SortByRooms implements SortingStrategy<Accommodation> {

    /**
     * Sorts a list of Accommodation objects by the number of rooms in ascending order.
     *
     * @param accommodations The list of Accommodation objects to be sorted.
     */
    @Override
    public void sort(ArrayList<Accommodation> accommodations) {
        Collections.sort(accommodations, Comparator.comparingInt(Accommodation::getRooms));
    }
}
