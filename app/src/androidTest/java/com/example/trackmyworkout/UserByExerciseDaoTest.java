package com.example.trackmyworkout;

import static junit.framework.TestCase.assertEquals;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.trackmyworkout.DB.Database;
import com.example.trackmyworkout.DB.ExerciseDao;
import com.example.trackmyworkout.DB.UserByExerciseDAO;
import com.example.trackmyworkout.DB.UserByExerciseTable;
import com.example.trackmyworkout.DB.UserDao;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class UserByExerciseDaoTest {

    private Database database;
    private UserByExerciseDAO userByExerciseDAO;
    private final int userIdTest = 1;
    private final int exerciseIdTest = 2;

    @Before
    public void initDB(){
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, Database.class).build();
        userByExerciseDAO = database.UBEDao();
    }

    @After
    public void closeDB(){
        database.close();
    }

    @Test
    public void insertUbeTest(){
        UserByExerciseTable userByExercise = new UserByExerciseTable(userIdTest, exerciseIdTest);
        userByExerciseDAO.userByExerciseInsert(userByExercise);
        // Assert insert userId By ExerciseId
        List<Integer> userExercises = userByExerciseDAO.getExerciseIdByUser(userIdTest);
        assertEquals(1,userExercises.size());
    }

    // No update case in this table

    @Test
    public void deleteUbeTest(){
        UserByExerciseTable userByExercise = new UserByExerciseTable(userIdTest, exerciseIdTest);
        userByExerciseDAO.userByExerciseInsert(userByExercise);
        UserByExerciseTable userByExercise2 = new UserByExerciseTable(userIdTest, 3);
        userByExerciseDAO.userByExerciseInsert(userByExercise2);
        // Assert insert userId By ExerciseId
        List<Integer> userExercises = userByExerciseDAO.getExerciseIdByUser(userIdTest);
        assertEquals(2,userExercises.size());
        // Assert exercise delete
        userByExerciseDAO.deleteUserByExercise(exerciseIdTest);
        List<Integer> userExercise = userByExerciseDAO.getExerciseIdByUser(userIdTest);
        assertEquals(1,userExercise.size());
    }
}
