package com.example.myproject.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.myproject.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.myproject.view.fragments.LogisticsFragment;
import com.example.myproject.view.fragments.DestinationFragment;
import com.example.myproject.view.fragments.DiningFragment;
import com.example.myproject.view.fragments.AccommodationsFragment;
import com.example.myproject.view.fragments.TravelCommunityFragment;

public class NavBar extends AppCompatActivity {

    /**
     * This method runs on create.
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_bar);  // Make sure this layout file exists

        BottomNavigationView bottomNavigationView = findViewById(
                R.id.bottom_navigation_view);

        // Load the default fragment on startup
        if (savedInstanceState == null) {
            loadFragment(new LogisticsFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.logistics:
                    selectedFragment = new LogisticsFragment();
                    break;
                case R.id.destination:
                    selectedFragment = new DestinationFragment();
                    break;
                case R.id.dining:
                    selectedFragment = new DiningFragment();
                    break;
                case R.id.accommodations:
                    selectedFragment = new AccommodationsFragment();
                    break;
                case R.id.travelCommunity:
                    selectedFragment = new TravelCommunityFragment();
                    break;
                default:
                    return false;
            }
            return loadFragment(selectedFragment);
        });
    }

    /**
     * Helper method to load the selected fragment into the container.
     * @param fragment the fragment to be loaded
     * @return true if fragment was loaded successfully, false otherwise
     */
    private boolean loadFragment(final Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_frame_layout, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
