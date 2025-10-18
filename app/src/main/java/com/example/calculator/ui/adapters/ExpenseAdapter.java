package com.example.calculator.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculator.R;
import com.example.calculator.models.Expense;
import java.util.List;

// Áthidalja a költségeket(Expense) a RecyclerView, dinamikus listával, amely a item_expense.xml alapján listázza ki a
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private final List<Expense> expenseList;
    private final Context context;
    public ExpenseAdapter(Context context, List<Expense> expenseList) {
        this.expenseList = expenseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.expenseType.setText(context.getString(R.string.label_type, expense.getType()));
        holder.expenseName.setText(context.getString(R.string.label_name, expense.getName()));
        holder.expenseCost.setText(context.getString(R.string.label_cost, expense.getCost() + " ft"));
        if (expense.getDiscount() > 0) {
            holder.expenseDiscount.setVisibility(View.VISIBLE);
            holder.expenseDiscount.setText(expense.getDiscount() + '%');
        }
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView expenseType, expenseName, expenseCost, expenseDiscount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseType = itemView.findViewById(R.id.expenseType);
            expenseName = itemView.findViewById(R.id.expenseName);
            expenseCost = itemView.findViewById(R.id.expenseCost);
            expenseDiscount = itemView.findViewById(R.id.expenseDiscount);
        }
    }
}
