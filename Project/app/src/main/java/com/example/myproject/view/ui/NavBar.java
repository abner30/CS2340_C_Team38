package com.example.myproject.view.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myproject.databinding.ActivityNavBarBinding;

import com.example.myproject.R;

public class NavBar extends AppCompatActivity {

    /**
     * binding completes navigation bar binding.
     */
    private ActivityNavBarBinding binding;

    /**
     * The methods run on create.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new LogisticsFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.logistics) {
                replaceFragment(new LogisticsFragment());
            } else if (itemId == R.id.destination) {
                replaceFragment(new DestinationFragment());
            } else if (itemId == R.id.diningEstablishments) {
                replaceFragment(new DiningEstablishmentsFragment());
            } else if (itemId == R.id.accommodations) {
                replaceFragment(new AccommodationsFragment());
            } else if (itemId == R.id.travelCommunity) {
                replaceFragment(new TravelCommunityFragment());
            }

            return true;
        });
    }

    /**
     * The method deals with fragment, combine them into nav bar.
     * @param fragment
     */
    private void replaceFragment(final Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}
