package com.example.calculator.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.calculator.models.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseViewModel extends ViewModel {

    private final MutableLiveData<List<Expense>> expenses = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Double> totalCost = new MutableLiveData<>(0.0);
    private final MutableLiveData<Integer> participantCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> months = new MutableLiveData<>(1);
    private final MutableLiveData<Double> monthlyPaymentPerPerson = new MutableLiveData<>(0.0);
    private final MutableLiveData<Double> totalPaymentPerPerson = new MutableLiveData<>(0.0);

    public LiveData<List<Expense>> getExpenses() {
        return expenses;
    }

    public LiveData<Double> getTotalCost() {
        return totalCost;
    }

    public LiveData<Integer> getParticipantCount() {
        return participantCount;
    }

    public LiveData<Integer> getMonths() {
        return months;
    }

    public LiveData<Double> getMonthlyPaymentPerPerson() {
        return monthlyPaymentPerPerson;
    }

    public LiveData<Double> getTotalPaymentPerPerson() {
        return totalPaymentPerPerson;
    }

    public void setParticipantCount(int value) {
        participantCount.setValue(value);
        calculatePayments();
    }

    public void setMonths(int value) {
        months.setValue(value);
        calculatePayments();
    }

    public void addExpense(Expense expense) {
        List<Expense> current = expenses.getValue();
        if (current != null) {
            current.add(expense);
            expenses.setValue(current);
            recalculateTotalCost();
        }
    }

    public void delExpense(int pos) {
        List<Expense> current = expenses.getValue();
        if (current != null && pos >= 0 && pos < current.size()) {
            current.remove(pos);
            expenses.setValue(current);
            recalculateTotalCost();
        }
    }

    private void recalculateTotalCost() {
        totalCost.setValue(calculateTotalCost());
        calculatePayments();
    }

    private double calculateTotalCost() {
        List<Expense> current = expenses.getValue();
        double cost = 0.0;
        if (current != null) {
            for (Expense expense : current) {
                cost += expense.getCost() * (1 - expense.getDiscount() / 100.0);
            }
        }
        return cost;
    }

    public void calculatePayments() {
        Double cost = totalCost.getValue();
        Integer people = participantCount.getValue();
        Integer time = months.getValue();

        if (cost == null || people == null || time == null || people == 0 || time == 0) {
            monthlyPaymentPerPerson.setValue(0.0);
            totalPaymentPerPerson.setValue(0.0);
            return;
        }

        double totalPerPerson = cost / people;
        double monthlyPerPerson = totalPerPerson / time;

        totalPaymentPerPerson.setValue(totalPerPerson);
        monthlyPaymentPerPerson.setValue(monthlyPerPerson);
    }
}
