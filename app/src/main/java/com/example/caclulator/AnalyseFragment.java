package com.example.caclulator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class AnalyseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_analyse, container, false);


        LineChart lineChart = view.findViewById(R.id.lineChart);


        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 2));
        entries.add(new Entry(1, 4));
        entries.add(new Entry(2, 1));
        entries.add(new Entry(3, 6));
        entries.add(new Entry(4, 3));


        LineDataSet dataSet = new LineDataSet(entries, "My Data");
        dataSet.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        dataSet.setValueTextColor(getResources().getColor(android.R.color.black));
        dataSet.setLineWidth(2f);
        dataSet.setCircleRadius(4f);


        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);


        lineChart.getDescription().setText("Sample Line Chart");
        lineChart.animateX(1000);


        lineChart.invalidate();

        return view;
    }
}