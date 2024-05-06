package com.example.trackmyworkout;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.trackmyworkout.DB.Database;
import com.example.trackmyworkout.DB.ExerciseDao;
import com.example.trackmyworkout.DB.UserByExerciseDAO;
import com.example.trackmyworkout.DB.UserDao;
import com.example.trackmyworkout.DB.UserTable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ExerciseDaoTest {
    private Database database;
    private UserDao userDao;
    private ExerciseDao exerciseDao;
    private UserByExerciseDAO userByExerciseDAO;
    private final String username = "Test";
    private final String email = "test@gmail.com";
    private final String password = "qwerty";
    private final String exerciseName = "Exercise Name";

    @Before
    public void initDB(){
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, Database.class).build();
        userDao = database.TMWDao();
        exerciseDao = database.EXDao();
        userByExerciseDAO = database.UBEDao();
    }

    @After
    public void closeDB(){
        database.close();
    }

    @Test
    public void insertExerciseTest(){
        UserTable user = new UserTable(0,username,email,password,false);
        int userId = Math.toIntExact(userDao.insertUser(user));
        // Assert User in DB
        assertEquals(username,userDao.getName(userId));
        // Init exercise using helper method -> link the exercise with the user
        int exerciseId = exerciseDao.syncExerciseWithUser(userId, exerciseName,userByExerciseDAO);
        // Assert exercise in DB
        assertEquals(exerciseName,exerciseDao.nameById(exerciseId));
    }

    @Test
    public void updateExerciseTest(){
        UserTable user = new UserTable(0,username,email,password,false);
        int userId = Math.toIntExact(userDao.insertUser(user));
        // Assert User in DB
        assertEquals(username,userDao.getName(userId));
        // Init exercise using helper method -> link the exercise with the user
        int exerciseId = exerciseDao.syncExerciseWithUser(userId, exerciseName,userByExerciseDAO);
        // Assert exercise in DB
        assertEquals(exerciseName,exerciseDao.nameById(exerciseId));
        // Assert exercise update name
        String newName = "Exercise Name 2";
        exerciseDao.updateExerciseName(exerciseId,newName);
        assertEquals(newName,exerciseDao.nameById(exerciseId));
    }

    @Test
    public void deleteExerciseTest(){
        UserTable user = new UserTable(0,username,email,password,false);
        int userId = Math.toIntExact(userDao.insertUser(user));
        // Assert User in DB
        assertEquals(username,userDao.getName(userId));
        // Init exercise using helper method -> link the exercise with the user
        int exerciseId = exerciseDao.syncExerciseWithUser(userId, exerciseName,userByExerciseDAO);
        // Assert exercise in DB
        assertEquals(exerciseName,exerciseDao.nameById(exerciseId));
        // Assert delete exercise
        exerciseDao.deleteExercise(exerciseId);
        assertNull(exerciseDao.nameById(exerciseId));
    }
}
