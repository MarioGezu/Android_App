package com.example.calculator.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.calculator.R;
import com.example.calculator.models.Expense;
import com.example.calculator.viewmodel.ExpenseViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        types = res.getStringArray(R.array.expense_types);
        colors = new int[]{
                res.getColor(android.R.color.holo_blue_dark),
                res.getColor(android.R.color.holo_orange_dark),
                res.getColor(android.R.color.holo_red_dark)
        };

        // Observe expenses
        ExpenseViewModel viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        viewModel.getExpenses().observe(getViewLifecycleOwner(), this::updateChart);

        return view;
    }

    private void updateChart(List<Expense> expenses) {
        if (barChart == null || types == null) return;

        // Map type -> total
        Map<String, Float> totals = new HashMap<>();
        for (String type : types) totals.put(type, 0f);

        for (Expense expense : expenses) {
            if (totals.containsKey(expense.getType())) {
                totals.put(expense.getType(), totals.get(expense.getType()) + (float) expense.getCost());
            }
        }

        // Create BarDataSets dynamically
        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        ArrayList<BarEntry> entries;
        for (int i = 0; i < types.length; i++) {
            String type = types[i];
            float total = totals.get(type);
            entries = new ArrayList<>();
            entries.add(new BarEntry(0f, total));
            BarDataSet set = new BarDataSet(entries, type);
            set.setColor(colors[i % colors.length]);
            dataSets.add(set);
        }

        // Combine datasets into BarData
        BarData barData = new BarData();
        for (BarDataSet set : dataSets) barData.addDataSet(set);

        // Configure bar widths and spacing
        float groupSpace = 0.3f;
        float barSpace = 0.05f;
        float barWidth = 0.2f;
        barData.setBarWidth(barWidth);

        barChart.setData(barData);
        barChart.groupBars(0f, groupSpace, barSpace);

        // Configure X-axis
        barChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "Expenses"; // single group label
            }
        });
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setCenterAxisLabels(true);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);

        // Optional styling
        barChart.getDescription().setText("Expenses by Type");
        barChart.animateY(500);
        barChart.invalidate();
    }
}

