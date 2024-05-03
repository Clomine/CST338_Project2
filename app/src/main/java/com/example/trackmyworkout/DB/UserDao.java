package com.example.trackmyworkout.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    Long insertUser(UserTable userTable);

    @Query("SELECT EXISTS (SELECT * FROM UserTable WHERE username = :username)")
    boolean is_takenUsername(String username);

    @Query("SELECT EXISTS (SELECT * FROM UserTable WHERE email = :email)")
    boolean is_takenEmail(String email);

    @Query("SELECT userId FROM UserTable where username = :username AND password = :password")
    Integer loginUsername(String username, String password);

    @Query("SELECT userId FROM UserTable where email = :email AND password = :password")
    Integer loginEmail(String email, String password);

    @Query("SELECT EXISTS (SELECT * FROM UserTable where userId = :userId AND isAdmin = 1)")
    boolean isAdmin(int userId);

    @Query("SELECT username FROM UserTable where userId = :userId")
    String getName(int userId);
}
