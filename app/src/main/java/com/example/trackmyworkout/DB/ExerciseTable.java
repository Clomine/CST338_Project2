package com.example.trackmyworkout.DB;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.trackmyworkout.DB.Database;

@Entity(tableName = Database.EXERCISE_TABLE)
public class ExerciseTable {

    @PrimaryKey(autoGenerate = true)
    private int exerciseId;
    private String name;

    public ExerciseTable(int exerciseId, String name) {
        this.exerciseId = exerciseId;
        this.name = name;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(int exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
