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

import com.example.myproject.model.Dining;
import com.example.myproject.viewmodel.DiningViewModel;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myproject.R;
import com.example.myproject.database.DatabaseManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DiningFragment extends Fragment {

    DiningViewModel diningViewModel = new DiningViewModel();

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
        View view = inflater.inflate(R.layout.fragment_dining, container, false);

        // Set up invite
        FloatingActionButton diningButton = view.findViewById(R.id.btn_add_dining);
        diningButton.setOnClickListener(v -> showDiningDialog());

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Now call populateTable after view has been created
        populateTable();
    }


    private void showDiningDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New Dining");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText dateInput = new EditText(getActivity());
        dateInput.setHint("data MM/DD/YYYY");
        layout.addView(dateInput);

        final EditText timeInput = new EditText(getActivity());
        timeInput.setHint("time HH:MM");
        layout.addView(timeInput);

        final EditText locationInput = new EditText(getActivity());
        locationInput.setHint("location");
        layout.addView(locationInput);

        final EditText websiteInput = new EditText(getActivity());
        locationInput.setHint("website");
        layout.addView(websiteInput);

        builder.setView(layout);

        builder.setPositiveButton("Add Dining", (dialog, which) -> {
            String date = dateInput.getText().toString().trim();
            String time = timeInput.getText().toString().trim();
            String location = locationInput.getText().toString().trim();
            String website = websiteInput.getText().toString().trim();
            addDining(date, time, location, website);
        });

        builder.show();
    }

    public void addDining(String date, String time, String location, String website) {
        // Validate inputs
        if (date.isEmpty() || time.isEmpty() || location.isEmpty() || website.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = DatabaseManager.getInstance().getCurrentUser().getUid();
        Dining dining = new Dining(date, time, location, website);
        diningViewModel.addDining(dining, uid, new DiningViewModel.CompletionCallback() {
            @Override
            public void onComplete() {
                Toast.makeText(getContext(), "Dining added successfully", Toast.LENGTH_SHORT).show();
                // Only add the newly created dining to the table
                addRowToTable(dining);
            }
        });
    }



    public void populateTable() {
        String uid = DatabaseManager.getInstance().getCurrentUser().getUid();
        LinearLayout diningsList = getView().findViewById(R.id.dinings_list);
        //diningsList.removeAllViews();

        diningViewModel.getDinings(uid, new DiningViewModel.DiningsCallback() {
            @Override
            public void onCallback(ArrayList<Dining> dinings) {

                for (Dining dining : dinings) {
                    addRowToTable(dining);
                }
            }
        });
    }

    public void addRowToTable(Dining dining) {
        LinearLayout diningsList = getView().findViewById(R.id.dinings_list);

        // Create a new layout for each dining entry
        LinearLayout diningLayout = new LinearLayout(getContext());
        diningLayout.setOrientation(LinearLayout.VERTICAL);
        diningLayout.setPadding(16, 16, 16, 16);

        // Location
        TextView locationView = new TextView(getContext());
        locationView.setText("Location: " + dining.getLocation());
        locationView.setTextSize(16);
        locationView.setTypeface(null, Typeface.BOLD);
        diningLayout.addView(locationView);

        // Check-in and Check-out Dates
        TextView dateOutView = new TextView(getContext());
        dateOutView.setText("Check-in: " + dining.getCheckIn() + ", Check-out: " + dining.getCheckOut());
        diningLayout.addView(dateOutView);

        // Number of Rooms
        TextView roomsView = new TextView(getContext());
        roomsView.setText("Number of Rooms: " + dining.getRooms());
        roomsView.setTypeface(null, Typeface.ITALIC);
        diningLayout.addView(roomsView);

        // Room Type
        TextView roomTypeView = new TextView(getContext());
        roomTypeView.setText("Room Type: " + dining.getType());
        roomTypeView.setPadding(8, 4, 8, 4);
        diningLayout.addView(roomTypeView);

        // Add the individual dining layout to the main dinings list
        diningsList.addView(diningLayout);
    }

}
