package com.example.trackmyworkout.DB;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.trackmyworkout.Converters;

@androidx.room.Database(entities = {UserTable.class, ExerciseTable.class, UserByExerciseTable.class, WeightTable.class}, version = 4)
@TypeConverters({Converters.class})
public abstract class Database extends RoomDatabase {

    public static final String USER_TABLE = "UserTable";
    public static final String EXERCISE_TABLE = "ExerciseTable";

    public static final String USERBYEXERCISE_TABLE = "UserByExerciseTable";
    public static final String WEIGHT_TABLE = "WeightTable";
    private static final String DATABASE_NAME = "TMW.DB";

    private static volatile Database instance;
    private static final Object LOCK = new Object();
    public abstract UserDao TMWDao();
    public abstract ExerciseDao EXDao();
    public abstract UserByExerciseDAO UBEDao();
    public abstract WeightDAO WDao();

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
