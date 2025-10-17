package com.example.caclulator.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caclulator.R;
import com.example.caclulator.models.Expense;

import java.util.List;

// Áthidalja a költségeket(Expense) a RecyclerView, dinamikus listával, amely a item_expense.xml alapján listázza ki a
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private List<Expense> expenseList;

    public ExpenseAdapter(List<Expense> expenseList) {
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.expenseType.setText("Type: " + expense.getType());
        holder.expenseName.setText("Name: " + expense.getName());
        holder.expenseCost.setText("Cost: $" + expense.getCost());
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView expenseType, expenseName, expenseCost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseType = itemView.findViewById(R.id.expenseType);
            expenseName = itemView.findViewById(R.id.expenseName);
            expenseCost = itemView.findViewById(R.id.expenseCost);
        }
    }
}
