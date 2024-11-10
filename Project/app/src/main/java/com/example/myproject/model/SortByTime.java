package com.example.myproject.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

public class SortByTime implements SortingStrategy<Dining> {

    @Override
    public void sort(ArrayList<Dining> dinings) {
        Collections.sort(dinings, (d1, d2) -> {
            LocalTime time1 = LocalTime.parse(d1.getTime());
            LocalTime time2 = LocalTime.parse(d2.getTime());
            return time1.compareTo(time2);
        });
    }
}
