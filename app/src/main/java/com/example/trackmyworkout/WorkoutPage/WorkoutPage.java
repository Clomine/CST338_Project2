package com.example.trackmyworkout.WorkoutPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.trackmyworkout.ConversionPage;
import com.example.trackmyworkout.DB.Database;
import com.example.trackmyworkout.DB.ExerciseDao;
import com.example.trackmyworkout.DB.UserByExerciseDAO;
import com.example.trackmyworkout.DB.UserDao;
import com.example.trackmyworkout.DB.WeightDAO;
import com.example.trackmyworkout.LandingPage.Exercise;
import com.example.trackmyworkout.LandingPage.LandingPage;
import com.example.trackmyworkout.R;
import com.example.trackmyworkout.SettingPage.SettingsPage;
import com.example.trackmyworkout.databinding.ActivityWorkoutPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
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


        //Recycler View

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

        //Button

        Button startWorkoutButton = findViewById(R.id.StartWorkoutButton);
        startWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (workoutAdapter.checkedExercises.isEmpty()) {
                    Toast.makeText(WorkoutPage.this, "You should add at least one exercise.",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(WorkoutPage.this, CurrentWorkout.class);
                    Log.v("CurrentWorkout", Arrays.toString(workoutAdapter.checkedExercises.toArray()));
                    intent.putIntegerArrayListExtra("checkedExercise", (ArrayList<Integer>) workoutAdapter.checkedExercises);
                    Log.v("getExtra", Arrays.toString(intent.getIntArrayExtra("checkedExercise")));
                    startActivity(intent);
                }

            }
        });
    }


}