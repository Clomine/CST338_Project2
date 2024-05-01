package com.example.trackmyworkout.LandingPage;

public class Exercise {
    private String name;
    private double weight;

    private int exId;

    public Exercise(String name, double weight, int exId) {
        this.name = name;
        this.weight = Math.round(weight * 100.0) / 100.0;
        this.exId = exId;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public void setName(String nam) {
        name = nam;
    }

    public void setWeight(double weigh) {
        weight = weigh;
    }
}