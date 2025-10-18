package com.example.calculator.ui.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.calculator.R;
import com.example.calculator.models.Expense;
import com.example.calculator.viewmodel.ExpenseViewModel;

import java.util.List;

public class ExpenseDialog {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<Expense> expenseList;
    private final ExpenseViewModel viewModel;

    private final EditText[] nameInput = new EditText[1];
    private final EditText[] costInput = new EditText[1];
    private final EditText[] discountInput = new EditText[1];

    public interface OnExpenseAddedListener {
        void onExpenseAdded(Expense expense);
    }

    public ExpenseDialog(@NonNull Context context, @NonNull List<Expense> expenseList, @NonNull ViewModelStoreOwner owner) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.expenseList = expenseList;
        this.viewModel = new ViewModelProvider(owner).get(ExpenseViewModel.class);
    }

    public void show(OnExpenseAddedListener listener) {
        View dialogView = inflater.inflate(R.layout.dialog_add_expense, null);
        Spinner typeSpinner = dialogView.findViewById(R.id.expenseTypeSpinner);
        FrameLayout container = dialogView.findViewById(R.id.dynamicContainer);
        Button addBtn = dialogView.findViewById(R.id.addExpenseButton);

        // Nem tetszenek a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                R.array.expense_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinnerListener(typeSpinner, container);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        addButtonListener(listener, addBtn, typeSpinner, dialog);

        dialog.show();
    }

    private void typeSpinnerListener(@NonNull Spinner typeSpinner, FrameLayout container) {
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            @SuppressLint("MissingInflatedId")
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                container.removeAllViews();
                int layoutId = layoutTypeSelector(position);
                View dynamicView = inflater.inflate(layoutId, container, false);
                container.addView(dynamicView);

                checkboxListener(dynamicView);
                nameInput[0] = dynamicView.findViewById(R.id.expenseNameInput);
                dynamicInput(position, dynamicView);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                container.removeAllViews();
            }
        });
    }

    private void addButtonListener(OnExpenseAddedListener listener, @NonNull Button addBtn, Spinner typeSpinner, AlertDialog dialog) {
        addBtn.setOnClickListener(v -> {
            if (nameInput[0] == null || costInput[0] == null) return;

            String type = typeSpinner.getSelectedItem().toString();
            String name = nameInput[0].getText().toString();

            int cost;
            try { cost = Integer.parseInt(costInput[0].getText().toString()); }
            catch (NumberFormatException ignored) { cost = 0; }

            int discount;
            try { discount = Integer.parseInt(costInput[0].getText().toString()); }
            catch (NumberFormatException ignored) { discount = 0; }

            if (name.isEmpty() || cost <= 0) {
                Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
                return;
            }

            Expense expense = new Expense(type, name, cost, discount);
            expenseList.add(expense);
            viewModel.addExpense(expense);

            // megtudni hogy ez mit csinál
            if (listener != null) listener.onExpenseAdded(expense);
            dialog.dismiss();
        });
    }

    private void dynamicInput(int position, View dynamicView) {
        switch(position) {
            case 0:
                discountInput[0] = dynamicView.findViewById(R.id.expenseDiscount);
                costInput[0] = dynamicView.findViewById(R.id.expenseCostInput);
                break;
            case 1:
                discountInput[0] = null;
                costInput[0] = dynamicView.findViewById(R.id.expenseCostInput);
                break;
            case 2:
                discountInput[0] = dynamicView.findViewById(R.id.expenseDiscount);
                costInput[0] = dynamicView.findViewById(R.id.expenseCostInput);
                break;
        }
    }

    private static void checkboxListener(View dynamicView) {
        CheckBox checkBox = dynamicView.findViewById(R.id.checkboxDiscount);
        EditText editText = dynamicView.findViewById(R.id.expenseDiscount);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editText.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            editText.animate().alpha(isChecked ? 1f : 0f).setDuration(200).start();
        });
    }

    private int layoutTypeSelector(int position) {
        switch (position) {
            case 0: return R.layout.layout_expense_transport;
            case 1: return R.layout.layout_expense_accommodation;
            case 2: return R.layout.layout_expense_ticket;
        }
        return position;
    }
}
