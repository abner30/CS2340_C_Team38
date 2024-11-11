package com.example.myproject.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myproject.R;
import com.example.myproject.database.DatabaseManager;
import com.example.myproject.model.Destination;
import com.example.myproject.viewmodel.DestinationViewModel;
import com.example.myproject.viewmodel.UserViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * A Fragment that manages user travel destinations, vacation time calculation,
 * and displays travel data in a table. It allows users to log travel destinations,
 * calculate the duration of vacations, and interact with a database to store and
 * retrieve travel data.
 */
public class DestinationFragment extends Fragment {
    private UserViewModel userViewModel = new UserViewModel();
    private DestinationViewModel destinationViewModel = new DestinationViewModel();
    private LinearLayout formLayout;
    private LinearLayout vacationFormLayout;
    private LinearLayout resultCard;
    private Button buttonLogTravel;
    private Button buttonCalculateVacationTime;
    private Button buttonReset;
    private EditText editTextTravelLocation;
    private EditText editTextStartDate;
    private EditText editTextStopDate;
    private Button buttonCancel;
    private Button buttonSubmit;
    private EditText editTextVacationStartDate;
    private EditText editTextVacationEndDate;
    private EditText editTextDuration;
    private Button buttonVacationSubmit;
    private TextView resultDays;
    private TableLayout tableLayout;

    /**
     * database reference to the full database
     */
    private DatabaseReference database;

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
     * Inflates the layout for this fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     *                 any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's
     *                  UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination, container, false);
        currentUserUid = DatabaseManager.getInstance().getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance().getReference("tripData")
                .child("contributors");
        tripDatabase = FirebaseDatabase.getInstance().getReference("tripData");
        return view;
    }

    /**
     * Called when the Fragment is visible to the user and actively running.
     * Initializes the views, sets up click listeners, and populates the
     * table with destinations.
     * This is needed because launching directly as you do with a view will break
     * the fragment binding
     */
    @Override
    public void onStart() {
        super.onStart();
        determineUserRole(() -> {
            initializeViews();
            setupClickListeners();
            populateTable();
        });
    }

    /**
     * Initializes the UI components by finding views by their IDs.
     */
    private void initializeViews() {
        formLayout = getView().findViewById(R.id.formLayout);
        vacationFormLayout = getView().findViewById(R.id.vacationFormLayout);
        resultCard = getView().findViewById(R.id.resultCard);
        buttonLogTravel = getView().findViewById(R.id.buttonLogTravel);
        if (tripOwnerId != null && tripOwnerId.equals(currentUserUid)) {
            buttonLogTravel.setVisibility(View.VISIBLE);
        } else if (isContributor) {
            // Show buttons for contributors but not the trip owner
            buttonLogTravel.setVisibility(View.VISIBLE);
        } else {
            // Hide buttons for non-contributors
            buttonLogTravel.setVisibility(View.GONE);
        }
        buttonCalculateVacationTime = getView().findViewById(R.id.buttonCalculateVacationTime);
        if (tripOwnerId != null && tripOwnerId.equals(currentUserUid)) {
            buttonCalculateVacationTime.setVisibility(View.VISIBLE);
        } else if (isContributor) {
            // Show buttons for contributors but not the trip owner
            buttonCalculateVacationTime.setVisibility(View.VISIBLE);
        } else {
            // Hide buttons for non-contributors
            buttonCalculateVacationTime.setVisibility(View.GONE);
        }
        buttonReset = getView().findViewById(R.id.buttonReset);
        editTextTravelLocation = getView().findViewById(R.id.editTextTravelLocation);
        editTextStartDate = getView().findViewById(R.id.editTextStartDate);
        editTextStopDate = getView().findViewById(R.id.editTextStopDate);
        buttonCancel = getView().findViewById(R.id.buttonCancel);
        buttonSubmit = getView().findViewById(R.id.buttonSubmit);
        editTextVacationStartDate = getView().findViewById(R.id.editTextVacationStartDate);
        editTextVacationEndDate = getView().findViewById(R.id.editTextVacationEndDate);
        editTextDuration = getView().findViewById(R.id.editTextDuration);
        buttonVacationSubmit = getView().findViewById(R.id.buttonVacationSubmit);
        resultDays = getView().findViewById(R.id.resultDays);
        tableLayout = getView().findViewById(R.id.tableLayout);
        // New table layout reference
    }

    private void determineUserRole(DestinationFragment.UserRoleCallback callback) {
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
                                effectiveUserUid = tripOwnerId; // Use trip owner's UID for database
                                isContributor = true;
                                break;
                            }
                        } else {
                            // User is not a contributor, check if they're an owner
                            tripDatabase.child("owner").addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(
                                                @NonNull DataSnapshot ownerSnapshot) {
                                            if (ownerSnapshot.exists()) {
                                                tripOwnerId = ownerSnapshot.getValue(String.class);
                                            } else {
                                                // If no owner is set, set current user as owner
                                                tripOwnerId = currentUserUid;
                                                tripDatabase.child("owner").
                                                        setValue(currentUserUid);
                                            }
                                            effectiveUserUid = tripOwnerId;
                                            callback.onComplete();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(getActivity(), "Error checking owner "
                                                            + "status: " + error.getMessage(),
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
     * Sets up click listeners for various buttons to perform actions
     * like logging travel, calculating vacation time, submitting and
     * resetting forms.
     */
    private void setupClickListeners() {
        buttonLogTravel.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);
            vacationFormLayout.setVisibility(View.GONE);
            resultCard.setVisibility(View.GONE);
            editTextStartDate.setText("01/01/2001");
            editTextStopDate.setText("01/01/2001");
            editTextTravelLocation.setText("Berlin");
        });

        buttonCalculateVacationTime.setOnClickListener(v -> {
            formLayout.setVisibility(View.GONE);
            vacationFormLayout.setVisibility(View.VISIBLE);
            resultCard.setVisibility(View.GONE);
            editTextVacationStartDate.setText("01/01/2001");
            editTextVacationEndDate.setText("01/01/2001");
            editTextDuration.setText("0");
        });

        buttonSubmit.setOnClickListener(v -> addDestination());
        buttonCancel.setOnClickListener(v -> formLayout.setVisibility(View.GONE));

        buttonVacationSubmit.setOnClickListener(v -> calculateDays());

        buttonReset.setOnClickListener(v -> {
            formLayout.setVisibility(View.GONE);
            vacationFormLayout.setVisibility(View.GONE);
            resultCard.setVisibility(View.GONE);
            clearEditTextFields();
        });
    }

    /**
     * Calculates the number of vacation days based on user input.
     * Validates input fields and adds a new trip if the required
     * fields are filled.
     * Stores data into firebase
     */
    private void calculateDays() {
        String date1 = editTextVacationStartDate.getText().toString();
        String date2 = editTextVacationEndDate.getText().toString();
        String durationInput = editTextDuration.getText().toString();
        int nullCount = 0;
        if (date1 == null || date1.trim().equals("")) {
            nullCount++;
        }
        if (date2 == null || date2.trim().equals("")) {
            nullCount++;
        }
        if (durationInput == null || durationInput.trim().equals("")) {
            nullCount++;
        }
        int duration;
        if (durationInput != null && !durationInput.trim().equals("")) {
            duration = Integer.valueOf(durationInput);
        } else {
            duration = 0;
        }
        if (nullCount > 1) {
            Toast.makeText(getContext(), "Enter at least two fields.", Toast.LENGTH_SHORT).show();

        }
        if (nullCount == 0) {
            userViewModel.addTrip(DatabaseManager.getInstance().getCurrentUser().getUid(),
                    duration, date1, date2);
        }
        if (nullCount == 1) {
            try {
                if (date1 == null || date1.trim().equals("")) {
                    date1 = userViewModel.calculateStart(date2, duration);
                }
                if (date2 == null || date2.trim().equals("")) {
                    date2 = userViewModel.calculateEnd(date1, duration);
                }
                if (durationInput == null || durationInput.trim().equals("")) {
                    duration = userViewModel.calculateDuration(date1, date2);
                }
                userViewModel.addTrip(DatabaseManager.getInstance().getCurrentUser().getUid(),
                        duration, date1, date2);
                resultDays.setText(String.valueOf(duration));
                resultCard.setVisibility(View.VISIBLE);

            } catch (ParseException e) {
                Toast.makeText(getContext(), "Invalid date format. Use MM/dd/yyyy.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Clears all EditText fields in the form.
     */
    private void clearEditTextFields() {
        editTextVacationStartDate.setText("");
        editTextVacationEndDate.setText("");
        editTextDuration.setText("");
    }

    /**
     * Adds destination to fireBase.
     */
    public void addDestination() {
        String startDate = editTextStartDate.getText().toString();
        String endDate = editTextStopDate.getText().toString();
        String location = editTextTravelLocation.getText().toString();
        Destination destination = new Destination(location, startDate, endDate, 0);
        destinationViewModel.addDestination(destination,
                DatabaseManager.getInstance().getCurrentUser().getUid(),
                new DestinationViewModel.CompletionCallback() {
                    @Override
                    public void onComplete() {
                        populateTable(); // Populate the table only after data is written
                        editTextStartDate.setText("");
                        editTextStopDate.setText("");
                        editTextTravelLocation.setText("");
                    }
                });
    }

    /**
     * Populates the table with destinations from the database.
     * If data is unavailable, displays a default entry in the table.
     */
    public void populateTable() {
        String uid = DatabaseManager.getInstance().getCurrentUser().getUid();
        destinationViewModel.getDestinations(uid, new DestinationViewModel.DestinationsCallback() {
            @Override
            public void onCallback(ArrayList<Destination> destinations) {
                ArrayList<Destination> list = destinationViewModel.
                        getRecentDestinations(destinations);
                tableLayout.removeAllViews();
                for (Destination destination: list) {
                    String location = destination.getLocation();
                    String daysPlanned;
                    try {
                        daysPlanned = String.valueOf(userViewModel.calculateDuration(
                                destination.getStartDate(), destination.getEndDate()));
                    } catch (ParseException e) {
                        daysPlanned = "0";
                    }
                    daysPlanned += " days planned";
                    addRowToTable(location, daysPlanned);
                }
            }
        });
    }

    /**
     * Adds a row to the table with the specified destination and days planned.
     *
     * @param destination the destination name
     * @param daysPlanned the days planned for the destination
     */
    private void addRowToTable(String destination, String daysPlanned) {
        TableRow row = new TableRow(getContext());
        TextView destinationText = new TextView(getContext());
        TextView daysText = new TextView(getContext());

        destinationText.setText(destination);
        daysText.setText(daysPlanned);

        // Styling the row
        destinationText.setPadding(8, 8, 8, 8);
        daysText.setPadding(8, 8, 8, 8);

        row.addView(destinationText);
        row.addView(daysText);

        tableLayout.addView(row);
    }
    private interface UserRoleCallback {
        void onComplete();
    }
}