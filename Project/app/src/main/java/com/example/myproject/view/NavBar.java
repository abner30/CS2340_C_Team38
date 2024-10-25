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
     * @param savedInstanceState
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

    // Helper method to load the selected fragment
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



/*
 public class NavBar extends AppCompatActivity {
@Override
public boolean onCreateOptionsMenu(Menu menu) {
getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
return true;
}

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.nav_bar);  // Make sure this layout file exists

final BottomNavigationView bottomNavigationView = findViewById(
R.id.bottom_navigation_view);

bottomNavigationView.setOnItemSelectedListener(item -> {
Log.d("itemId", String.format("%d", item.getItemId()));
switch (item.getItemId()) {
case R.id.logistics:
startActivity(new Intent(getApplicationContext(),
 LogisticsActivity.class));
return true;
case R.id.destination:
startActivity(new Intent(getApplicationContext(),
 DestinationActivity.class));
return true;
case R.id.dining:
startActivity(new Intent(getApplicationContext(),
 DiningActivity.class));
return true;
case R.id.accommodations:
startActivity(new Intent(getApplicationContext()
, AccommodationsActivity.class));
return true;
case R.id.travelCommunity:
startActivity(new Intent(getApplicationContext(),
 TravelCommunityActivity.class));
return true;
}
return false;
});
}
} */
