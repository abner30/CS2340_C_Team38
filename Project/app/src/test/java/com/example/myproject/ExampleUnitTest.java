package com.example.myproject;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.myproject.viewmodel.UserViewModel;

import java.text.ParseException;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void date_isCorrect() {
        UserViewModel userViewModel = new UserViewModel();
        try {
            String first = userViewModel.calculateEnd("01/06/2001", 5);
            String second = userViewModel.calculateStart("01/01/2001", 5);
            int third = userViewModel.calculateDuration("01/06/2001", "01/01/2001");
            System.out.println(first);
            System.out.println(second);
            System.out.println(third);
            assertEquals(first, "01/11/2001");
            assertEquals(second, "12/27/2000");
            assertEquals(third, 5);
        } catch (
                ParseException e) {
            System.out.println("Hi");
        }

    }
}