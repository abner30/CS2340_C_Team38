package com.example.myproject.view.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.myproject.viewmodel.AccommodationViewModel;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myproject.R;
import com.example.myproject.database.DatabaseManager;
import com.example.myproject.model.Accommodation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AccommodationsFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_accommodations,
                container, false);

        // Set up invite
        FloatingActionButton accommodationButton = view.findViewById(R.id.btn_add_accommodation);
        accommodationButton.setOnClickListener(v -> showAccommodationDialog());

        return view;
    }

    private void showAccommodationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("New Accommodation");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText checkInInput = new EditText(getActivity());
        checkInInput.setHint("check in MM/DD/YYYY");
        layout.addView(checkInInput);

        final EditText checkOutInput = new EditText(getActivity());
        checkOutInput.setHint("check out MM/DD/YYYY");
        layout.addView(checkOutInput);

        final EditText locationInput = new EditText(getActivity());
        locationInput.setHint("location");
        layout.addView(locationInput);

        // Add TextView for Number of Rooms
        TextView numRoomsLabel = new TextView(getActivity());
        numRoomsLabel.setText("Number of Rooms");
        numRoomsLabel.setTextSize(19);
        layout.addView(numRoomsLabel);

        final Spinner numRoomsSpinner = new Spinner(getActivity());
        ArrayAdapter<CharSequence> numAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.num_of_rooms, android.R.layout.simple_spinner_item);
        numAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numRoomsSpinner.setAdapter(numAdapter);
        layout.addView(numRoomsSpinner);

        // Add TextView for Room Type
        TextView roomTypeLabel = new TextView(getActivity());
        roomTypeLabel.setText("Room Type");
        roomTypeLabel.setTextSize(19);
        layout.addView(roomTypeLabel);

        final Spinner roomTypeSpinner = new Spinner(getActivity());
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.room_types,
                android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roomTypeSpinner.setAdapter(typeAdapter);
        layout.addView(roomTypeSpinner);

        builder.setView(layout);

        builder.setPositiveButton("Add Accommodation", (dialog, which) -> {
            String checkIn = checkInInput.getText().toString().trim();
            String checkOut = checkOutInput.getText().toString().trim();
            String location = locationInput.getText().toString().trim();
            String numRoomsString = numRoomsSpinner.getSelectedItem().toString();
            int numRooms = Integer.parseInt(numRoomsString);
            String roomType = roomTypeSpinner.getSelectedItem().toString();

            String uid = DatabaseManager.getInstance().getCurrentUser().getUid();

            Accommodation accommodation = new Accommodation(checkIn, checkOut, location, numRooms, roomType);

            AccommodationViewModel accommodationViewModel = new ViewModelProvider(this).get(AccommodationViewModel.class);

//            accommodationViewModel.addAccommodation(accommodation, uid, new AccommodationViewModel.CompletionCallback() {
//                @Override
//                public void onComplete() {
//                    // This will ensure the callback is executed
//                    Log.d("AccommodationFragment", "Accommodation added successfully");
//                    Toast.makeText(getActivity(), "Accommodation added successfully!", Toast.LENGTH_SHORT).show();
//                }
//            });
        });
        builder.show();
    }
}
