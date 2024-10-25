package com.example.myproject.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myproject.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DestinationFragment extends Fragment {
    private LinearLayout formLayout, vacationFormLayout;
    private Button buttonLogTravel, buttonCalculateVacationTime;
    private EditText editTextTravelLocation, editTextStartDate, editTextStopDate;
    private Button buttonCancel, buttonSubmit;
    private EditText editTextVacationStartDate, editTextVacationEndDate;
    private Button buttonVacationSubmit;
    private TextView textViewResult; // Added for displaying the result
    /**
     * This method runs on create.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null,
     *                          this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination,
                container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        formLayout = getView().findViewById(R.id.formLayout);
        vacationFormLayout = getView().findViewById(R.id.vacationFormLayout);
        buttonLogTravel = getView().findViewById(R.id.buttonLogTravel);
        buttonCalculateVacationTime = getView().findViewById(R.id.buttonCalculateVacationTime);
        editTextTravelLocation = getView().findViewById(R.id.editTextTravelLocation);
        editTextStartDate = getView().findViewById(R.id.editTextStartDate);
        editTextStopDate = getView().findViewById(R.id.editTextStopDate);
        buttonCancel = getView().findViewById(R.id.buttonCancel);
        buttonSubmit = getView().findViewById(R.id.buttonSubmit);
        editTextVacationStartDate = getView().findViewById(R.id.editTextVacationStartDate);
        editTextVacationEndDate = getView().findViewById(R.id.editTextVacationEndDate);
        buttonVacationSubmit = getView().findViewById(R.id.buttonVacationSubmit);
        textViewResult = getView().findViewById(R.id.textViewResult); // Initialize result TextView
    }

    private void setupClickListeners() {
        Log.d("Happy", "I am not");
        buttonLogTravel.setOnClickListener(v -> {
            Log.d("Happy", "I am not");

            formLayout.setVisibility(View.VISIBLE);
        });

        buttonCalculateVacationTime.setOnClickListener(v -> {

            vacationFormLayout.setVisibility(View.VISIBLE);
        });

        buttonSubmit.setOnClickListener(v -> validateAndSubmitTravelLog());

        buttonCancel.setOnClickListener(v -> {
            clearFormFields();
        });

        buttonVacationSubmit.setOnClickListener(v -> validateAndSubmitVacation());
    }

    private void validateAndSubmitVacation() {
        String startDateStr = editTextVacationStartDate.getText().toString().trim();
        String endDateStr = editTextVacationEndDate.getText().toString().trim();

        // Validate dates if they're not empty
        if (!startDateStr.isEmpty() && !isValidDateFormat(startDateStr)) {
            Toast.makeText(getView().getContext(), "Invalid start date format. Use YYYY-MM-DD", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!endDateStr.isEmpty() && !isValidDateFormat(endDateStr)) {
            Toast.makeText(getView().getContext(), "Invalid end date format. Use YYYY-MM-DD", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check date order if both dates are provided
        if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
            if (!isEndDateAfterStartDate(startDateStr, endDateStr)) {
                Toast.makeText(getView().getContext(), "End date must be after start date", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Calculate the number of days between the two dates
        if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
            long daysBetween = calculateDaysBetween(startDateStr, endDateStr);
            showResult(daysBetween); // Display the result
        }

        // If all validations pass
        Toast.makeText(getView().getContext(), "Vacation time calculated successfully!", Toast.LENGTH_SHORT).show();
        clearVacationFields();
        hideAllForms();
    }

    private long calculateDaysBetween(String startDateStr, String endDateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);
            long difference = endDate.getTime() - startDate.getTime();
            return difference / (1000 * 60 * 60 * 24); // Convert milliseconds to days
        } catch (ParseException e) {
            return 0; // If there's an error, return 0
        }
    }

    private void showResult(long days) {
        textViewResult.setText("RESULT: " + days + " days");
        textViewResult.setVisibility(View.VISIBLE); // Show the result
        vacationFormLayout.setVisibility(View.GONE); // Hide the vacation form
    }

    private boolean isValidDateFormat(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isEndDateAfterStartDate(String startDateStr, String endDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date startDate = sdf.parse(startDateStr);
            Date endDate = sdf.parse(endDateStr);
            return endDate.after(startDate);
        } catch (ParseException e) {
            return false;
        }
    }

    private void clearVacationFields() {
        editTextVacationStartDate.setText("");
        editTextVacationEndDate.setText("");
    }

    private void clearFormFields() {
        editTextTravelLocation.setText("");
        editTextStartDate.setText("");
        editTextStopDate.setText("");
    }

    private void hideAllForms() {
        formLayout.setVisibility(View.GONE);
        vacationFormLayout.setVisibility(View.GONE);
        textViewResult.setVisibility(View.GONE); // Hide the result when hiding forms
    }

    // Dummy method for travel log submission (implement as needed)
    private void validateAndSubmitTravelLog() {
        Toast.makeText(getView().getContext(), "Travel log submitted!", Toast.LENGTH_SHORT).show();
        clearFormFields();
        hideAllForms();
    }
}
