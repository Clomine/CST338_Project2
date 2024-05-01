package com.example.trackmyworkout.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ExerciseDao {

    @Insert
    int exerciseInsert(ExerciseTable exerciseTable);

    @Query("SELECT name FROM ExerciseTable WHERE exerciseID = :exerciseID")
    String nameById(int exerciseID);

    @Query("DELETE FROM ExerciseTable WHERE exerciseID = :exerciseID")
    int deleteExercise(int exerciseID);


    @Insert
    default void syncExerciseWithUser(int userID, String exerciseName) {
        int exerciseId = exerciseInsert(new ExerciseTable(0,exerciseName));
        UserByExerciseTable userByExercise = new UserByExerciseTable(userID, exerciseId);
        insertUserExercise(userByExercise);
    }

    @Insert
    void insertUserExercise(UserByExerciseTable userByExercise);
}
