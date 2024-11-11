package com.example.myproject.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class SortByDate<T> implements SortingStrategy<T> {

    @Override
    public void sort(ArrayList<T> list) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

        Collections.sort(list, (obj1, obj2) -> {
            try {
                String date1 = (obj1 instanceof Dining) ? ((Dining) obj1).getDate()
                        : ((Accommodation) obj1).getCheckOut();
                String date2 = (obj2 instanceof Dining) ? ((Dining) obj2).getDate()
                        : ((Accommodation) obj2).getCheckOut();
                return sdf.parse(date1).compareTo(sdf.parse(date2));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
