package com.example.calculator.ui;


import android.app.DatePickerDialog;
import android.os.Bundle;
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

    public FieldTripFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_trip, container, false);
        ExpenseViewModel viewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        TextInputEditText participantInput = view.findViewById(R.id.studentsInput);
        dateInput = view.findViewById(R.id.dateInput);

        ViewUtils.bindEditText(participantInput, value -> {
            try {
                viewModel.setParticipantCount(Integer.parseInt(value));
                viewModel.calculatePayments();
            } catch (NumberFormatException e) {
                viewModel.setParticipantCount(0);
            }
        });

        ViewUtils.bindEditText(dateInput, value -> {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(dateInput.getText(), formatter);
                Period period = Period.between(LocalDate.now(), date);
                int months = period.getYears() * 12 + period.getMonths(); // total months
                viewModel.setMonths(months);
                viewModel.calculatePayments();
            } catch (NumberFormatException e) {
                viewModel.setMonths(0);
            }
        });

        viewModel.getTotalCost().observe(getViewLifecycleOwner(), total -> {
            TextView totalCostText = view.findViewById(R.id.totalCostText);
            totalCostText.setText(getContext().getString(R.string.field_total_cost, total));
        });

        viewModel.getMonths().observe(getViewLifecycleOwner(), months -> {
            TextView daysLeftText = view.findViewById(R.id.daysLeftText);
            daysLeftText.setText(getContext().getString(R.string.field_months_left, months));
        });

        viewModel.getParticipantCount().observe(getViewLifecycleOwner(), total -> {
            TextView totalPeopleText = view.findViewById(R.id.totalPeopleText);
            totalPeopleText.setText(getContext().getString(R.string.field_total_students, total));
        });

        // numeric keyboard
        Keyboard keyboard = new Keyboard(view);
        // default keyboard disabled
        participantInput.setShowSoftInputOnFocus(false);
        // on_focus listeners
        keyboard.setKeyboardListener(participantInput);
        // Open date picker
        dateInput.setOnClickListener(v -> showDatePicker());

        return view;
    }

    private void showDatePicker() {
        Calendar today = Calendar.getInstance();
        new DatePickerDialog(
                getContext(),
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
