package com.example.myproject.view.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.myproject.databinding.ActivityNavBarBinding;

import com.example.myproject.R;

public class NavBar extends AppCompatActivity {

    ActivityNavBarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    private void replaceFragment (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}