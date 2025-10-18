package com.example.calculator.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import com.example.calculator.models.Expense;
public class ExpenseViewModel extends ViewModel {

    // Kiadások listája
    private final MutableLiveData<List<Expense>> expenses = new MutableLiveData<>(new ArrayList<>());

    // Alapvető paraméterek
    private final MutableLiveData<Double> totalCost = new MutableLiveData<>(0.0);
    private final MutableLiveData<Integer> participantCount = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> months = new MutableLiveData<>(1);

    // Diákonkénti havi hozzájárulás
    private final MutableLiveData<List<Double>> pocketMoneyList = new MutableLiveData<>(new ArrayList<>());

    // Eredmények
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

    public LiveData<List<Double>> getPocketMoneyList() {
        return pocketMoneyList;
    }

    public LiveData<Double> getMonthlyPaymentPerPerson() {
        return monthlyPaymentPerPerson;
    }

    public LiveData<Double> getTotalPaymentPerPerson() {
        return totalPaymentPerPerson;
    }

    public void setTotalCost(double value) {
        totalCost.setValue(value);
    }

    public void setParticipantCount(int value) {
        participantCount.setValue(value);
    }

    public void setMonths(int value) {
        months.setValue(value);
    }

    public void setPocketMoneyList(List<Double> list) {
        pocketMoneyList.setValue(list);
    }

    public void addExpense(Expense expense) {
        List<Expense> current = expenses.getValue();
        current.add(expense);
        expenses.setValue(current);
    }

    public void delExpense(int pos) {
        List<Expense> current = expenses.getValue();
        if (pos >= 0 && pos < current.size()) {
            current.remove(pos);
            expenses.setValue(current);
        }
    }

    // Kiszámolja, mennyit kell fizetnie fejenként
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

