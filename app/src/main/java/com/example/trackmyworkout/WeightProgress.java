package com.example.trackmyworkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.trackmyworkout.DB.Database;
import com.example.trackmyworkout.DB.ExerciseDao;
import com.example.trackmyworkout.DB.UserByExerciseDAO;
import com.example.trackmyworkout.DB.UserDao;
import com.example.trackmyworkout.DB.WeightDAO;
import com.example.trackmyworkout.DB.WeightData;
import com.example.trackmyworkout.DB.WeightTable;
import com.example.trackmyworkout.LandingPage.LandingPage;
import com.example.trackmyworkout.SettingPage.SettingsPage;
import com.example.trackmyworkout.databinding.ActivityWeightProgressBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Date;
import java.util.List;

public class WeightProgress extends AppCompatActivity {

    UserDao userDao;
    UserByExerciseDAO userByExerciseDAO;
    ExerciseDao exerciseDao;
    WeightDAO weightDAO;

    SharedPreferences sharedPreferences;
    int userId;

    int exerciseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);
        exerciseId = getIntent().getIntExtra("exerciseId", -1);

        Database db = Room.databaseBuilder(this, Database.class, "DB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();

        userDao = db.TMWDao();
        exerciseDao = db.EXDao();
        userByExerciseDAO = db.UBEDao();
        weightDAO = db.WDao();

        // The following part is for the Bottom Navigation Bar
        ActivityWeightProgressBinding binding = ActivityWeightProgressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int temp = item.getItemId();
                if (R.id.home == temp) {
                    intent = new Intent(WeightProgress.this, LandingPage.class);
                    startActivity(intent);
                } else if (R.id.workout == temp) {
                    intent = new Intent(WeightProgress.this, WorkoutPage.class);
                    startActivity(intent);
                } else if (R.id.converter == temp) {
                    intent = new Intent(WeightProgress.this, ConversionPage.class);
                    startActivity(intent);
                } else if (R.id.settings == temp) {
                    intent = new Intent(WeightProgress.this, SettingsPage.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        // The above part is for the Bottom Navigation Bar

        // Setup graph and title
        TextView textTitle = findViewById(R.id.tvExerciseName);
        String newTitle = exerciseDao.nameById(exerciseId);
        textTitle.setText(newTitle);
        setupButtonListeners();
        setupGraph();
    }

    private void setupGraph() {
        GraphView graph = findViewById(R.id.graph);

        // Fetch weight data from the database
        List<WeightData> weights = weightDAO.getWeightsByExerciseId(exerciseId);

        // Create DataPoint array from weights
        DataPoint[] points = new DataPoint[weights.size()];
        for (int i = 0; i < weights.size(); i++) {
            points[i] = new DataPoint(weights.get(i).getDate(), weights.get(i).getWeight());
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        graph.addSeries(series);

        // Configure viewport for graph
        Viewport viewport = graph.getViewport();
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(points[0].getX());
        viewport.setMaxX(points[points.length - 1].getX());
        viewport.setScrollable(true);
        GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();
        gridLabelRenderer.setHorizontalLabelsVisible(false);

        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(8);
        series.setColor(getResources().getColor(R.color.c2_electric_purple));
    }

    private void setupButtonListeners() {
        ImageButton imageButton = findViewById(R.id.imageButton);
        Button btnDelete = findViewById(R.id.btnDelete);
        Button btnUpdate = findViewById(R.id.btnUpdate);

        // Back button
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(WeightProgress.this, LandingPage.class);
            startActivity(intent);
        });

        // Delete button
        btnDelete.setOnClickListener(v -> {
            deleteExercise();
        });

        // Update button
        btnUpdate.setOnClickListener(v -> {
            updateExercise();
        });
    }

    // Confirm before deletion
    private void deleteExercise() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this exercise ?")
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    // Positive button action
                    int exerciseId = getIntent().getIntExtra("exerciseId", -1);
                    if (exerciseId != -1) {
                        exerciseDao.deleteExercise(exerciseId);
                        userByExerciseDAO.deleteUserByExercise(exerciseId);
                        Intent intent = new Intent(WeightProgress.this, LandingPage.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void updateExercise() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_update_weight, null);
        EditText editTextNewWeight = dialogView.findViewById(R.id.editTextNewWeight);
        editTextNewWeight.setText(weightDAO.getLastWeightByExerciseId(exerciseId).toString());

        // Create the dialog
        new AlertDialog.Builder(this)
                .setTitle("Update Weight")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    String weightStr = editTextNewWeight.getText().toString();
                    if (!weightStr.isEmpty()) {
                        try {
                            double newWeight = Double.parseDouble(weightStr);
                            WeightTable weight = new WeightTable(0, exerciseId, newWeight, new Date());
                            weightDAO.weightInsert(weight);
                            setupGraph();
                        } catch (NumberFormatException e) {
                            editTextNewWeight.setError("Please enter a valid number.");
                            return;
                        }
                    } else {
                        editTextNewWeight.setError("Weight cannot be empty.");
                        return;
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}