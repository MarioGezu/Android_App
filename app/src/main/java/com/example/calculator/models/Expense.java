package com.example.calculator.models;

public class Expense {
    private final String type;
    private final String name;
    private final int cost;
    private final int discount;

    public Expense(String type, String name, int cost, int discount) {
        this.type = type;
        this.name = name;
        this.cost = cost;
        this.discount = discount;
    }

    public Expense(String type, String name, int cost) {
        this(type, name, cost, 0 );
    }

    public String getType() { return type; }
    public String getName() { return name; }
    public int getCost() { return cost; }
    public int getDiscount() { return discount; }
}
