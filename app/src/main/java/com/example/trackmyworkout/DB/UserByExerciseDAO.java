package com.example.trackmyworkout.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserByExerciseDAO {

    @Insert
    void userByExerciseInsert(UserByExerciseTable userByExerciseTable);

    @Query("SELECT exerciseId FROM userbyexercisetable WHERE userId = :userId")
    List<Integer> getExerciseIdByUser(int userId);

    @Query("DELETE FROM UserByExerciseTable WHERE exerciseId = :exerciseId")
    void deleteUserByExercise(int exerciseId);

    @Query("DELETE FROM UserByExerciseTable WHERE userId = :userId")
    void deleteAllExerciseByUserId(int userId);
}
