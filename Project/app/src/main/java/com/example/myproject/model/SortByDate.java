package com.example.myproject.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Sorting strategy that sorts items by date.
 * Can be used with both Accommodation and Dining objects, sorting them in ascending order by date.
 *
 * @param <T> the type of objects that this strategy can sort
 */
public class SortByDate<T> implements SortingStrategy<T> {

    /**
     * Sorts a list of items by date in ascending order.
     * If the item is an instance of Dining, it uses the date field;
     * if the item is an instance of Accommodation, it uses the check-out date.
     *
     * @param list The list of items to be sorted.
     * @throws RuntimeException if there is an error parsing dates in the list.
     */
    @Override
    public void sort(ArrayList<T> list) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        
        Collections.sort(list, (obj1, obj2) -> {
            try {
                String date1 = (obj1 instanceof Dining) ? ((Dining) obj1).getDate() : ((Accommodation) obj1).getCheckOut();
                String date2 = (obj2 instanceof Dining) ? ((Dining) obj2).getDate() : ((Accommodation) obj2).getCheckOut();
                
                return sdf.parse(date1).compareTo(sdf.parse(date2));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
