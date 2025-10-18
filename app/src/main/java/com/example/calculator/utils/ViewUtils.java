package com.example.calculator.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class ViewUtils {

    /**
     * Binds an EditText to a setter in the ViewModel, updating on text change and/or focus loss.
     *
     * @param editText The EditText to monitor
     * @param onChange Callback to execute when the value changes
     */
    public static void bindEditText(EditText editText, ValueChangeCallback onChange) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onChange.onValueChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                onChange.onValueChanged(editText.getText().toString());
            }
        });
    }

    // Functional interface for callback
    public interface ValueChangeCallback {
        void onValueChanged(String value);
    }
}
