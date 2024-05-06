package com.example.trackmyworkout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Insert;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.trackmyworkout.DB.Database;
import com.example.trackmyworkout.DB.ExerciseDao;
import com.example.trackmyworkout.DB.ExerciseTable;
import com.example.trackmyworkout.DB.UserByExerciseDAO;
import com.example.trackmyworkout.DB.UserDao;
import com.example.trackmyworkout.DB.WeightDAO;
import com.example.trackmyworkout.DB.WeightTable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class WeightDaoTest {
    private Database database;

    private WeightDAO weightDao;
    private ExerciseDao exerciseDao;
    private final String exerciseName = "Exercise Name";
    private final double weight = 10.00;
    private final Date date = new Date();

    @Before
    public void initDB(){
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, Database.class).build();
        exerciseDao = database.EXDao();
        weightDao = database.WDao();
    }

    @After
    public void closeDB(){
        database.close();
    }

    @Test
    public void insertWeightTest() {
        // Init exercise using helper method -> link the exercise with the user
        ExerciseTable exerciseTable = new ExerciseTable(0,exerciseName);
        int exerciseId = Math.toIntExact(exerciseDao.exerciseInsert(exerciseTable));
        // Assert exercise in DB
        assertEquals(exerciseName,exerciseDao.nameById(exerciseId));
        // Assert weight in DB
        WeightTable weightTable = new WeightTable(0,exerciseId,weight,date);
        weightDao.weightInsert(weightTable);
        assertNotNull(weightDao.getLastWeightByExerciseId(exerciseId));
    }

    @Test
    public void updateWeightTest() {
        // Init exercise using helper method -> link the exercise with the user
        ExerciseTable exerciseTable = new ExerciseTable(0,exerciseName);
        int exerciseId = Math.toIntExact(exerciseDao.exerciseInsert(exerciseTable));
        // Assert exercise in DB
        assertEquals(exerciseName,exerciseDao.nameById(exerciseId));
        // Assert weight in DB
        WeightTable weightTable = new WeightTable(0,exerciseId,weight,date);
        weightDao.weightInsert(weightTable);
        assertNotNull(weightDao.getLastWeightByExerciseId(exerciseId));
        // Assert Update Weight
        Double newWeight = 20.00;
        weightDao.updateWeight(exerciseId,date,newWeight);
        assertEquals(newWeight,weightDao.getLastWeightByExerciseId(exerciseId));
    }

    @Test
    public void deleteWeightTest() {
        // Init exercise using helper method -> link the exercise with the user
        ExerciseTable exerciseTable = new ExerciseTable(0,exerciseName);
        int exerciseId = Math.toIntExact(exerciseDao.exerciseInsert(exerciseTable));
        // Assert exercise in DB
        assertEquals(exerciseName,exerciseDao.nameById(exerciseId));
        // Assert weight in DB
        WeightTable weightTable = new WeightTable(0,exerciseId,weight,date);
        weightDao.weightInsert(weightTable);
        assertNotNull(weightDao.getLastWeightByExerciseId(exerciseId));
        // Assert delete weight
        weightDao.deleteWeight(exerciseId,date);
        assertNull(weightDao.getLastWeightByExerciseId(exerciseId));
    }
}
