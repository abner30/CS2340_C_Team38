package com.example.myproject.view.fragments;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.myproject.model.Accommodation;
import com.example.myproject.viewmodel.AccommodationViewModel;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myproject.R;
import com.example.myproject.database.DatabaseManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AccommodationsFragment extends Fragment {

    private AccommodationViewModel accommodationViewModel = new AccommodationViewModel();
    private DatabaseReference database;
    private DatabaseReference tripDatabase;
    private String currentUserUid;
    private String effectiveUserUid; // This will store either the current user's UID or the trip owner's UID
    private boolean isContributor = false;
    private String tripOwnerId;
    /**
     * This method runs on create.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null,
     *                           this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return
     */
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accommodations, container, false);
        currentUserUid = DatabaseManager.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance().getReference("tripData").child("contributors");
        tripDatabase = FirebaseDatabase.getInstance().getReference("tripData");

        // Set up invite
        FloatingActionButton accommodationButton = view.findViewById(R.id.btn_add_accommodation);

        determineUserRole(() -> {
            accommodationButton.setOnClickListener(v -> showAccommodationDialog());
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        determineUserRole(() -> {
        // Now call populateTable after view has been created
            populateTable();
        });
    }

    private interface UserRoleCallback {
        void onComplete();
    }
    private void determineUserRole(AccommodationsFragment.UserRoleCallback callback) {
        // First check if the user is a contributor to any trip
        DatabaseManager.getInstance().getReference().child("users")
                .child(currentUserUid)
                .child("sharedTrips")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // User is a contributor to someone's trip
                            for (DataSnapshot tripOwner : snapshot.getChildren()) {
                                tripOwnerId = tripOwner.getKey();
                                effectiveUserUid = tripOwnerId; // Use trip owner's UID for database operations
                                isContributor = true;
                                break;
                            }
                        } else {
                            // User is not a contributor, check if they're an owner
                            tripDatabase.child("owner").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot ownerSnapshot) {
                                    if (ownerSnapshot.exists()) {
                                        tripOwnerId = ownerSnapshot.getValue(String.class);
                                    } else {
                                        // If no owner is set, set current user as owner
                                        tripOwnerId = currentUserUid;
                                        tripDatabase.child("owner").setValue(currentUserUid);
                                    }
                                    effectiveUserUid = tripOwnerId;
                                    callback.onComplete();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getActivity(), "Error checking owner status: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    callback.onComplete();
                                }
                            });
                        }
                        callback.onComplete();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Error checking contributor status: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        callback.onComplete();
                    }
                });
    }


    private void showAccommodationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New Accommodation");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText checkInInput = new EditText(getActivity());
        checkInInput.setHint("check in MM/DD/YYYY");
        layout.addView(checkInInput);

        final EditText checkOutInput = new EditText(getActivity());
        checkOutInput.setHint("check out MM/DD/YYYY");
        layout.addView(checkOutInput);

        final EditText locationInput = new EditText(getActivity());
        locationInput.setHint("location");
        layout.addView(locationInput);

        // Add TextView for Number of Rooms
        TextView numRoomsLabel = new TextView(getActivity());
        numRoomsLabel.setText("Number of Rooms");
        numRoomsLabel.setTextSize(19);
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
        roomTypeLabel.setTextSize(19);
        layout.addView(roomTypeLabel);

        final Spinner roomTypeSpinner = new Spinner(getActivity());
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.room_types,
                android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomTypeSpinner.setAdapter(typeAdapter);
        layout.addView(roomTypeSpinner);

        builder.setView(layout);

        builder.setPositiveButton("Add Accommodation", (dialog, which) -> {
            String checkIn = checkInInput.getText().toString().trim();
            String checkOut = checkOutInput.getText().toString().trim();
            String location = locationInput.getText().toString().trim();
            String numRoomsString = numRoomsSpinner.getSelectedItem().toString();
            int numRooms = Integer.parseInt(numRoomsString);
            String roomType = roomTypeSpinner.getSelectedItem().toString();
            addAccommodation(checkIn, checkOut, location, numRooms, roomType);
        });

        builder.show();
    }

    public void addAccommodation(String checkIn, String checkOut, String location, int numRooms, String roomType) {
        // Validate inputs
        if (checkIn.isEmpty() || checkOut.isEmpty() || location.isEmpty() || numRooms <= 0 || roomType.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        //String uid = DatabaseManager.getInstance().getCurrentUser().getUid();
        Accommodation accommodation = new Accommodation(checkIn, checkOut, location, numRooms, roomType);
        accommodationViewModel.addAccommodation(accommodation, effectiveUserUid, new AccommodationViewModel.CompletionCallback() {
            @Override
            public void onComplete() {
                Toast.makeText(getContext(), "Accommodation added successfully", Toast.LENGTH_SHORT).show();
                // Only add the newly created accommodation to the table
                addRowToTable(accommodation);
            }
        });
    }



    public void populateTable() {
        //String uid = DatabaseManager.getInstance().getCurrentUser().getUid();
        LinearLayout accommodationsList = getView().findViewById(R.id.accommodations_list);
        //accommodationsList.removeAllViews();

        accommodationViewModel.getAccommodations(effectiveUserUid, new AccommodationViewModel.AccommodationsCallback() {
            @Override
            public void onCallback(ArrayList<Accommodation> accommodations) {
                if (accommodationsList == null) {
                    return;
                }
                for (Accommodation accommodation : accommodations) {
                    addRowToTable(accommodation);
                }
            }
        });
    }

    public void addRowToTable(Accommodation accommodation) {
        LinearLayout accommodationsList = getView().findViewById(R.id.accommodations_list);

        // Create a new layout for each accommodation entry
        LinearLayout accommodationLayout = new LinearLayout(getContext());
        accommodationLayout.setOrientation(LinearLayout.VERTICAL);
        accommodationLayout.setPadding(16, 16, 16, 16);

        // Location
        TextView locationView = new TextView(getContext());
        locationView.setText("Location: " + accommodation.getLocation());
        locationView.setTextSize(16);
        locationView.setTypeface(null, Typeface.BOLD);
        accommodationLayout.addView(locationView);

        // Check-in and Check-out Dates
        TextView checkInOutView = new TextView(getContext());
        checkInOutView.setText("Check-in: " + accommodation.getCheckIn() + ", Check-out: " + accommodation.getCheckOut());
        accommodationLayout.addView(checkInOutView);

        // Number of Rooms
        TextView roomsView = new TextView(getContext());
        roomsView.setText("Number of Rooms: " + accommodation.getRooms());
        roomsView.setTypeface(null, Typeface.ITALIC);
        accommodationLayout.addView(roomsView);

        // Room Type
        TextView roomTypeView = new TextView(getContext());
        roomTypeView.setText("Room Type: " + accommodation.getType());
        roomTypeView.setPadding(8, 4, 8, 4);
        accommodationLayout.addView(roomTypeView);

        // Add the individual accommodation layout to the main accommodations list
        accommodationsList.addView(accommodationLayout);
    }

}
