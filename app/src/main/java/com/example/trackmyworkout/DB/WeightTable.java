package com.example.trackmyworkout.DB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.trackmyworkout.DB.Database;
import com.example.trackmyworkout.DB.ExerciseTable;

import java.util.Date;

@Entity(tableName = Database.WEIGHT_TABLE,
        foreignKeys = {
                @ForeignKey(
                        entity = ExerciseTable.class,
                        parentColumns = "exerciseId",
                        childColumns = "exerciseId",
                        onDelete = ForeignKey.CASCADE
                )
        })
public class WeightTable {

    @PrimaryKey(autoGenerate = true)
    private int weightId;
    private int exerciseId;

    private double weight;
    private Date date;

    public WeightTable(int weightId, int exerciseId, double weight, Date date) {
        this.weightId = weightId;
        this.exerciseId = exerciseId;
        this.weight = weight;
        this.date = date;
    }

    public int getWeightId() {
        return weightId;
    }

    public void setWeightId(int weightId) {
        this.weightId = weightId;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
