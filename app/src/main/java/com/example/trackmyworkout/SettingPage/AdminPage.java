package com.example.trackmyworkout.SettingPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trackmyworkout.ConversionPage;
import com.example.trackmyworkout.DB.UserTable;
import com.example.trackmyworkout.LandingPage.ExerciseAdapter;
import com.example.trackmyworkout.LandingPage.Exercise;
import com.example.trackmyworkout.DB.Database;
import com.example.trackmyworkout.DB.UserByExerciseDAO;
import com.example.trackmyworkout.DB.UserDao;
import com.example.trackmyworkout.LandingPage.LandingPage;
import com.example.trackmyworkout.R;
import com.example.trackmyworkout.WorkoutPage.WorkoutPage;
import com.example.trackmyworkout.databinding.ActivityAdminPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AdminPage extends AppCompatActivity {

    private ExerciseAdapter adapter;

    UserDao userDao;
    UserByExerciseDAO userByExerciseDAO;

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
        userByExerciseDAO = db.UBEDao();



        // The following part is for the Bottom Navigation Bar
        ActivityAdminPageBinding binding = ActivityAdminPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int temp = item.getItemId();
                if (R.id.home == temp) {
                    intent = new Intent(AdminPage.this, LandingPage.class);
                    startActivity(intent);
                } else if (R.id.workout == temp){
                    intent = new Intent(AdminPage.this, WorkoutPage.class);
                    startActivity(intent);
                } else if (R.id.converter == temp) {
                    intent = new Intent(AdminPage.this, ConversionPage.class);
                    startActivity(intent);
                } else if (R.id.settings == temp) {
                    intent = new Intent(AdminPage.this, SettingsPage.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        // The above part is for the Bottom Navigation Bar

        // The following part is for the User RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewWorkouts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // User list initialisation using DB
        ArrayList<Exercise> users = new ArrayList<>();
        List<Integer> iDlist = userDao.selectAllId();

        for (Integer id : iDlist) {
            double admin = 0.0;
            if (userDao.isAdmin(id)) {
                admin = 1.0;
            }
            users.add(new Exercise(userDao.getName(id), admin, id));
        }

        adapter = new ExerciseAdapter(users);
        adapter.setOnItemLongClickListener(position -> {
            // Dialog for user delete
            new AlertDialog.Builder(this).setTitle("Confirm User Delete")
                    .setMessage("Are you sure you want to delete this user ?")
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (userId == users.get(position).getExId()) {
                                Toast.makeText(AdminPage.this, "You can't delete yourself via the admin page.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            } else {
                                userByExerciseDAO.deleteAllExerciseByUserId(users.get(position).getExId());
                                userDao.deleteAccount(users.get(position).getExId());
                                adapter.removeExercise(position);
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
            return true;
        });
        recyclerView.setAdapter(adapter);
        // The above part is for the Exercise RecyclerView

        // Dialog part for adding user
        FloatingActionButton fabAddWorkout = findViewById(R.id.fabAddWorkout);
        fabAddWorkout.setOnClickListener(view -> {
            showAddUserDialog();
        });
    }

    public void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_user_dialog, null);
        builder.setView(dialogView);

        EditText usernameEditText = dialogView.findViewById(R.id.username);
        EditText emailEditText = dialogView.findViewById(R.id.email);
        EditText passwordEditText = dialogView.findViewById(R.id.password);
        CheckBox isAdminCheckBox = dialogView.findViewById(R.id.isAdmin);
        Button validateButton = dialogView.findViewById(R.id.btn_validate);
        Button cancelButton = dialogView.findViewById(R.id.btn_cancel);

        AlertDialog dialog = builder.create();

        validateButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            boolean isAdmin = isAdminCheckBox.isChecked();

            if (userDao.is_takenUsername(username)) {
                usernameEditText.setError("Username is already taken");
                return;
            }
            if (userDao.is_takenEmail(email) || !email.contains("@")) {
                emailEditText.setError("Invalid email or is already taken");
                return;
            }

            UserTable user = new UserTable(0,username,email,password, isAdmin);
            userDao.insertUser(user);
            adapter.addExercise(new Exercise(username, isAdmin ? 1.0: 0.0, 0));
            adapter.notifyDataSetChanged();

            dialog.dismiss();
        });
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}