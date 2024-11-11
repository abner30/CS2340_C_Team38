package com.example.myproject.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Sorting strategy that sorts Dining objects by time within the same date.
 * Sorts in ascending order based on the time field.
 */
public class SortByTime implements SortingStrategy<Dining> {

    /**
     * Sorts a list of Dining objects by time in ascending order.
     *
     * @param dinings The list of Dining objects to be sorted.
     */
    @Override
    public void sort(ArrayList<Dining> dinings) {
        Collections.sort(dinings, (d1, d2) -> {
            LocalTime time1 = LocalTime.parse(d1.getTime());
            LocalTime time2 = LocalTime.parse(d2.getTime());
            return time1.compareTo(time2);
        });
    }
}
