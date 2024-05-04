package com.example.trackmyworkout.LandingPage;

public class Exercise {
    private String name;
    private double weight;

    private int exId;
    private boolean isSelected;

    public Exercise(String name, double weight, int exId) {
        this.name = name;
        this.weight = Math.round(weight * 100.0) / 100.0;
        this.exId = exId;
        this.isSelected = false;
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

    public int getExId() {
        return exId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}