package com.example.trackmyworkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.trackmyworkout.databinding.ActivitySettingsPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsPage extends AppCompatActivity {

    private boolean isLbsGreen = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        TextView textViewAdminOption1 = findViewById(R.id.textViewAdminOption1);
        TextView textViewAdminOption2 = findViewById(R.id.textViewAdminOption2);

        isLbsGreen = false; // Need check DB
        boolean isAdmin = true;
        if (isAdmin) {
            textViewAdminOption1.setVisibility(View.VISIBLE);
            textViewAdminOption2.setVisibility(View.VISIBLE);
        }

        textViewOption1.setOnClickListener(new View.OnClickListener() {
            // LBS or Kg
            @Override
            public void onClick(View view) {
                // Add real change
                updateTextColors();
            }
        });
        textViewOption2.setOnClickListener(new View.OnClickListener() {
            // Reset workout
            @Override
            public void onClick(View view) {

            }
        });
        textViewOption3.setOnClickListener(new View.OnClickListener() {
            // Logout
            @Override
            public void onClick(View view) {

            }
        });
        textViewOption4.setOnClickListener(new View.OnClickListener() {
            // Delete Account
            @Override
            public void onClick(View view) {

            }
        });
        textViewAdminOption2.setOnClickListener(new View.OnClickListener() {
            // If admin manage account
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void updateTextColors() {
        TextView textViewOption1 = findViewById(R.id.textViewOption1);
        String text = "LBS or Kg";
        SpannableString spannable = new SpannableString(text);
        if (isLbsGreen) {
            // "LBS" is green
            spannable.setSpan(new ForegroundColorSpan(Color.GREEN), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(Color.BLACK), 3, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            // "Kg" is green
            spannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(Color.GREEN), 7, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textViewOption1.setText(spannable);
    }
}