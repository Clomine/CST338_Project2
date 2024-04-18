package com.example.trackmyworkout;

public class Exercise {
    private final String name;
    private final double weight;

    public Exercise(String name, double weight) {
        this.name = name;
        this.weight = Math.round(weight * 100.0) / 100.0;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }
}