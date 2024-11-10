package com.example.myproject.view.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myproject.R;
import com.example.myproject.database.DatabaseManager;
import com.example.myproject.model.Destination;
import com.example.myproject.viewmodel.DestinationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.util.ArrayList;

public class DiningFragment extends Fragment {

    //DiningViewModel diningViewModel = new DiningViewModel();

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
        View view = inflater.inflate(R.layout.fragment_dining,
                container, false);

        // Set up invite
        FloatingActionButton reservationButton = view.findViewById(R.id.btn_add_dining);
        reservationButton.setOnClickListener(v -> showReservationDialog());

        return view;
    }

    private void showReservationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New Dining Reservation");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText timeInput = new EditText(getActivity());
        timeInput.setHint("time");
        layout.addView(timeInput);

        final EditText locationInput = new EditText(getActivity());
        locationInput.setHint("location");
        layout.addView(locationInput);

        final EditText websiteInput = new EditText(getActivity());
        websiteInput.setHint("website");
        layout.addView(websiteInput);

        builder.setView(layout);

        builder.setPositiveButton("Add Reservation", (dialog, which) -> {
            String time = timeInput.getText().toString().trim();
            String location = locationInput.getText().toString().trim();
            String website = websiteInput.getText().toString().trim();

            String uid = DatabaseManager.getInstance().getCurrentUser().getUid();
        });

        builder.show();
    }
//
//    public void populateTable() {
//        String uid = DatabaseManager.getInstance().getCurrentUser().getUid();
//        diningViewModel.getDining(uid, new DiningViewModel.DiningCallback() {
//            @Override
//            public void onCallback(ArrayList<Destination> destinations) {
//                ArrayList<Dining> list = diningViewModel.getRecentDining(destinations);
//                tableLayout.removeAllViews();
//                for (Dining dining : list) {
//                    String location = dining.getLocation();
//                    String daysPlanned;
//                    try {
//                        daysPlanned = String.valueOf(userViewModel.calculateDuration(dining.getStartDate(), dining.getEndDate()));
//                    } catch (ParseException e) {
//                        daysPlanned = "0";
//                    }
//                    daysPlanned += " days planned";
//                    addRowToTable(location, daysPlanned);
//                }
//            }
//        });
//    }
//
//    private void addRowToTable(String time, String location, String website) {
//        TableRow row = new TableRow(getContext());
//        TextView destinationText = new TextView(getContext());
//        TextView daysText = new TextView(getContext());
//
//        destinationText.setText(destination);
//        daysText.setText(daysPlanned);
//
//        // Styling the row
//        destinationText.setPadding(8, 8, 8, 8);
//        daysText.setPadding(8, 8, 8, 8);
//
//        row.addView(destinationText);
//        row.addView(daysText);
//
//        tableLayout.addView(row);
//    }
}
