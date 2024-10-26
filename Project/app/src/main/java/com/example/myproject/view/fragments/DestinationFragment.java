package com.example.myproject.view.fragments;

import android.os.Bundle;
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
    private LinearLayout formLayout, vacationFormLayout, resultCard;
    private Button buttonLogTravel, buttonCalculateVacationTime, buttonReset;
    private EditText editTextTravelLocation, editTextStartDate, editTextStopDate;
    private Button buttonCancel, buttonSubmit;
    private EditText editTextVacationStartDate, editTextVacationEndDate;
    private Button buttonVacationSubmit;
    private TextView resultDays;

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
        buttonVacationSubmit = getView().findViewById(R.id.buttonVacationSubmit);
        resultDays = getView().findViewById(R.id.resultDays);
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date startDate = dateFormat.parse(editTextVacationStartDate.getText().toString());
            Date endDate = dateFormat.parse(editTextVacationEndDate.getText().toString());

            if (startDate != null && endDate != null) {
                long diffMillis = endDate.getTime() - startDate.getTime();
                long diffDays = diffMillis / (24 * 60 * 60 * 1000);
                resultDays.setText(String.valueOf(diffDays));
                resultCard.setVisibility(View.VISIBLE);
            }
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Invalid date format. Use YYYY-MM-DD.", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearEditTextFields() {
        editTextVacationStartDate.setText("");
        editTextVacationEndDate.setText("");
    }
}
