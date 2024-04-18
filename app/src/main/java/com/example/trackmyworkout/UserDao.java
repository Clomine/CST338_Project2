package com.example.trackmyworkout;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    void insertUser(UserTable userTable);

    @Query("SELECT EXISTS (SELECT * FROM UserTable WHERE username = :username)")
    boolean is_takenUsername(String username);

    @Query("SELECT EXISTS (SELECT * FROM UserTable WHERE email = :email)")
    boolean is_takenEmail(String email);

    @Query("SELECT EXISTS (SELECT * FROM UserTable where username = :username AND password = :password)")
    boolean loginUsername(String username, String password);

    @Query("SELECT EXISTS (SELECT * FROM UserTable where email = :email AND password = :password)")
    boolean loginEmail(String email, String password);
}
