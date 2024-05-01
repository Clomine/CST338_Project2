package com.example.trackmyworkout.DB;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = Database.USERBYEXERCISE_TABLE,
        primaryKeys = {"userId", "exerciseId"},
        foreignKeys = {
                @ForeignKey(
                        entity = UserTable.class,
                        parentColumns = "userId",
                        childColumns = "userId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = ExerciseTable.class,
                        parentColumns = "exerciseId",
                        childColumns = "exerciseId",
                        onDelete = ForeignKey.CASCADE
                )
        })
public class UserByExerciseTable {

    private int userId;
    private int exerciseId;

    public UserByExerciseTable(int userId, int exerciseId) {
        this.userId = userId;
        this.exerciseId = exerciseId;
    }

    public int getUserId() {
        return userId;
    }

    public int getExerciseId() {
        return exerciseId;
    }
}
