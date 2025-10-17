package com.example.caclulator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.example.caclulator.models.Expense;
import java.util.ArrayList;
import java.util.List;

public class AnalyseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ExpenseViewModel viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        viewModel.getExpenses().observe(getViewLifecycleOwner(), expenseList -> {
            // Rebuild chart with new expenseList
            updateChart(expenseList);
        });

        View view = inflater.inflate(R.layout.fragment_analyse, container, false);

        BarChart barChart = view.findViewById(R.id.barChart);

        // Sample entries for each category
        ArrayList<BarEntry> travelEntries = new ArrayList<>();
        travelEntries.add(new BarEntry(0, 100));


        ArrayList<BarEntry> accommodationEntries = new ArrayList<>();
        accommodationEntries.add(new BarEntry(0, 300));


        ArrayList<BarEntry> ticketEntries = new ArrayList<>();
        ticketEntries.add(new BarEntry(0, 1500));


        // Create datasets
        BarDataSet dataSetTravel = new BarDataSet(travelEntries, "Travel");
        dataSetTravel.setColor(getResources().getColor(android.R.color.holo_blue_dark));

        BarDataSet dataSetAccommodation = new BarDataSet(accommodationEntries, "Accommodation");
        dataSetAccommodation.setColor(getResources().getColor(android.R.color.holo_orange_dark));

        BarDataSet dataSetTicket = new BarDataSet(ticketEntries, "Ticket");
        dataSetTicket.setColor(getResources().getColor(android.R.color.holo_red_dark));

        // Create BarData with all datasets
        BarData barData = new BarData(dataSetTravel, dataSetAccommodation, dataSetTicket);

        // Adjust bar width and spacing
        float groupSpace = 0.3f;
        float barSpace = 0.05f;
        float barWidth = 0.2f;
        barData.setBarWidth(barWidth);

        // After setting barData
        barChart.setData(barData);
        barChart.groupBars(0f, groupSpace, barSpace); // start at x=0

// Fix the x-axis to show all bars
        barChart.getXAxis().setAxisMinimum(0f);
        barChart.getXAxis().setAxisMaximum(0f + barChart.getBarData().getGroupWidth(groupSpace, barSpace) * travelEntries.size());
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setCenterAxisLabels(true);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);

        // Set data and group bars
        barChart.setData(barData);
        barChart.groupBars(0f, groupSpace, barSpace); // start at x=0

        // Optional styling
        barChart.getDescription().setText("Expenses Chart");
        barChart.animateY(1500);
        barChart.invalidate(); // refresh

        return view;
    }

    public void updateChart(List<Expense> expenseList) {
        if (getView() == null) return;

        BarChart barChart = getView().findViewById(R.id.barChart);

        float travelTotal = 0f;
        float accommodationTotal = 0f;
        float ticketTotal = 0f;

        for (Expense expense : expenseList) {
            switch (expense.getType()) {
                case "Travel": travelTotal += expense.getCost(); break;
                case "Accommodation": accommodationTotal += expense.getCost(); break;
                case "Tickets": ticketTotal += expense.getCost(); break;
            }
        }

        ArrayList<BarEntry> travelEntries = new ArrayList<>();
        travelEntries.add(new BarEntry(0f, travelTotal));

        ArrayList<BarEntry> accommodationEntries = new ArrayList<>();
        accommodationEntries.add(new BarEntry(0f, accommodationTotal));

        ArrayList<BarEntry> ticketEntries = new ArrayList<>();
        ticketEntries.add(new BarEntry(0f, ticketTotal));

        BarDataSet setTravel = new BarDataSet(travelEntries, "Travel");
        setTravel.setColor(getResources().getColor(android.R.color.holo_blue_dark));

        BarDataSet setAccommodation = new BarDataSet(accommodationEntries, "Accommodation");
        setAccommodation.setColor(getResources().getColor(android.R.color.holo_orange_dark));

        BarDataSet setTicket = new BarDataSet(ticketEntries, "Ticket");
        setTicket.setColor(getResources().getColor(android.R.color.holo_red_dark));

        BarData data = new BarData(setTravel, setAccommodation, setTicket);

        float groupSpace = 0.3f;
        float barSpace = 0.05f;
        float barWidth = 0.2f;

        data.setBarWidth(barWidth);
        barChart.setData(data);
        barChart.groupBars(0f, groupSpace, barSpace);

        barChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "Expenses"; // Only one group, so one label
            }
        });

        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setCenterAxisLabels(true);

        barChart.getDescription().setText("Expenses by Type");
        barChart.animateY(500);
        barChart.invalidate();
    }

}
