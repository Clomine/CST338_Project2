package com.example.trackmyworkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.trackmyworkout.databinding.ActivityLandingPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class LandingPage extends AppCompatActivity {

    // Argument for exercise
    private RecyclerView recyclerView;
    private ExerciseAdapter adapter;
    private ArrayList<Exercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The following part is for the Bottom Navigation Bar
        ActivityLandingPageBinding binding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int temp = item.getItemId();
                if (R.id.home == temp) {
                    intent = new Intent(LandingPage.this, LandingPage.class);
                    startActivity(intent);
                } else if (R.id.workout == temp){
                    intent = new Intent(LandingPage.this, WorkoutPage.class);
                    startActivity(intent);
                } else if (R.id.converter == temp) {
                    intent = new Intent(LandingPage.this, ConversionPage.class);
                    startActivity(intent);
                } else if (R.id.settings == temp) {
                    intent = new Intent(LandingPage.this, SettingsPage.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        // The above part is for the Bottom Navigation Bar

        // The following part is for the Exercise RecyclerView
        recyclerView = findViewById(R.id.recyclerViewWorkouts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        exercises = new ArrayList<>();
        adapter = new ExerciseAdapter(exercises);
        recyclerView.setAdapter(adapter);

            // Dialog part
        FloatingActionButton fabAddWorkout = findViewById(R.id.fabAddWorkout);
        fabAddWorkout.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_exercise, null);
            builder.setView(dialogView);

            EditText workoutNameInput = dialogView.findViewById(R.id.editTextWorkoutName);
            EditText workoutWeightInput = dialogView.findViewById(R.id.editTextWorkoutWeight);

            builder.setPositiveButton("Add", null);
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String name = workoutNameInput.getText().toString().trim();
                String weightStr = workoutWeightInput.getText().toString().trim();
                if (name.isEmpty()) {
                    workoutNameInput.setError("Workout name is required!");
                    return;
                }
                double weight = 0;
                try {
                    weight = Double.parseDouble(weightStr);
                } catch (NumberFormatException e) {
                    workoutWeightInput.setError("Valid weight is required!");
                    return;
                }
                addWorkout(new Exercise(name, weight));
                dialog.dismiss();
            });
        });
    }

    private void addWorkout(Exercise workout) {
        exercises.add(workout);
        adapter.notifyItemInserted(exercises.size() - 1);
    }
    // The above part is for the Exercise RecyclerView
}