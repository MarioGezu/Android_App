package com.example.caclulator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import android.widget.AdapterView;

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
        FrameLayout container = dialogView.findViewById(R.id.dynamicContainer);
        Button addBtn = dialogView.findViewById(R.id.addExpenseButton);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.expense_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        // Keep references to the dynamic views
        final EditText[] nameInput = new EditText[1];
        final EditText[] costInput = new EditText[1];
        final EditText[] discountInput = new EditText[1];

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                container.removeAllViews(); // clear previous layout

                int layoutId;
                switch (position) {
                    case 0:
                        layoutId = R.layout.layout_expense_transport;
                        break;
                    case 1:
                        layoutId = R.layout.layout_expense_accommodation;
                        break;
                    case 2:
                        layoutId = R.layout.layout_expense_ticket;
                        break;
                    default:
                        layoutId = R.layout.layout_expense_accommodation;
                }

                View dynamicView = inflater.inflate(layoutId, container, false);
                container.addView(dynamicView);

                CheckBox checkBox = dynamicView.findViewById(R.id.checkboxDiscount);
                EditText editText = dynamicView.findViewById(R.id.expenseDiscount);

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        editText.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                        editText.animate().alpha(isChecked ? 1f : 0f).setDuration(200).start();
                    }
                });
                // Find inputs in the newly inflated layout
                nameInput[0] = dynamicView.findViewById(R.id.expenseNameInput);

                if (position == 0) { // transport
                    discountInput[0] = dynamicView.findViewById(R.id.expenseDiscount);
                    costInput[0] = dynamicView.findViewById(R.id.expenseCostInput);
                } else if (position == 1) { // accommodation
                    costInput[0] = dynamicView.findViewById(R.id.expenseCostInput);
                    discountInput[0] = null;
                } else if (position == 2) { // ticket
                    discountInput[0] = dynamicView.findViewById(R.id.expenseDiscount);
                    costInput[0] = dynamicView.findViewById(R.id.expenseCostInput);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                container.removeAllViews();
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .create();

        addBtn.setOnClickListener(v -> {
            if (nameInput[0] == null || costInput[0] == null) return;

            String type = typeSpinner.getSelectedItem().toString();
            String name = nameInput[0].getText().toString();
            double cost = 0;
            int discount = 0;
            try { cost = Double.parseDouble(costInput[0].getText().toString()); }
            catch (NumberFormatException ignored) {}
            try { discount = Integer.parseInt(costInput[0].getText().toString()); }
            catch (NumberFormatException ignored) {}

            if (name.isEmpty() || cost <= 0) {
                Toast.makeText(getContext(), "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
                return;
            }

            // Add expense to your list
            expenseList.add(new Expense(type, name, cost, discount));
            expenseAdapter.notifyItemInserted(expenseList.size() - 1);
            dialog.dismiss();
        });

        dialog.show();
    }

}
