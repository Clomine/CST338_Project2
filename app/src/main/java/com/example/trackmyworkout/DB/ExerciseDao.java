package com.example.trackmyworkout.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ExerciseDao {

    @Insert
    Long exerciseInsert(ExerciseTable exerciseTable);

    @Query("SELECT name FROM ExerciseTable WHERE exerciseID = :exerciseID")
    String nameById(int exerciseID);

    @Query("DELETE FROM ExerciseTable WHERE exerciseID = :exerciseID")
    int deleteExercise(int exerciseID);

    @Query("UPDATE ExerciseTable SET name = :name WHERE exerciseId = :exerciseId")
    void updateExerciseName(int exerciseId, String name);


    @Insert
    default int syncExerciseWithUser(int userID, String exerciseName, UserByExerciseDAO userByExerciseDAO) {
        int exerciseId = Math.toIntExact(exerciseInsert(new ExerciseTable(0, exerciseName)));
        UserByExerciseTable userByExercise = new UserByExerciseTable(userID, exerciseId);
        userByExerciseDAO.userByExerciseInsert(userByExercise);
        return exerciseId;
    }

}
