package com.example.myproject.view.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myproject.model.Dining;
import com.example.myproject.viewmodel.DiningViewModel;
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

public class DiningFragment extends Fragment {

    //private DiningViewModel diningViewModel = new DiningViewModel();
    private DiningViewModel diningViewModel = new DiningViewModel();
    private DatabaseReference database;
    private DatabaseReference tripDatabase;
    private String currentUserUid;
    private String effectiveUserUid;
    private boolean isContributor = false;
    private String tripOwnerId;
    /**
     * This method runs on create.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's UI
     *                           should be attached to. The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null,
     *                           this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return view
     */
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dining, container, false);
        currentUserUid = DatabaseManager.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance().getReference("tripData").child("contributors");
        tripDatabase = FirebaseDatabase.getInstance().getReference("tripData");

        determineUserRole(() -> {
            FloatingActionButton diningButton = view.findViewById(R.id.btn_add_dining);
            if (tripOwnerId != null && tripOwnerId.equals(currentUserUid)) {
                diningButton.setVisibility(View.VISIBLE);
            } else if (isContributor) {
                // Show buttons for contributors but not the trip owner
                diningButton.setVisibility(View.VISIBLE);
            } else {
                // Hide buttons for non-contributors
                diningButton.setVisibility(View.GONE);
            }
            diningButton.setOnClickListener(v -> showDiningDialog());
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        determineUserRole(() -> {
            populateTable();
        });
    }

    private void determineUserRole(DiningFragment.UserRoleCallback callback) {
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
                                effectiveUserUid = tripOwnerId;
                                isContributor = true;
                                break;
                            }
                        } else {
                            // User is not a contributor, check if they're an owner
                            tripDatabase.child("owner").addListenerForSingleValueEvent(
                                    new ValueEventListener() {
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
                                        Toast.makeText(getActivity(),
                                                "Error checking owner status: "
                                                        + error.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        callback.onComplete();
                                    }
                                });
                            }
                            callback.onComplete();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Error checking contributor status: "
                                + error.getMessage(), Toast.LENGTH_SHORT).show();
                        callback.onComplete();
                    }
                });
    }

    private void showDiningDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New Dining");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText locationInput = new EditText(getActivity());
        locationInput.setHint("location");
        layout.addView(locationInput);

        final EditText websiteInput = new EditText(getActivity());
        websiteInput.setHint("website");
        layout.addView(websiteInput);

        final EditText timeInput = new EditText(getActivity());
        timeInput.setHint("time HH:MM");
        layout.addView(timeInput);

        final EditText dateInput = new EditText(getActivity());
        dateInput.setHint("date MM/DD/YYYY");
        layout.addView(dateInput);

        builder.setView(layout);

        builder.setPositiveButton("Add Dining", (dialog, which) -> {
            String location = locationInput.getText().toString().trim();
            String website = websiteInput.getText().toString().trim();
            String time = timeInput.getText().toString().trim();
            String date = dateInput.getText().toString().trim();
            addDining(location, website, time, date);
        });

        builder.show();
    }

    public void addDining(String location, String website, String time, String date) {
        // Validate inputs
        if (date.isEmpty() || time.isEmpty() || location.isEmpty() || website.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = DatabaseManager.getInstance().getCurrentUser().getUid();
        Dining dining = new Dining(location, website, time, date);
        diningViewModel.addDining(dining, effectiveUserUid,
                new DiningViewModel.CompletionCallback() {
                @Override
                public void onComplete() {
                    Toast.makeText(getContext(), "Dining added successfully",
                            Toast.LENGTH_SHORT).show();
                    addRowToTable(dining);
                }
            });
    }

    public void populateTable() {
        String uid = DatabaseManager.getInstance().getCurrentUser().getUid();
        LinearLayout diningList = getView().findViewById(R.id.dining_list);
        diningList.removeAllViews();

        diningViewModel.getDining(effectiveUserUid, new DiningViewModel.DiningCallback() {
            @Override
            public void onCallback(ArrayList<Dining> dinings) {
                if (diningList == null) {
                    return;
                }
                for (Dining dining : dinings) {
                    addRowToTable(dining);
                }
            }
        });
    }

    public void addRowToTable(Dining dining) {
        LinearLayout diningList = getView().findViewById(R.id.dining_list);

        // Create a new layout for each dining entry
        LinearLayout diningLayout = new LinearLayout(getContext());
        diningLayout.setOrientation(LinearLayout.VERTICAL);
        diningLayout.setPadding(16, 16, 16, 16);

        // Location
        TextView locationView = new TextView(getContext());
        locationView.setText("Location: " + dining.getWebsite());
        diningLayout.addView(locationView);

        // Website
        TextView websiteView = new TextView(getContext());
        websiteView.setText("Website: " + dining.getWebsite());
        diningLayout.addView(websiteView);

        // Time
        TextView timeView = new TextView(getContext());
        timeView.setText("Time: " + dining.getTime());
        diningLayout.addView(timeView);

        // Date
        TextView date = new TextView(getContext());
        date.setText("Date: " + dining.getDate());
        diningLayout.addView(date);

        if (dining.isExpired()) {
            diningLayout.setBackgroundColor(Color.RED);
        }

        // Add the individual dining layout to the main dinings list
        diningList.addView(diningLayout);
    }

    private interface UserRoleCallback {
        void onComplete();
    }

}
