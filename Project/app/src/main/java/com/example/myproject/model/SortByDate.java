package com.example.myproject.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class SortByDate implements SortingStrategy {
    @Override
    public void sort(ArrayList<Accommodation> accommodations) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        Collections.sort(accommodations, (a1, a2) -> {
            try {
                return sdf.parse(a1.getCheckOut()).compareTo(sdf.parse(a2.getCheckOut()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
