package com.example.myproject.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myproject.R;

//import java.util.ArrayList;
//import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.data.PieData;
//import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;


public class LogisticsFragment extends Fragment {
    //private PieChart pieChart;

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
        return inflater.inflate(R.layout.fragment_logistics,
                container, false);

        // Initialize the PieChart
//        pieChart = view.findViewById(R.id.pieChart);
//        setupPieChart();
//
//        return view;
    }
//    private void setupPieChart() {
//        // Example data for the pie chart
//        List<PieEntry> entries = new ArrayList<>();
//        entries.add(new PieEntry(4, "Allotted Days"));
//        entries.add(new PieEntry(3, "Remaining Days"));
//
//        PieDataSet dataSet = new PieDataSet(entries, "Trip Days");
//        dataSet.setColors(new int[]{android.graphics.Color.GREEN, android.graphics.Color.RED});
//
//        PieData data = new PieData(dataSet);
//        pieChart.setData(data);
//        pieChart.invalidate(); // Refresh chart with data
//    }
}


