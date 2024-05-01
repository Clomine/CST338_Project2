package com.example.trackmyworkout.DB;

import java.util.Date;

public class WeightData {
    private double weight;
    private Date date;

    public WeightData(double weight, Date date) {
        this.weight = weight;
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public Date getDate() {
        return date;
    }
}
