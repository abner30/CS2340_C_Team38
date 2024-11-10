package com.example.myproject.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myproject.R;
import com.example.myproject.database.DatabaseManager;
import com.example.myproject.model.Destination;
import com.example.myproject.view.LoginActivity;
import com.example.myproject.viewmodel.DestinationViewModel;
import com.example.myproject.viewmodel.UserViewModel;
import com.github.mikephil.charting.charts.PieChart;

import android.widget.Button;

import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LogisticsFragment extends Fragment {

    /**
     * database reference to the full database
     */
    private DatabaseReference database;

    /**
     * reference calls reference to instance of database
     */
    private DatabaseReference reference;

    /**
     * tripDatabase reference to the tripData from firebase for ownerID
     */
    private DatabaseReference tripDatabase;
    /**
     * currentUserUid id of current user
     */
    private String currentUserUid;
    /**
     * effectiveUserUid stores the current user ID of the trip owner
     */
    private String effectiveUserUid;
    /**
     * isContributor boolean for whether user is a contributor or not
     */
    private boolean isContributor = false;
    /**
     * tripOwnerId stores the owner user ID
     */
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logistics, container, false);

        // Initialize Firebase references
        currentUserUid = DatabaseManager.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance().getReference("tripData")
                .child("contributors");
        tripDatabase = FirebaseDatabase.getInstance().getReference("tripData");
        reference = FirebaseDatabase.getInstance().getReference();

        // First, determine if user is a contributor and get the trip owner's ID
        determineUserRole(() -> {
            // Set up chart
            Button generateChartButton = view.findViewById(R.id.btn_generate_chart);
            PieChart pieChart = view.findViewById(R.id.pieChart);
            generateChartButton.setOnClickListener(v -> {
                generatePieChart(pieChart);
                pieChart.setVisibility(View.VISIBLE);
            });

            // Set up invite button - only visible to trip owner
            Button inviteButton = view.findViewById(R.id.btn_invite_users);
            inviteButton.setVisibility(View.GONE); // Hide by default

            if (tripOwnerId != null && tripOwnerId.equals(currentUserUid)) {
                inviteButton.setVisibility(View.VISIBLE);
            }

            Button logoutButton = view.findViewById(R.id.btn_logout);
            inviteButton.setOnClickListener(v -> showInviteDialog());
            logoutButton.setOnClickListener(v -> showLogoutDialog());
        });

        return view;
    }

    private void determineUserRole(UserRoleCallback callback) {
        // First check if the user is a contributor to any trip
        reference.child("users").child(currentUserUid) .child("sharedTrips")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // User is a contributor to someone's trip
                            for (DataSnapshot tripOwner : snapshot.getChildren()) {
                                tripOwnerId = tripOwner.getKey();
                                effectiveUserUid = tripOwnerId; // Use trip owner's UID for database
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
                                            tripDatabase.child("owner")
                                                    .setValue(currentUserUid);
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

    private void generatePieChart(PieChart pieChart) {
        // Always use the effectiveUserUid (trip owner's ID) for database operations
        DestinationViewModel destinationViewModel = new DestinationViewModel();
        UserViewModel userViewModel = new UserViewModel();

        reference.child("users").child(effectiveUserUid).child("duration")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final int[] allocatedDays = {1};
                        if (snapshot.exists()) {
                            allocatedDays[0] = snapshot.getValue(Integer.class);
                        }
                        // Use effectiveUserUid instead of currentUserUid
                        destinationViewModel.getDestinations(effectiveUserUid,
                                new DestinationViewModel.DestinationsCallback() {
                                @Override
                                public void onCallback(ArrayList<Destination> destinations) {
                                    ArrayList<Destination> list = destinationViewModel.
                                            getRecentDestinations(destinations);
                                    ArrayList<PieEntry> entries = new ArrayList<>();
                                    for (Destination destination : list) {
                                        String location = destination.getLocation();
                                        Integer daysPlanned;
                                        try {
                                            daysPlanned = userViewModel.calculateDuration(
                                                    destination.getStartDate(),
                                                    destination.getEndDate());
                                        } catch (ParseException e) {
                                            daysPlanned = 0;
                                        }
                                        if (allocatedDays[0] - daysPlanned >= 0
                                                && daysPlanned > 0) {
                                            allocatedDays[0] -= daysPlanned;
                                            entries.add(new PieEntry(daysPlanned, location));
                                        } else {
                                            break;
                                        }
                                    }
                                    entries.add(new PieEntry(allocatedDays[0],
                                            "Alloted days"));

                                    PieDataSet dataSet = new PieDataSet(entries, " ");
                                    PieData data = new PieData(dataSet);

                                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                    data.setValueTextSize(14f);
                                    dataSet.setValueTextColor(Color.BLACK);
                                    dataSet.setValueTextSize(16f);
                                    dataSet.setValueLinePart1OffsetPercentage(80f);
                                    pieChart.setEntryLabelColor(Color.BLACK);
                                    pieChart.getDescription().setEnabled(false);

                                    // Outline the entire pie chart
                                    dataSet.setSliceSpace(2f);
                                    pieChart.setHoleColor(Color.WHITE);
                                    pieChart.setTransparentCircleColor(Color.BLACK);
                                    pieChart.setTransparentCircleAlpha(110);
                                    pieChart.setTransparentCircleRadius(55f);
                                    pieChart.setData(data);
                                    pieChart.invalidate();
                                }
                            });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Error loading planned days",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> performLogout())
                .setNegativeButton("No", null)
                .show();
    }

    private void performLogout() {
        FirebaseAuth.getInstance().signOut();
        // Navigate to login activity
        Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        getActivity().finish();
    }

    private void showInviteDialog() {
        if (tripOwnerId == null || !tripOwnerId.equals(currentUserUid)) {
            Toast.makeText(getActivity(), "Only the trip owner can invite users",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Invite a User");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 10, 20, 10);

        final EditText emailInput = new EditText(getActivity());
        emailInput.setHint("Email Address");
        emailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(emailInput);

        final EditText noteInput = new EditText(getActivity());
        noteInput.setHint("Note (optional)");
        layout.addView(noteInput);

        builder.setView(layout);

        builder.setPositiveButton("Invite", (dialog, which) -> {
            String email = emailInput.getText().toString().trim();
            String note = noteInput.getText().toString().trim();
            inviteUser(email, note);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void inviteUser(String email, String note) {
        if (email.isEmpty()) {
            Toast.makeText(getActivity(), "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getActivity(), "Please enter a valid email address",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (tripOwnerId == null || tripOwnerId.isEmpty()) {
            Toast.makeText(getActivity(), "Error: Trip owner not found",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // First, check if the user exists by email
        reference.child("users").orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            showInviteErrorDialog(email);
                            return;
                        }

                        // Get the user's UID from the snapshot
                        DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                        final String invitedUserUid = userSnapshot.getKey();

                        if (invitedUserUid == null) {
                            Toast.makeText(getActivity(), "Error: Invalid user data",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Check if user is already a contributor
                        database.orderByChild("uid").equalTo(invitedUserUid)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(
                                            @NonNull DataSnapshot contributorSnapshot) {
                                        if (contributorSnapshot.exists()) {
                                            Toast.makeText(getActivity(),
                                                    "This user is already a contributor",
                                                    Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                        // Add the user as a contributor
                                        Map<String, Object> userMap = new HashMap<>();
                                        userMap.put("email", email);
                                        userMap.put("notes", note);
                                        userMap.put("uid", invitedUserUid);
                                        userMap.put("canEdit", true);
                                        userMap.put("invitedAt", ServerValue.TIMESTAMP);
                                        userMap.put("invitedBy", currentUserUid);

                                        // Generate a unique key for this contributor
                                        String contributorKey = database.push().getKey();
                                        if (contributorKey == null) {
                                            Toast.makeText(getActivity(),
                                                    "Error generating contributor key",
                                                    Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        database.child(contributorKey).setValue(userMap)
                                                .addOnSuccessListener(aVoid -> {
                                                    // Add this trip to the invited user's trips
                                                    reference.child("users")
                                                            .child(invitedUserUid)
                                                            .child("sharedTrips")
                                                            .child(effectiveUserUid)
                                                            .setValue(true)
                                                            .addOnSuccessListener(aVoid2 -> {
                                                                Toast.makeText(getActivity(),
                                                                        "User " + email
                                                                                + " invited"
                                                                                + "successfully!",
                                                                        Toast.LENGTH_SHORT).show();
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Toast.makeText(getActivity(),
                                                                        "Failed updating trip",
                                                                        Toast.LENGTH_SHORT).show();
                                                            });
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(getActivity(),
                                                            "Failed to invite user: "
                                                                    + e.getMessage(),
                                                            Toast.LENGTH_SHORT).show();
                                                });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getActivity(),
                                                "Error checking existing contributors: "
                                                        + error.getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(),
                                "Error checking email: " + error.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showInviteErrorDialog(String email) {
        new AlertDialog.Builder(getActivity())
                .setTitle("User Not Found")
                .setMessage("No user account found for " + email
                        + ". Please make sure the email address is correct "
                        + "and the user has registered an account.")
                .setPositiveButton("OK", null)
                .show();
    }

    private interface UserRoleCallback {
        void onComplete();
    }
}