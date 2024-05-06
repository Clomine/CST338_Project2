package com.example.trackmyworkout.WorkoutPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.trackmyworkout.ConversionPage;
import com.example.trackmyworkout.DB.Database;
import com.example.trackmyworkout.DB.ExerciseDao;
import com.example.trackmyworkout.DB.UserByExerciseDAO;
import com.example.trackmyworkout.DB.UserDao;
import com.example.trackmyworkout.DB.WeightDAO;
import com.example.trackmyworkout.DB.WeightTable;
import com.example.trackmyworkout.LandingPage.LandingPage;
import com.example.trackmyworkout.R;
import com.example.trackmyworkout.SettingsPage;
import com.example.trackmyworkout.databinding.ActivityCurrentWorkoutBinding;
import com.example.trackmyworkout.databinding.ActivityWorkoutPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurrentWorkout extends AppCompatActivity {

    UserDao userDao;
    UserByExerciseDAO userByExerciseDAO;
    ExerciseDao exerciseDao;
    WeightDAO weightDAO;

    SharedPreferences sharedPreferences;
    int userId;

    private int exIndex = 0;

    ArrayList<Integer> checkedExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_workout);

        // The following part is for the Bottom Navigation Bar
        ActivityCurrentWorkoutBinding binding = ActivityCurrentWorkoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int temp = item.getItemId();
                if (R.id.home == temp) {
                    intent = new Intent(CurrentWorkout.this, LandingPage.class);
                    startActivity(intent);
                } else if (R.id.workout == temp){
                    intent = new Intent(CurrentWorkout.this, WorkoutPage.class);
                    startActivity(intent);
                } else if (R.id.converter == temp) {
                    intent = new Intent(CurrentWorkout.this, ConversionPage.class);
                    startActivity(intent);
                } else if (R.id.settings == temp) {
                    intent = new Intent(CurrentWorkout.this, SettingsPage.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        // The above part is for the Bottom Navigation Bar

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

        checkedExercise = getIntent().getIntegerArrayListExtra("checkedExercise");

        displayExercise(exIndex);

        EditText weightEditText = findViewById(R.id.weightEditText);


        Button nextExercise = findViewById(R.id.nextExerciseButton);
        if (exIndex == checkedExercise.size() - 1) {
            nextExercise.setVisibility(View.INVISIBLE);
        }
            nextExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double weight = Double.parseDouble(weightEditText.getText().toString());
                WeightTable weightTable = new WeightTable(0,checkedExercise.get(exIndex),weight,new Date());
                weightDAO.weightInsert(weightTable);
                exIndex++;
                displayExercise(exIndex);
            }
        });

        Button stopSession = findViewById(R.id.stopSessionButton);
        stopSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double weight = Double.parseDouble(weightEditText.getText().toString());
                WeightTable weightTable = new WeightTable(0,checkedExercise.get(exIndex),weight,new Date());
                weightDAO.weightInsert(weightTable);
                Intent intent = new Intent(CurrentWorkout.this, LandingPage.class);
                startActivity(intent);
            }
        });


    }

    private void displayExercise(int exIndex) {
        Button nextExercise = findViewById(R.id.nextExerciseButton);
        if (exIndex == checkedExercise.size() - 1) {
            nextExercise.setVisibility(View.INVISIBLE);
        }
        String currentExName = exerciseDao.nameById(checkedExercise.get(exIndex));
        String currentExWeight = String.valueOf(weightDAO.getLastWeightByExerciseId(checkedExercise.get(exIndex)));
        TextView exerciseNameTxtView = findViewById(R.id.exerciseNameTextView);
        exerciseNameTxtView.setText(currentExName);
        EditText weightEditText = findViewById(R.id.weightEditText);
        weightEditText.setText(currentExWeight);
    }
}