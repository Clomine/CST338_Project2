package com.example.trackmyworkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.trackmyworkout.LandingPage.LandingPage;
import com.example.trackmyworkout.databinding.ActivityConversionPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConversionPage extends AppCompatActivity {


    private EditText lbsInput, kgInput;
    private Button convertButton;
    private TextView resultText;


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
                } else if (R.id.workout == temp) {
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

        lbsInput = binding.lbsInput;
        kgInput = binding.kgInput;
        convertButton = binding.convertButton;
        resultText = binding.resultText; // Initialize the result TextView

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
                String result;
                if (!lbsInput.getText().toString().isEmpty()) {
                    double lbs = Double.parseDouble(lbsInput.getText().toString());
                    double kg = lbs / 2.20462;
                    result = String.format("%.2f kg", kg);
                    lbsInput.setText(""); // Clear the lbs input
                } else if (!kgInput.getText().toString().isEmpty()) {
                    double kg = Double.parseDouble(kgInput.getText().toString());
                    double lbs = kg * 2.20462;
                    result = String.format("%.2f lbs", lbs);
                    kgInput.setText(""); // Clear the kg input
                } else {
                    result = "Please enter a value !";
                }
                resultText.setText(result);
                resultText.setVisibility(View.VISIBLE);
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
