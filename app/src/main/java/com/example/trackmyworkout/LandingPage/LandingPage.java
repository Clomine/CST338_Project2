package com.example.trackmyworkout.LandingPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.trackmyworkout.ConversionPage;
import com.example.trackmyworkout.DB.Database;
import com.example.trackmyworkout.DB.ExerciseDao;
import com.example.trackmyworkout.DB.ExerciseTable;
import com.example.trackmyworkout.DB.UserByExerciseDAO;
import com.example.trackmyworkout.DB.WeightDAO;
import com.example.trackmyworkout.DB.WeightTable;
import com.example.trackmyworkout.R;
import com.example.trackmyworkout.SettingsPage;
import com.example.trackmyworkout.WorkoutPage;
import com.example.trackmyworkout.databinding.ActivityLandingPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LandingPage extends AppCompatActivity {

    private ExerciseAdapter adapter;

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

        exerciseDao = db.EXDao();
        userByExerciseDAO = db.UBEDao();
        weightDAO = db.WDao();



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
        RecyclerView recyclerView = findViewById(R.id.recyclerViewWorkouts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Exercises list initialisation using DB
        ArrayList<Exercise> exercises = new ArrayList<>();
        List<Integer> iDlist = userByExerciseDAO.getExerciseIdByUser(userId);

        for (Integer id : iDlist) {
            exercises.add(new Exercise(exerciseDao.nameById(id), weightDAO.getLastWeightByExerciseId(id), id));
        }

        adapter = new ExerciseAdapter(exercises);
        adapter.setOnItemLongClickListener(position -> {
            showEditDeleteDialog(position);
            return true;
        });
        // The above part is for the Exercise RecyclerView

        recyclerView.setAdapter(adapter);

        // Dialog part for adding element when clicking on the add button
        FloatingActionButton fabAddWorkout = findViewById(R.id.fabAddWorkout);
        fabAddWorkout.setOnClickListener(view -> {
            showAddDialog();
        });
    }

    // Editing Recycler view using long click on exercise's
    private void showEditDeleteDialog(final int position) {
        Exercise exercise = adapter.getExercises().get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Exercise");

        // Custom layout for dialog
        final View customLayout = getLayoutInflater().inflate(R.layout.edit_exercise_dialog, null);
        builder.setView(customLayout);

        // Set up the input fields
        final EditText nameInput = customLayout.findViewById(R.id.editTextExerciseName);
        final EditText weightInput = customLayout.findViewById(R.id.editTextExerciseWeight);
        nameInput.setText(exercise.getName());
        weightInput.setText(String.valueOf(exercise.getWeight()));

        // Set up the buttons
        builder.setPositiveButton("Save", null);
        builder.setNegativeButton("Cancel", null);

        builder.setNeutralButton("Delete", (dialog, which) -> {
            // Delete exercise in DB
            exerciseDao.deleteExercise(adapter.getExercises().get(position).getExId());
            userByExerciseDAO.deleteUserByExercise(adapter.getExercises().get(position).getExId());

            adapter.removeExercise(position);
            adapter.notifyDataSetChanged();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        // Overriding the OnClickListener to prevent dialog from closing on positive button if validation fails
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            String newName = nameInput.getText().toString();
            String newWeightStr = weightInput.getText().toString();

            if (newName.isEmpty()) {
                nameInput.setError("Workout name is required!");
                return;
            }
            double newWeight;
            try {
                newWeight = Double.parseDouble(newWeightStr);
            } catch (NumberFormatException e) {
                weightInput.setError("Valid weight is required!");
                return;
            }

            // Change name & weight in Adapter
            exercise.setName(newName);
            exercise.setWeight(newWeight);

            // Modify name and weight in DB
            exerciseDao.updateExerciseName(adapter.getExercises().get(position).getExId(), newName);
            WeightTable weight = new WeightTable(0, adapter.getExercises().get(position).getExId(), newWeight, new Date());
            weightDAO.weightInsert(weight);

            adapter.notifyItemChanged(position);
            dialog.dismiss();
        });
    }

    // Adding to the Recycler view using the Floating button
    private void showAddDialog() {
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

            // Adding new Exercise to DB
            int exerciseId = exerciseDao.syncExerciseWithUser(userId, name,userByExerciseDAO);
            WeightTable weightTable = new WeightTable(0, exerciseId, weight, new Date());
            weightDAO.weightInsert(weightTable);

            // Adding new Exercise to Adapter
            adapter.addExercise(new Exercise(name, weight, exerciseId));
            dialog.dismiss();
        });
    }
}