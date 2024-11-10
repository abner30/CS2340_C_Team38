package com.example.myproject.model;

import java.util.ArrayList;

public interface SortingStrategy<T> {
    void sort(ArrayList<T> list);
}
