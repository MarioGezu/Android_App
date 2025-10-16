package com.example.caclulator;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


import android.widget.Toast;

public class FieldTripFragment extends Fragment {

    private TextInputEditText studentsInput, teachersInput, dateInput;
    private TextView daysLeftText, totalPeopleText, totalCostText;
    private LinearLayout customKeyboard;
    private TextInputEditText activeInput;
    private Calendar selectedDate = Calendar.getInstance();

    public FieldTripFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field_trip, container, false);

        studentsInput = view.findViewById(R.id.studentsInput);
        teachersInput = view.findViewById(R.id.teachersInput);
        dateInput = view.findViewById(R.id.dateInput);

        daysLeftText = view.findViewById(R.id.daysLeftText);
        totalPeopleText = view.findViewById(R.id.totalPeopleText);
        totalCostText = view.findViewById(R.id.totalCostText);



        // numeric keyboard
        customKeyboard = view.findViewById(R.id.customKeyboardInclude);

        // default keyboard disabled
        studentsInput.setShowSoftInputOnFocus(false);
        teachersInput.setShowSoftInputOnFocus(false);


        // Open date picker
        dateInput.setOnClickListener(v -> showDatePicker());
        // Focus listeners
        studentsInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) showKeyboard(studentsInput);
        });
        teachersInput.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) showKeyboard(teachersInput);
        });
        initKeyboard(view);
        return view;
    }
    private void showKeyboard(TextInputEditText input) {
        activeInput = input;
        customKeyboard.setVisibility(View.VISIBLE);
    }

    private void hideKeyboard() {
        customKeyboard.setVisibility(View.GONE);
        activeInput = null;
    }

    private void initKeyboard(View view) {
        int[] keyIds = {
                R.id.key0, R.id.key1, R.id.key2, R.id.key3,
                R.id.key4, R.id.key5, R.id.key6, R.id.key7,
                R.id.key8, R.id.key9
        };

        for (int id : keyIds) {
            Button key = view.findViewById(id);
            key.setOnClickListener(v -> appendText(key.getText().toString()));
        }

        view.findViewById(R.id.keyBackspace).setOnClickListener(v -> removeLastChar());
        view.findViewById(R.id.keyDone).setOnClickListener(v -> hideKeyboard());
    }

    private void appendText(String value) {
        if (activeInput == null) return;
        Editable text = activeInput.getText();
        if (text == null) return;
        text.append(value);
    }

    private void removeLastChar() {
        if (activeInput == null) return;
        Editable text = activeInput.getText();
        if (text != null && text.length() > 0) {
            text.delete(text.length() - 1, text.length());
        }
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

    private void calculateTrip() {
        int students = 0;
        int teachers = 0;

        try {
            students = Integer.parseInt(studentsInput.getText().toString());
        } catch (NumberFormatException ignored) {}

        try {
            teachers = Integer.parseInt(teachersInput.getText().toString());
        } catch (NumberFormatException ignored) {}

        int totalPeople = students + teachers;
        totalPeopleText.setText("Total People: " + totalPeople);

        // Example cost: $20 per person
        double totalCost = totalPeople * 20.0;
        totalCostText.setText(String.format(Locale.getDefault(), "Total Cost: $%.2f", totalCost));

        // Days left
        long diff = selectedDate.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        long daysLeft = TimeUnit.MILLISECONDS.toDays(diff);
        if (daysLeft >= 0) {
            daysLeftText.setText("Days Left: " + daysLeft);
        } else {
            daysLeftText.setText("Trip date passed");
        }

        Toast.makeText(getContext(), "Trip info updated", Toast.LENGTH_SHORT).show();
    }
}
