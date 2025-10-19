package com.example.calculator.ui;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.calculator.R;
import com.example.calculator.ui.dialogs.Keyboard;
import com.example.calculator.utils.ViewUtils;
import com.example.calculator.viewmodel.ExpenseViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class FieldTripFragment extends Fragment {

    private TextInputEditText dateInput;
    private final Calendar selectedDate = Calendar.getInstance();
    private final Handler debounceHandler = new Handler(Looper.getMainLooper());
    private Runnable debounceRunnable;

    public FieldTripFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_trip, container, false);
        ExpenseViewModel viewModel = new ViewModelProvider(requireActivity()).get(ExpenseViewModel.class);

        TextInputEditText studentsInput = view.findViewById(R.id.studentsInput);
        TextInputEditText teachersInput = view.findViewById(R.id.teachersInput);
        dateInput = view.findViewById(R.id.dateInput);

        // numeric keyboard
        Keyboard keyboard = new Keyboard(view);
        // default keyboard disabled
        studentsInput.setShowSoftInputOnFocus(false);
        teachersInput.setShowSoftInputOnFocus(false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                debounceHandler.removeCallbacks(debounceRunnable);
                debounceRunnable = () -> {
                    try {
                        int students = 0;
                        if (!studentsInput.getText().toString().isEmpty()) {
                            students = Integer.parseInt(studentsInput.getText().toString());
                        }
                        int teachers = 0;
                        if (!teachersInput.getText().toString().isEmpty()) {
                            teachers = Integer.parseInt(teachersInput.getText().toString());
                        }
                        viewModel.setParticipantCount(students + teachers);
                    } catch (NumberFormatException e) {
                        viewModel.setParticipantCount(0);
                    }
                };
                debounceHandler.postDelayed(debounceRunnable, 300); // 300ms delay
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        studentsInput.addTextChangedListener(textWatcher);
        teachersInput.addTextChangedListener(textWatcher);

        View.OnFocusChangeListener focusChangeListener = (v, hasFocus) -> {
            if (hasFocus) {
                keyboard.showKeyboard((TextInputEditText) v);
            } else {
                keyboard.hideKeyboard();
            }
        };

        studentsInput.setOnFocusChangeListener(focusChangeListener);
        teachersInput.setOnFocusChangeListener(focusChangeListener);

        ViewUtils.bindEditText(dateInput, value -> {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(value, formatter);
                Period period = Period.between(LocalDate.now(), date);
                int months = period.getYears() * 12 + period.getMonths(); // total months
                viewModel.setMonths(months);
            } catch (Exception e) {
                viewModel.setMonths(0);
            }
        });

        viewModel.getTotalCost().observe(getViewLifecycleOwner(), total -> {
            TextView totalCostText = view.findViewById(R.id.totalCostText);
            totalCostText.setText(requireContext().getString(R.string.total_cost, total));
        });

        viewModel.getMonths().observe(getViewLifecycleOwner(), months -> {
            TextView daysLeftText = view.findViewById(R.id.daysLeftText);
            daysLeftText.setText(requireContext().getString(R.string.months_left, months));
        });

        viewModel.getParticipantCount().observe(getViewLifecycleOwner(), total -> {
            TextView totalPeopleText = view.findViewById(R.id.totalPeopleText);
            totalPeopleText.setText(requireContext().getString(R.string.participants, total));
        });

        // Open date picker
        dateInput.setOnClickListener(v -> showDatePicker());

        return view;
    }

    private void showDatePicker() {
        Calendar today = Calendar.getInstance();
        new DatePickerDialog(
                requireContext(),
                (DatePicker datePicker, int year, int month, int day) -> {
                    selectedDate.set(year, month, day);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    dateInput.setText(sdf.format(selectedDate.getTime()));
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}
