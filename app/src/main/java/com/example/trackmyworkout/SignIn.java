package com.example.trackmyworkout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.trackmyworkout.DB.Database;
import com.example.trackmyworkout.DB.UserDao;
import com.example.trackmyworkout.LandingPage.LandingPage;
import com.example.trackmyworkout.databinding.ActivitySignInBinding;

public class SignIn extends AppCompatActivity {

    Integer userId;
    EditText emailorusername;
    EditText password;
    ActivitySignInBinding binding;
    UserDao userDao;

    public static boolean isAllowed = false;

    private void submitSignIn() {
        String EmailorUsername = emailorusername.getText().toString();
        String Password = password.getText().toString();

        if (EmailorUsername.contains("@")) {
            userId = userDao.loginEmail(EmailorUsername,Password);
            if(userId != null) {
                isAllowed = true;
            }
            else {
                isAllowed = false;
                Toast.makeText(SignIn.this,"Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            userId = userDao.loginUsername(EmailorUsername,Password);
            if(userId != null) {
                isAllowed = true;
            }
            else {
                Toast.makeText(SignIn.this,"Authentication failed.", Toast.LENGTH_SHORT).show();
                isAllowed = false;
            }
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailorusername = binding.emailorusernameField;
        password = binding.passwordField;


        userDao = Room.databaseBuilder(this, Database.class,"DB")
                .allowMainThreadQueries()
                .build()
                .TMWDao();

        ImageButton backButton = findViewById(R.id.imageButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this,MainActivity.class);
                startActivity(intent);

            }
        });

        Button signIn = findViewById(R.id.signInButton);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSignIn();
                if (isAllowed) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn",true);
                    editor.putString("emailOrUsername",emailorusername.getText().toString());
                    editor.putInt("userId", userId);
                    editor.apply();
                    Intent intent = new Intent(SignIn.this, LandingPage.class);
                    startActivity(intent);
                }
            }
        });

    }
}