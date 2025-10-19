package com.example.calculator.ui.dialogs;

import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.calculator.R;
import com.google.android.material.textfield.TextInputEditText;

public class Keyboard {
    private TextInputEditText activeInput;
    private final LinearLayout keyboard;

    public Keyboard(View view) {
        this.keyboard = view.findViewById(R.id.customKeyboardInclude);
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

    public void setKeyboardListener(@NonNull TextInputEditText input) {
        input.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) showKeyboard(input);
            else hideKeyboard();
        });
    }
    private void appendText(String value) {
        if (activeInput == null) return;
        Editable text = activeInput.getText();
        if (text != null) text.append(value);
    }
    private void removeLastChar() {
        if (activeInput == null) return;
        Editable text = activeInput.getText();
        if (text != null && text.length() > 0) {
            text.delete(text.length() - 1, text.length());
        }
    }
    public void showKeyboard(TextInputEditText input) {
        this.activeInput = input;
        this.keyboard.setVisibility(View.VISIBLE);
    }
    public void hideKeyboard() {
        this.keyboard.setVisibility(View.GONE);
        this.activeInput = null;
    }
}
