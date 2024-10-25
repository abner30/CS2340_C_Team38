package com.example.myproject.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.myproject.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DestinationActivity extends AppCompatActivity {
    private LinearLayout formLayout, vacationFormLayout;
    private Button buttonLogTravel, buttonCalculateVacationTime;
    private EditText editTextTravelLocation, editTextStartDate, editTextStopDate;
    private Button buttonCancel, buttonSubmit;
    private EditText editTextVacationStartDate, editTextVacationEndDate;
    private Button buttonVacationSubmit;
    private TextView textViewResult; // Added for displaying the result

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_destination);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        formLayout = findViewById(R.id.formLayout);
        vacationFormLayout = findViewById(R.id.vacationFormLayout);
        buttonLogTravel = findViewById(R.id.buttonLogTravel);
        buttonCalculateVacationTime = findViewById(R.id.buttonCalculateVacationTime);
        editTextTravelLocation = findViewById(R.id.editTextTravelLocation);
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextStopDate = findViewById(R.id.editTextStopDate);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        editTextVacationStartDate = findViewById(R.id.editTextVacationStartDate);
        editTextVacationEndDate = findViewById(R.id.editTextVacationEndDate);
        buttonVacationSubmit = findViewById(R.id.buttonVacationSubmit);
        textViewResult = findViewById(R.id.textViewResult); // Initialize result TextView
    }

    private void setupClickListeners() {
        buttonLogTravel.setOnClickListener(v -> {
            hideAllForms();
            formLayout.setVisibility(View.VISIBLE);
        });

        buttonCalculateVacationTime.setOnClickListener(v -> {
            hideAllForms();
            vacationFormLayout.setVisibility(View.VISIBLE);
        });

        buttonSubmit.setOnClickListener(v -> validateAndSubmitTravelLog());

        buttonCancel.setOnClickListener(v -> {
            clearFormFields();
            hideAllForms();
        });

        buttonVacationSubmit.setOnClickListener(v -> validateAndSubmitVacation());
    }

    private void hideAllForms() {
        formLayout.setVisibility(View.GONE);
        vacationFormLayout.setVisibility(View.GONE);
        textViewResult.setVisibility(View.GONE); // Hide the result when hiding forms
    }

    private void validateAndSubmitVacation() {
        String startDateStr = editTextVacationStartDate.getText().toString().trim();
        String endDateStr = editTextVacationEndDate.getText().toString().trim();

        // Validate dates if they're not empty
        if (!startDateStr.isEmpty() && !isValidDateFormat(startDateStr)) {
            Toast.makeText(this, "Invalid start date format. Use YYYY-MM-DD", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!endDateStr.isEmpty() && !isValidDateFormat(endDateStr)) {
            Toast.makeText(this, "Invalid end date format. Use YYYY-MM-DD", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check date order if both dates are provided
        if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
            if (!isEndDateAfterStartDate(startDateStr, endDateStr)) {
                Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Calculate the number of days between the two dates
        if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
            long daysBetween = calculateDaysBetween(startDateStr, endDateStr);
            showResult(daysBetween); // Display the result
        }

        // If all validations pass
        Toast.makeText(this, "Vacation time calculated successfully!", Toast.LENGTH_SHORT).show();
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

    // Dummy method for travel log submission (implement as needed)
    private void validateAndSubmitTravelLog() {
        Toast.makeText(this, "Travel log submitted!", Toast.LENGTH_SHORT).show();
        clearFormFields();
        hideAllForms();
    }
}
