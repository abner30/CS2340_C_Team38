package com.example.myproject.model;

import java.util.ArrayList;

/**
 * Interface for sorting strategies used with objects such as Accommodation and Dining.
 * Defines a method for sorting a list of items based on specific criteria.
 *
 * @param <T> the type of objects that this strategy can sort
 */
public interface SortingStrategy<T> {
    
    /**
     * Sorts a list of items based on a specific criterion.
     *
     * @param list The list of items to be sorted.
     */
    void sort(ArrayList<T> list);
}
