package com.example.calculator.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.calculator.R;
import com.example.calculator.models.Expense;
import com.example.calculator.viewmodel.ExpenseViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AnalyseFragment extends Fragment {

    private BarChart barChart;
    private String[] types;
    private int[] colors;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_analyse, container, false);
        barChart = view.findViewById(R.id.barChart);
        Resources res = getResources();

        // Define types and colors
        types = res.getStringArray(R.array.expense_types);
        colors = new int[]{
                ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark),
                ContextCompat.getColor(requireContext(), android.R.color.holo_orange_dark),
                ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)
        };

        // Basic chart setup
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(types));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(types.length);

        // Setup ListView
        ListView dataList = view.findViewById(R.id.dataList);
        List<String> statItems = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, statItems);
        dataList.setAdapter(adapter);

        // Observe ViewModel and update UI
        ExpenseViewModel viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        viewModel.getExpenses().observe(getViewLifecycleOwner(), expenses -> {
            if (expenses == null) return;

            // 1. Calculate category totals
            Map<String, Float> categoryTotals = new HashMap<>();
            for (String type : types) { // Initialize map to ensure order
                categoryTotals.put(type, 0f);
            }
            for (Expense e : expenses) {
                categoryTotals.put(e.getType(), categoryTotals.getOrDefault(e.getType(), 0f) + e.getCost());
            }

            // 2. Update Bar Chart
            updateChart(categoryTotals);

            // 3. Update Statistics List
            statItems.clear();
            statItems.add(getString(R.string.total_cost, viewModel.getTotalCost().getValue()));
            statItems.add(getString(R.string.monthly_per_person, viewModel.getMonthlyPaymentPerPerson().getValue()));
            statItems.add(getString(R.string.participants, viewModel.getParticipantCount().getValue()));
            statItems.add(getString(R.string.months_left, viewModel.getMonths().getValue()));
            statItems.add(""); // Separator
            statItems.add(getString(R.string.category_breakdown));
            for (int i = 0; i < types.length; i++) {
                statItems.add(types[i] + ": " + String.format(Locale.getDefault(), "%.2f", categoryTotals.get(types[i])));
            }
            adapter.notifyDataSetChanged();
        });

        return view;
    }

    private void updateChart(Map<String, Float> categoryTotals) {
        // Check if there is any data to display
        boolean hasData = false;
        for(float v : categoryTotals.values()) {
            if (v > 0) {
                hasData = true;
                break;
            }
        }

        if (!hasData) {
            barChart.clear();
            barChart.invalidate();
            return;
        }

        // Create entries in a fixed order based on the 'types' array
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < types.length; i++) {
            entries.add(new BarEntry(i, categoryTotals.get(types[i])));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Expenses");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.invalidate(); // Refresh the chart
    }
}
