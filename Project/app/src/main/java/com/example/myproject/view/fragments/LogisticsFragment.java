package com.example.myproject.view.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myproject.R;
import com.example.myproject.database.DatabaseManager;
import com.github.mikephil.charting.charts.PieChart;

import android.widget.Button;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LogisticsFragment extends Fragment {

    private DatabaseReference database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logistics, container, false);

        // Initialize Firebase reference
        database = FirebaseDatabase.getInstance().getReference("tripData").child("contributors");

        // Set up chart
        Button generateChartButton = view.findViewById(R.id.btn_generate_chart);
        PieChart pieChart = view.findViewById(R.id.pieChart);
        generateChartButton.setOnClickListener(v -> {
            generatePieChart(pieChart);
            pieChart.setVisibility(View.VISIBLE); // Show the PieChart and push elements below it
        });

        // Set up invite
        Button inviteButton = view.findViewById(R.id.btn_invite_users);
        inviteButton.setOnClickListener(v -> showInviteDialog());

        return view;

    }

    private void generatePieChart(PieChart pieChart) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(DatabaseManager.getInstance().getCurrentUser().getUid());

        userRef.child("duration").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                DatabaseReference tripDaysRef = FirebaseDatabase.getInstance().getReference("tripData").child("days");
                tripDaysRef.child("plannedDays").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Initialize allottedDays and plannedDays
                        Integer allottedDays = snapshot.getValue(Integer.class);
                        if (allottedDays == null) allottedDays = 0;
                        Integer plannedDays = snapshot.getValue(Integer.class);
                        if (plannedDays == null) plannedDays = 0;

                        // Initialize entries used for pieChart
                        ArrayList<PieEntry> entries = new ArrayList<>();
                        entries.add(new PieEntry(allottedDays, "Allotted"));
                        entries.add(new PieEntry(plannedDays, "Planned"));

                        // Initialize dataSet and data for pieChart
                        PieDataSet dataSet = new PieDataSet(entries, " ");
                        PieData data = new PieData(dataSet);

                        // Set design of text and chart
                        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        data.setValueTextSize(14f);
                        dataSet.setValueTextColor(Color.BLACK);
                        dataSet.setValueTextSize(16f);
                        dataSet.setValueLinePart1OffsetPercentage(80f);
                        pieChart.setEntryLabelColor(Color.BLACK);
                        pieChart.getDescription().setEnabled(false); // Hide the description label

                        // Outline the entire pie chart
                        dataSet.setSliceSpace(2f); // Adds spacing between slices (for outline effect)
                        pieChart.setHoleColor(Color.WHITE); // Set hole color (background color)
                        pieChart.setTransparentCircleColor(Color.BLACK); // Set color of the transparent outline
                        pieChart.setTransparentCircleAlpha(110); // Set transparency for the outline
                        pieChart.setTransparentCircleRadius(55f); // Set radius of the outline


                        pieChart.setData(data);
                        pieChart.invalidate();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Error loading planned days", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error loading allotted days", Toast.LENGTH_SHORT).show();
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
            String username = nameInput.getText().toString().trim();
            String note = noteInput.getText().toString().trim();
            inviteUser(username, note);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void inviteUser(String username, String note) {
        if (username.isEmpty()) {
            Toast.makeText(getActivity(), "Username cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", username);
        userMap.put("notes", note);

        database.child(username).setValue(userMap)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(getActivity(), "User " + username + " invited!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(getActivity(), "Failed to invite user: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
