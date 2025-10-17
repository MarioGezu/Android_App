package com.example.caclulator.models;

public class Expense {
    private String type;
    private String name;
    private double cost;
    private int discount;

    public Expense(String type, String name, double cost, int discount) {
        this.type = type;
        this.name = name;
        this.cost = cost;
        this.discount = discount;
    }

    public Expense(String type, String name, double cost) {
        this(type, name, cost, 0 );
    }

    public String getType() { return type; }
    public String getName() { return name; }
    public double getCost() { return cost; }
}
