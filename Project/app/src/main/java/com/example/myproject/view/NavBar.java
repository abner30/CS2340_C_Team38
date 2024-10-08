package com.example.myproject.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.example.myproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;


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

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.logistics:
                    startActivity(new Intent(getApplicationContext(), LogisticsActivity.class));
                    return true;
                case R.id.destination:
                    startActivity(new Intent(getApplicationContext(), DestinationActivity.class));
                    return true;
                case R.id.dining:
                    startActivity(new Intent(getApplicationContext(), DiningActivity.class));
                    return true;
                case R.id.accommodations:
                    startActivity(new Intent(getApplicationContext(), AccommodationsActivity.class));
                    return true;
                case R.id.travelCommunity:
                    startActivity(new Intent(getApplicationContext(), TravelCommunityActivity.class));
                    return true;
            }
            return false;
        });
    }
}