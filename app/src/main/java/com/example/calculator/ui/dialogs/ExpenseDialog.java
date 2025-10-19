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
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class ExpenseDialog {
    private final Context context;
    private final LayoutInflater inflater;
    private final List<Expense> expenseList;
    private final ExpenseViewModel viewModel;

    private final TextInputEditText[] nameInput = new TextInputEditText[1];
    private final TextInputEditText[] costInput = new TextInputEditText[1];
    private final TextInputEditText[] discountInput = new TextInputEditText[1];

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

        Keyboard keyboard = new Keyboard(dialogView);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context,
                R.array.expense_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinnerListener(typeSpinner, container, keyboard);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        addButtonListener(listener, addBtn, typeSpinner, dialog);

        dialog.show();
    }

    private void typeSpinnerListener(@NonNull Spinner typeSpinner, FrameLayout container, Keyboard keyboard) {
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

                if (costInput[0] != null)
                    keyboard.setKeyboardListener(costInput[0]);
                if (discountInput[0] != null)
                    keyboard.setKeyboardListener(discountInput[0]);
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

            int discount = 0;
            if (discountInput[0] != null && discountInput[0].getVisibility() == View.VISIBLE) {
                try { discount = Integer.parseInt(discountInput[0].getText().toString()); }
                catch (NumberFormatException ignored) { discount = 0; }
            }

            if (name.isEmpty() || cost <= 0) {
                Toast.makeText(context, "Please fill all fields correctly", Toast.LENGTH_SHORT).show();
                return;
            }

            Expense expense = new Expense(type, name, cost, discount);
            expenseList.add(expense);
            viewModel.addExpense(expense);

            if (listener != null) listener.onExpenseAdded(expense);
            dialog.dismiss();
        });
    }

    private void dynamicInput(int position, View dynamicView) {
        switch(position) {
            case 0: // Travel
                costInput[0] = dynamicView.findViewById(R.id.expenseCostInput);
                discountInput[0] = dynamicView.findViewById(R.id.expenseDiscount);
                break;
            case 1: // Accommodation
                costInput[0] = dynamicView.findViewById(R.id.expenseCostInput);
                discountInput[0] = null;
                break;
            case 2: // Tickets
                costInput[0] = dynamicView.findViewById(R.id.expenseCostInput);
                discountInput[0] = dynamicView.findViewById(R.id.expenseDiscount);
                break;
        }
    }

    private static void checkboxListener(View dynamicView) {
        CheckBox checkBox = dynamicView.findViewById(R.id.checkboxDiscount);
        EditText discountEditText = dynamicView.findViewById(R.id.expenseDiscount);

        // Only proceed if both views exist in the layout
        if (checkBox != null && discountEditText != null) {
            final View discountLayout = (View) discountEditText.getParent().getParent();

            // Set initial state
            discountLayout.setVisibility(View.GONE);
            discountLayout.setAlpha(0f);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    discountLayout.setVisibility(View.VISIBLE);
                    discountLayout.animate().alpha(1f).setDuration(200).start();
                } else {
                    discountLayout.animate().alpha(0f).setDuration(200).withEndAction(() -> {
                        discountLayout.setVisibility(View.GONE);
                    }).start();
                }
            });
        }
    }

    private int layoutTypeSelector(int position) {
        switch (position) {
            case 0: return R.layout.layout_expense_transport;
            case 1: return R.layout.layout_expense_accommodation;
            case 2: return R.layout.layout_expense_ticket;
            default: throw new IllegalArgumentException("Invalid expense type position: " + position);
        }
    }
}
