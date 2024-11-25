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

/**
 * Fragment for managing dining reservations within a trip.
 * Provides functionality for contributors and trip owners to view, add, and manage dining entries.
 */
public class DiningFragment extends Fragment {

    private DiningViewModel diningViewModel = new DiningViewModel();
    private DatabaseReference database;
    private DatabaseReference tripDatabase;
    private String currentUserUid;
    private String effectiveUserUid;
    private boolean isContributor = false;
    private String tripOwnerId;

    /**
     * Called to create the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI
     *                           should be attached to. The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return The root view of the fragment.
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
                diningButton.setVisibility(View.VISIBLE);
            } else {
                diningButton.setVisibility(View.GONE);
            }
            diningButton.setOnClickListener(v -> showDiningDialog());
        });
        return view;
    }

    /**
     * Called after the fragment's view has been created.
     *
     * @param view               The View returned by {@link #onCreateView}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        determineUserRole(() -> {
            populateTable();
        });
    }

    /**
     * Determines the role of the current user in relation to the trip (owner, contributor, or neither).
     *
     * @param callback A callback to be invoked once the role is determined.
     */
    private void determineUserRole(UserRoleCallback callback) {
        DatabaseManager.getInstance().getReference().child("users")
                .child(currentUserUid)
                .child("sharedTrips")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot tripOwner : snapshot.getChildren()) {
                                tripOwnerId = tripOwner.getKey();
                                effectiveUserUid = tripOwnerId;
                                isContributor = true;
                                break;
                            }
                        } else {
                            tripDatabase.child("owner").addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot ownerSnapshot) {
                                        if (ownerSnapshot.exists()) {
                                            tripOwnerId = ownerSnapshot.getValue(String.class);
                                        } else {
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

    /**
     * Displays a dialog for adding a new dining entry.
     */
    private void showDiningDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New Dining");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText locationInput = new EditText(getActivity());
        locationInput.setHint("Location");
        layout.addView(locationInput);

        final EditText websiteInput = new EditText(getActivity());
        websiteInput.setHint("Website");
        layout.addView(websiteInput);

        final EditText timeInput = new EditText(getActivity());
        timeInput.setHint("Time HH:MM");
        layout.addView(timeInput);

        final EditText dateInput = new EditText(getActivity());
        dateInput.setHint("Date MM/DD/YYYY");
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

    /**
     * Adds a new dining entry.
     *
     * @param location The location of the dining entry.
     * @param website  The website of the dining location.
     * @param time     The time of the dining reservation (HH:MM format).
     * @param date     The date of the dining reservation (MM/DD/YYYY format).
     */
    public void addDining(String location, String website, String time, String date) {
        if (date.isEmpty() || time.isEmpty() || location.isEmpty() || website.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!diningViewModel.isValidDate(date)) {
            Toast.makeText(getContext(),
                    "Date must be in MM/DD/YYYY format and be a valid date",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (!diningViewModel.isValidTime(time)) {
            Toast.makeText(getContext(),
                    "Time must be in HH:MM format and be a valid time",
                    Toast.LENGTH_LONG).show();
            return;
        }

        Dining dining = new Dining(location, website, time, date);
        diningViewModel.addDining(dining, effectiveUserUid, () -> {
            Toast.makeText(getContext(), "Dining added successfully", Toast.LENGTH_SHORT).show();
            addRowToTable(dining);
        });
    }

    /**
     * Populates the dining table with entries from the database.
     */
    public void populateTable() {
        LinearLayout diningList = getView().findViewById(R.id.dining_list);
        diningList.removeAllViews();

        diningViewModel.getDining(effectiveUserUid, dinings -> {
            if (diningList == null) {
                return;
            }
            for (Dining dining : dinings) {
                addRowToTable(dining);
            }
        });
    }

    /**
     * Adds a single dining entry to the table.
     *
     * @param dining The dining entry to add.
     */
    public void addRowToTable(Dining dining) {
        LinearLayout diningList = getView().findViewById(R.id.dining_list);

        LinearLayout diningLayout = new LinearLayout(getContext());
        diningLayout.setOrientation(LinearLayout.VERTICAL);
        diningLayout.setPadding(16, 16, 16, 16);

        TextView locationView = new TextView(getContext());
        locationView.setText("Location: " + dining.getLocation());
        diningLayout.addView(locationView);

        TextView websiteView = new TextView(getContext());
        websiteView.setText("Website: " + dining.getWebsite());
        diningLayout.addView(websiteView);

        TextView timeView = new TextView(getContext());
        timeView.setText("Time: " + dining.getTime());
        diningLayout.addView(timeView);

        TextView dateView = new TextView(getContext());
        dateView.setText("Date: " + dining.getDate());
        diningLayout.addView(dateView);

        if (dining.isExpired()) {
            diningLayout.setBackgroundColor(Color.RED);
        }

        diningList.addView(diningLayout);
    }

    /**
     * Callback interface for user role determination.
     */
    private interface UserRoleCallback {
        void onComplete();
    }
}
