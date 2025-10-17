package com.example.caclulator.models;

public class Expense {
    private String type;
    private String name;
    private double cost;

    public Expense(String type, String name, double cost) {
        this.type = type;
        this.name = name;
        this.cost = cost;
    }

    public String getType() { return type; }
    public String getName() { return name; }
    public double getCost() { return cost; }
}
