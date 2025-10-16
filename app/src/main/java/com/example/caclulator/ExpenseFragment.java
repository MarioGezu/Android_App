package com.example.caclulator;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caclulator.adapters.ExpenseAdapter;
import com.example.caclulator.models.Expense;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ExpenseFragment extends Fragment {

    private List<Expense> expenseList;
    private ExpenseAdapter expenseAdapter;

    public ExpenseFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.expenseRecyclerView);
        FloatingActionButton addButton = view.findViewById(R.id.addExpenseFab);

        expenseList = new ArrayList<>();
        expenseAdapter = new ExpenseAdapter(expenseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(expenseAdapter);

        addButton.setOnClickListener(v -> showAddExpenseDialog());

        return view;
    }

    private void showAddExpenseDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_add_expense, null);

        Spinner typeSpinner = dialogView.findViewById(R.id.expenseTypeSpinner);
        EditText nameInput = dialogView.findViewById(R.id.expenseNameInput);
        EditText costInput = dialogView.findViewById(R.id.expenseCostInput);
        Button addBtn = dialogView.findViewById(R.id.addExpenseButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.expense_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .create();

        addBtn.setOnClickListener(v -> {
            String type = typeSpinner.getSelectedItem().toString();
            String name = nameInput.getText().toString();
            double cost = 0;
            try { cost = Double.parseDouble(costInput.getText().toString()); }
            catch (NumberFormatException ignored) {}

            if (name.isEmpty() || cost <= 0) {
                Toast.makeText(getContext(), "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
                return;
            }

            expenseList.add(new Expense(type, name, cost));
            expenseAdapter.notifyItemInserted(expenseList.size() - 1);
            dialog.dismiss();
        });

        dialog.show();
    }
}
