package com.example.calculator.ui;

import com.example.calculator.R;
import com.example.calculator.ui.dialogs.ExpenseDialog;
import com.example.calculator.ui.adapters.ExpenseAdapter;
import com.example.calculator.viewmodel.ExpenseViewModel;
import com.example.calculator.models.Expense;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ExpenseFragment extends Fragment {
    private List<Expense> expenseList;
    private ExpenseAdapter expenseAdapter;

    public ExpenseFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.expenseRecyclerView);
        FloatingActionButton addButton = view.findViewById(R.id.addExpenseFab);

        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(getContext(), expenseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(expenseAdapter);

        addButton.setOnClickListener(v ->
                new ExpenseDialog(requireContext(), expenseList, requireActivity())
                        .show(expense -> expenseAdapter.notifyItemInserted(expenseList.size() - 1))
        );

        ExpenseViewModel viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);
        // When adding expense:

        return view;
    }

}
