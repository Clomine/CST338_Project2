package com.example.trackmyworkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.trackmyworkout.databinding.ActivityConversionPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConversionPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The following part is for the Bottom Navigation Bar
        ActivityConversionPageBinding binding = ActivityConversionPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = binding.bottomNavigation;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                int temp = item.getItemId();
                if (R.id.home == temp) {
                    intent = new Intent(ConversionPage.this, LandingPage.class);
                    startActivity(intent);
                } else if (R.id.workout == temp){
                    intent = new Intent(ConversionPage.this, WorkoutPage.class);
                    startActivity(intent);
                } else if (R.id.converter == temp) {
                    intent = new Intent(ConversionPage.this, ConversionPage.class);
                    startActivity(intent);
                } else if (R.id.settings == temp) {
                    intent = new Intent(ConversionPage.this, SettingsPage.class);
                    startActivity(intent);
                }
                return true;
            }
        });
        // The above part is for the Bottom Navigation Bar
    }
}