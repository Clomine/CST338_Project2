package com.example.trackmyworkout.SettingPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.trackmyworkout.ConversionPage;
import com.example.trackmyworkout.DB.UserByExerciseDAO;
import com.example.trackmyworkout.LandingPage.LandingPage;
import com.example.trackmyworkout.DB.Database;
import com.example.trackmyworkout.DB.UserDao;
import com.example.trackmyworkout.MainActivity;
import com.example.trackmyworkout.R;
import com.example.trackmyworkout.WorkoutPage;
import com.example.trackmyworkout.databinding.ActivitySettingsPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsPage extends AppCompatActivity {

    UserDao userDao;

    UserByExerciseDAO userByExerciseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userDao = Room.databaseBuilder(this,Database.class,"DB").allowMainThreadQueries().build().TMWDao();
        userByExerciseDAO = Room.databaseBuilder(this,Database.class,"DB").allowMainThreadQueries().build().UBEDao();

        // The following part is for the Bottom Navigation Bar
        ActivitySettingsPageBinding binding = ActivitySettingsPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int temp = item.getItemId();
                if (R.id.home == temp) {
                    intent = new Intent(SettingsPage.this, LandingPage.class);
                    startActivity(intent);
                } else if (R.id.workout == temp){
                    intent = new Intent(SettingsPage.this, WorkoutPage.class);
                    startActivity(intent);
                } else if (R.id.converter == temp) {
                    intent = new Intent(SettingsPage.this, ConversionPage.class);
                    startActivity(intent);
                } else if (R.id.settings == temp) {
                    intent = new Intent(SettingsPage.this, SettingsPage.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        // The above part is for the Bottom Navigation Bar

        TextView textViewOption1 = findViewById(R.id.textViewOption1);
        TextView textViewOption2 = findViewById(R.id.textViewOption2);
        TextView textViewOption3 = findViewById(R.id.textViewOption3);
        TextView textViewOption4 = findViewById(R.id.textViewOption4);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId", -1);

        textViewOption1.setOnClickListener(new View.OnClickListener() {
            // LBS or Kg (GONE FOR NOW) !!!
            @Override
            public void onClick(View view) {

            }
        });
        textViewOption2.setOnClickListener(new View.OnClickListener() {
            // Reset workout
            @Override
            public void onClick(View view) {
                showConfirmationDialog("Are you sure you want to Reset your Workouts ?", new ConfirmationDialogListener() {
                    @Override
                    public void onConfirm(boolean confirmed) {
                        if (confirmed) {
                            userByExerciseDAO.deleteAllExerciseByUserId(userId);
                        } else {
                            return;
                        }
                    }
                });
            }
        });
        textViewOption3.setOnClickListener(new View.OnClickListener() {
            // Logout
            @Override
            public void onClick(View view) {
                showConfirmationDialog("Are you sure you want to Logout ?", new ConfirmationDialogListener() {
                    @Override
                    public void onConfirm(boolean confirmed) {
                        if (confirmed) {
                            // Logout
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", false);
                            editor.apply();
                            Intent intent = new Intent(SettingsPage.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            return;
                        }
                    }
                });
            }
        });
        textViewOption4.setOnClickListener(new View.OnClickListener() {
            // Delete Account
            @Override
            public void onClick(View view) {
                showConfirmationDialog("Are you sure you want to Reset your Workouts ?", new ConfirmationDialogListener() {
                    @Override
                    public void onConfirm(boolean confirmed) {
                        if (confirmed) {
                            // Delete Account
                            userByExerciseDAO.deleteAllExerciseByUserId(userId);
                            userDao.deleteAccount(userId);

                            // Logout
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", false);
                            editor.apply();
                            Intent intent = new Intent(SettingsPage.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            return;
                        }
                    }
                });
            }
        });
    }

    public interface ConfirmationDialogListener {
        void onConfirm(boolean confirmed);
    }

    private void showConfirmationDialog(String message, ConfirmationDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onConfirm(true);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onConfirm(false);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}