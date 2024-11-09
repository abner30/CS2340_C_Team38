package com.example.myproject.view.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myproject.R;
import com.example.myproject.database.DatabaseManager;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AccommodationsFragment extends Fragment {
    private DatabaseReference database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accommodations, container, false);

        // Initialize Firebase reference
        database = FirebaseDatabase.getInstance().getReference("accommodations");

        // Set up invite
        FloatingActionButton inviteButton = view.findViewById(R.id.btn_add_reservation);
        inviteButton.setOnClickListener(v -> showAccommodationDialog());

        return view;
    }

    private void showAccommodationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New Accommodation");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText checkInInput = new EditText(getActivity());
        checkInInput.setHint("check in time");
        layout.addView(checkInInput);

        final EditText checkOutInput = new EditText(getActivity());
        checkOutInput.setHint("check out time");
        layout.addView(checkOutInput);

        final EditText locationInput = new EditText(getActivity());
        locationInput.setHint("location");
        layout.addView(locationInput);

        // Add TextView for Number of Rooms
        TextView numRoomsLabel = new TextView(getActivity());
        numRoomsLabel.setText("Number of Rooms");
        numRoomsLabel.setTextSize(20);
        layout.addView(numRoomsLabel);

        final Spinner numRoomsSpinner = new Spinner(getActivity());
        ArrayAdapter<CharSequence> numAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.num_of_rooms, android.R.layout.simple_spinner_item);
        numAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numRoomsSpinner.setAdapter(numAdapter);
        layout.addView(numRoomsSpinner);

        // Add TextView for Room Type
        TextView roomTypeLabel = new TextView(getActivity());
        roomTypeLabel.setText("Room Type");
        roomTypeLabel.setTextSize(20);
        layout.addView(roomTypeLabel);

        final Spinner roomTypeSpinner = new Spinner(getActivity());
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.room_types, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomTypeSpinner.setAdapter(typeAdapter);
        layout.addView(roomTypeSpinner);

        builder.setView(layout);

        builder.setPositiveButton("Add Accommodation", (dialog, which) -> {
            String checkIn = checkInInput.getText().toString().trim();
            String checkOut = checkOutInput.getText().toString().trim();
            String location = locationInput.getText().toString().trim();
            String numRooms = numRoomsSpinner.getSelectedItem().toString();
            String roomType = roomTypeSpinner.getSelectedItem().toString();
            addAccommodation(checkIn, checkOut, location, numRooms, roomType);
        });

        builder.show();
    }

    private void addAccommodation(String checkIn, String checkOut, String location, String numRooms, String roomType) {
        if (checkIn.isEmpty()) {
            Toast.makeText(getActivity(), "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("checkin", checkIn);
        userMap.put("checkout", checkOut);
        userMap.put("location", location);
        userMap.put("number of rooms", numRooms);
        userMap.put("room type", roomType);
        userMap.put("uid", DatabaseManager.getInstance().getCurrentUser().getUid());

        database.child(checkIn).setValue(userMap)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(getActivity(), "User " + checkIn + " invited!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getActivity(), "Failed to invite user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}
