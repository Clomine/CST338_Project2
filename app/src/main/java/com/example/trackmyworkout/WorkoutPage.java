package com.example.trackmyworkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.trackmyworkout.DB.Database;
import com.example.trackmyworkout.DB.ExerciseDao;
import com.example.trackmyworkout.DB.UserByExerciseDAO;
import com.example.trackmyworkout.DB.UserDao;
import com.example.trackmyworkout.DB.WeightDAO;
import com.example.trackmyworkout.LandingPage.Exercise;
import com.example.trackmyworkout.LandingPage.LandingPage;
import com.example.trackmyworkout.databinding.ActivityWorkoutPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class WorkoutPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WorkoutAdapter workoutAdapter;
    private List<Exercise> exerciseList;

    UserDao userDao;
    UserByExerciseDAO userByExerciseDAO;
    ExerciseDao exerciseDao;
    WeightDAO weightDAO;

    SharedPreferences sharedPreferences;
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        Database db = Room.databaseBuilder(this, Database.class, "DB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        userDao = db.TMWDao();
        exerciseDao = db.EXDao();
        userByExerciseDAO = db.UBEDao();
        weightDAO = db.WDao();
        // The following part is for the Bottom Navigation Bar
        ActivityWorkoutPageBinding binding = ActivityWorkoutPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int temp = item.getItemId();
                if (R.id.home == temp) {
                    intent = new Intent(WorkoutPage.this, LandingPage.class);
                    startActivity(intent);
                } else if (R.id.workout == temp){
                    intent = new Intent(WorkoutPage.this, WorkoutPage.class);
                    startActivity(intent);
                } else if (R.id.converter == temp) {
                    intent = new Intent(WorkoutPage.this, ConversionPage.class);
                    startActivity(intent);
                } else if (R.id.settings == temp) {
                    intent = new Intent(WorkoutPage.this, SettingsPage.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        // The above part is for the Bottom Navigation Bar

        recyclerView = findViewById(R.id.recyclerViewWorkouts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exerciseList = new ArrayList<>();
        workoutAdapter = new WorkoutAdapter(exerciseList);
        recyclerView.setAdapter(workoutAdapter);

        List<Integer> iDlist =  userByExerciseDAO.getExerciseIdByUser(userId);

        for (Integer id : iDlist) {
            exerciseList.add(new Exercise(exerciseDao.nameById(id), weightDAO.getLastWeightByExerciseId(id), id));
        }

// Notify the adapter that the data set has changed
        workoutAdapter.notifyDataSetChanged();
    }
}