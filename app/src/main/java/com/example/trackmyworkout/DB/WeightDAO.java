package com.example.trackmyworkout.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface WeightDAO {

    @Insert
    void weightInsert(WeightTable weightTable);

    @Query("SELECT weight, date FROM WeightTable WHERE exerciseId = :exerciseId")
    List<WeightData> getWeightsByExerciseId(int exerciseId);

    @Query("SELECT weight FROM WeightTable WHERE exerciseId = :exerciseId ORDER BY date DESC LIMIT 1")
    Double getLastWeightByExerciseId(int exerciseId);

    @Query("UPDATE weighttable SET weight = :weight WHERE exerciseId =:exerciseId AND date =:date")
    void updateWeight(int exerciseId, Date date, double weight);
    @Query("DELETE FROM weighttable WHERE exerciseId =:exerciseId AND date =:date")
    void deleteWeight(int exerciseId, Date date);

}
