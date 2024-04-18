package com.example.trackmyworkout;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {UserTable.class}, version = 1)
public abstract class Database extends RoomDatabase {

    public static final String USER_TABLE = "UserTable";
    private static final String DATABASE_NAME = "TMW.DB";

    private static volatile Database instance;
    private static final Object LOCK = new Object();
    public abstract UserDao TMWDao();

    public static Database getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            Database.class,
                            DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}
