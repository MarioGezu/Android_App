package com.example.calculator.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculator.R;
import com.example.calculator.models.Expense;
import com.example.calculator.ui.adapters.ExpenseAdapter;
import com.example.calculator.ui.dialogs.ExpenseDialog;
import com.example.calculator.viewmodel.ExpenseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ExpenseFragment extends Fragment {

    private ExpenseAdapter expenseAdapter;
    private List<Expense> expenseList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        // Get the shared ViewModel
        ExpenseViewModel viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        // Setup RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.expenseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        expenseAdapter = new ExpenseAdapter(getContext(), expenseList, viewModel);
        recyclerView.setAdapter(expenseAdapter);

        // Observe the expenses LiveData
        viewModel.getExpenses().observe(getViewLifecycleOwner(), expenses -> {
            if (expenses != null) {
                expenseList.clear();
                expenseList.addAll(expenses);
                expenseAdapter.notifyDataSetChanged();
            }
        });

        // Setup FAB
        FloatingActionButton addButton = view.findViewById(R.id.addExpenseFab);
        addButton.setOnClickListener(v -> {
            new ExpenseDialog(requireContext(), expenseList, requireActivity()).show(expense -> {
                // The observer will handle the UI update
            });
        });

        return view;
    }
}
