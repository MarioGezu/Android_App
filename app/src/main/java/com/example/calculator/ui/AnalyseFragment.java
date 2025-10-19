package com.example.calculator.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import androidx.core.content.ContextCompat;
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
                ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark),
                ContextCompat.getColor(requireContext(), android.R.color.holo_orange_dark),
                ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)
        };

        // Observe expenses
        ExpenseViewModel viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        viewModel.getExpenses().observe(getViewLifecycleOwner(), this::updateChart);

        ListView dataList = view.findViewById(R.id.dataList);
        List<String> statItems = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, statItems);
        dataList.setAdapter(adapter);

        viewModel.getExpenses().observe(getViewLifecycleOwner(), expenses -> {
            statItems.clear();

            List<Expense> sortedExpenses = new ArrayList<>(expenses);
            sortedExpenses.sort((e1, e2) -> Float.compare(e2.getCost(), e1.getCost()));

            for (Expense e : sortedExpenses) {
                statItems.add(e.getType() + ": " + e.getCost());
            }

            statItems.add(0, "Total cost: " + viewModel.getTotalCost().getValue());
            statItems.add(1, "Monthly per person: " + viewModel.getMonthlyPaymentPerPerson().getValue());
            statItems.add(2, "Participants: " + viewModel.getParticipantCount().getValue());
            statItems.add(3, "Months left: " + viewModel.getMonths().getValue());

            adapter.notifyDataSetChanged();
        });

        return view;
    }

    private void updateChart(List<Expense> expenses) {
        if (expenses == null || expenses.isEmpty()) {
            barChart.clear();
            barChart.invalidate();
            return;
        }

        Map<String, Float> categoryTotals = new HashMap<>();
        for (Expense e : expenses) {
            float current = categoryTotals.getOrDefault(e.getType(), 0f);
            categoryTotals.put(e.getType(), current + e.getCost());
        }

        List<BarEntry> entries = new ArrayList<>();
        int i = 0;
        for (String type : categoryTotals.keySet()) {
            entries.add(new BarEntry(i++, categoryTotals.get(type)));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Expenses");
        dataSet.setColors(colors);
        BarData data = new BarData(dataSet);
        barChart.setData(data);
        barChart.invalidate();
    }

}

