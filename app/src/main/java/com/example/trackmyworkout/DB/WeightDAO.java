package com.example.trackmyworkout.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WeightDAO {

    @Insert
    void weightInsert(WeightTable weightTable);

    @Query("SELECT weight, date FROM WeightTable WHERE exerciseId = :exerciseId")
    List<Object[]> getWeightsByExerciseId(int exerciseId);

    @Query("SELECT weight FROM WeightTable WHERE exerciseId = :exerciseId ORDER BY date DESC LIMIT 1")
    Double getLastWeightByExerciseId(int exerciseId);

}
