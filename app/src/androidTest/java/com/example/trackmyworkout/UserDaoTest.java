package com.example.trackmyworkout;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;


import com.example.trackmyworkout.DB.Database;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.trackmyworkout.DB.UserDao;
import com.example.trackmyworkout.DB.UserTable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserDaoTest {
    private Database database;
    private UserDao userDao;
    private final String username = "Test";
    private final String email = "test@gmail.com";
    private final String password = "qwerty";

    @Before
    public void initDB(){
        Context context = ApplicationProvider.getApplicationContext();
        database = Room.inMemoryDatabaseBuilder(context, Database.class).build();
        userDao = database.TMWDao();
    }

    @After
    public void closeDB(){
        database.close();
    }

    @Test
    public void insertUserTest(){
        UserTable user = new UserTable(0,username,email,password,false);
        int userId = Math.toIntExact(userDao.insertUser(user));
        // Assert User in DB
        assertEquals(username,userDao.getName(userId));
        // Assert username already taken
        assertTrue(userDao.is_takenUsername(username));
        // Assert login email
        Integer emailLoginId = userDao.loginEmail(email,password);
        assertEquals(userId,emailLoginId.intValue());
        // Assert login username
        Integer userLoginId = userDao.loginUsername(username,password);
        assertEquals(userId,userLoginId.intValue());
    }

    @Test
    public void updateUserTest(){
        UserTable user = new UserTable(0,username,email,password,false);
        int userId = Math.toIntExact(userDao.insertUser(user));
        // Assert User in DB
        assertEquals(username,userDao.getName(userId));
        // Assert username update
        String newUsername = "Test2";
        userDao.changeUsername(userId,newUsername);
        assertEquals(newUsername,userDao.getName(userId));
    }

    @Test
    public void deleteUserTest() {
        UserTable user = new UserTable(0,username,email,password,false);
        int userId = Math.toIntExact(userDao.insertUser(user));
        // Assert User in DB
        assertEquals(username,userDao.getName(userId));
        // Assert User deleted
        userDao.deleteAccount(userId);
        Integer userLoginId = userDao.loginUsername(username,password);
        assertNull(userLoginId);
    }
}
