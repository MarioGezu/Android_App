package com.example.calculator.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import com.example.calculator.models.Expense;

public class ExpenseViewModel extends ViewModel {
    private final MutableLiveData<List<Expense>> expenses = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Expense>> getExpenses() {
        return expenses;
    }

    public void addExpense(Expense expense) {
        List<Expense> current = expenses.getValue();
        current.add(expense);
        expenses.setValue(current);
    }
}
