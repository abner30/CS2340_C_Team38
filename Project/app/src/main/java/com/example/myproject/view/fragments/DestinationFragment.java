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

import androidx.fragment.app.Fragment;

import com.example.myproject.R;
import com.example.myproject.database.DatabaseManager;
import com.example.myproject.model.Destination;
import com.example.myproject.model.User;
import com.example.myproject.viewmodel.DestinationViewModel;
import com.example.myproject.viewmodel.UserViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;

public class DestinationFragment extends Fragment {
    UserViewModel userViewModel = new UserViewModel();
    DestinationViewModel destinationViewModel = new DestinationViewModel();
    private LinearLayout formLayout, vacationFormLayout, resultCard;
    private Button buttonLogTravel, buttonCalculateVacationTime, buttonReset;
    private EditText editTextTravelLocation, editTextStartDate, editTextStopDate;
    private Button buttonCancel, buttonSubmit;
    private EditText editTextVacationStartDate, editTextVacationEndDate, editTextDuration;
    private Button buttonVacationSubmit;
    private TextView resultDays;
    private TableLayout tableLayout;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeViews();
        setupClickListeners();
        populateTable();
    }

    private void initializeViews() {
        formLayout = getView().findViewById(R.id.formLayout);
        vacationFormLayout = getView().findViewById(R.id.vacationFormLayout);
        resultCard = getView().findViewById(R.id.resultCard);
        buttonLogTravel = getView().findViewById(R.id.buttonLogTravel);
        buttonCalculateVacationTime = getView().findViewById(R.id.buttonCalculateVacationTime);
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
        tableLayout = getView().findViewById(R.id.tableLayout); // New table layout reference
    }

    private void setupClickListeners() {
        buttonLogTravel.setOnClickListener(v -> {
            formLayout.setVisibility(View.VISIBLE);
            vacationFormLayout.setVisibility(View.GONE);
            resultCard.setVisibility(View.GONE);
        });

        buttonCalculateVacationTime.setOnClickListener(v -> {
            formLayout.setVisibility(View.GONE);
            vacationFormLayout.setVisibility(View.VISIBLE);
            resultCard.setVisibility(View.GONE);
        });

        buttonSubmit.setOnClickListener(v -> formLayout.setVisibility(View.GONE));
        buttonCancel.setOnClickListener(v -> formLayout.setVisibility(View.GONE));

        buttonVacationSubmit.setOnClickListener(v -> calculateDays());

        buttonReset.setOnClickListener(v -> {
            formLayout.setVisibility(View.GONE);
            vacationFormLayout.setVisibility(View.GONE);
            resultCard.setVisibility(View.GONE);
            clearEditTextFields();
        });
    }

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
        int duration = Integer.valueOf(durationInput);
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
            } catch (ParseException e) {
                Toast.makeText(getContext(), "Invalid date format. Use MM-dd-yyyy.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clearEditTextFields() {
        editTextVacationStartDate.setText("");
        editTextVacationEndDate.setText("");
        editTextDuration.setText("");
    }

    /**
     * Populates the table with destinations from the database.
     * If data is unavailable, displays a default entry in the table.
     */
    private void populateTable() {
        ArrayList<Destination> list = destinationViewModel.getRecentDestinations(destinationViewModel.getDestinations(
                DatabaseManager.getInstance().getCurrentUser().getUid()));
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("destinations");
        tableLayout.removeAllViews();
        for (Destination destination: list) {
            String location = destination.getLocation();
            String daysPlanned;
            try {
                daysPlanned= String.valueOf(userViewModel.calculateDuration(destination.getStartDate(), destination.getEndDate()));
            } catch (ParseException e){
                daysPlanned = "0";
            }
            daysPlanned += " days planned";
            addRowToTable(location, daysPlanned);
        }
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
}
