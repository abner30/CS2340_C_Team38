package com.example.myproject.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myproject.R;
import com.github.mikephil.charting.charts.PieChart;

import android.widget.Button;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LogisticsFragment extends Fragment {

    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_logistics, container, false);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("tripData").child("contributors");

        // Set up chart and invite buttons
        Button generateChartButton = view.findViewById(R.id.btn_generate_chart);
        PieChart pieChart = view.findViewById(R.id.pieChart);
        generateChartButton.setOnClickListener(v -> generatePieChart(pieChart));

        Button inviteButton = view.findViewById(R.id.btn_invite_users);
        inviteButton.setOnClickListener(v -> showInviteDialog());

        return view;
    }

    private void generatePieChart(PieChart pieChart) {
        DatabaseReference tripDaysRef = FirebaseDatabase.getInstance().getReference("tripData").child("days");

        tripDaysRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                int allottedDays = task.getResult().child("allottedDays").getValue(Integer.class);
                int plannedDays = task.getResult().child("plannedDays").getValue(Integer.class);

                ArrayList<PieEntry> entries = new ArrayList<>();
                entries.add(new PieEntry(allottedDays, "Allotted"));
                entries.add(new PieEntry(plannedDays, "Planned"));

                PieDataSet dataSet = new PieDataSet(entries, "Trip Days");
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                PieData data = new PieData(dataSet);

                pieChart.setData(data);
                pieChart.invalidate();
            } else {
                Toast.makeText(getActivity(), "Failed to load data for chart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showInviteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Invite a User");

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nameInput = new EditText(getActivity());
        nameInput.setHint("Username");
        layout.addView(nameInput);

        final EditText noteInput = new EditText(getActivity());
        noteInput.setHint("Note");
        layout.addView(noteInput);

        builder.setView(layout);

        builder.setPositiveButton("Invite", (dialog, which) -> {
            String username = nameInput.getText().toString();
            String note = noteInput.getText().toString();
            inviteUser(username, note);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void inviteUser(String username, String note) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", username);
        userMap.put("notes", note);

        databaseReference.child(username).setValue(userMap)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(getActivity(), "User " + username + " invited!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getActivity(), "Failed to invite user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
