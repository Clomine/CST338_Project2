package com.example.trackmyworkout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.trackmyworkout.LandingPage.LandingPage;
import com.example.trackmyworkout.DB.Database;
import com.example.trackmyworkout.DB.UserDao;
import com.example.trackmyworkout.DB.UserTable;
import com.example.trackmyworkout.databinding.ActivitySignUpBinding;

public class SignUp extends AppCompatActivity {

    EditText username;
    EditText email;
    EditText password;
    EditText confirmPassword;
    UserDao userDao;
    ActivitySignUpBinding binding;
    public static boolean isAllowed = false;


    private void submitSignUp(){
        String Username = username.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String ConfirmPassword = confirmPassword.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            // Invalid email address
            Toast.makeText(SignUp.this,"Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            isAllowed = false;
        }

        else if (!Password.equals(ConfirmPassword)) {
            Toast.makeText(SignUp.this,"The password and confirmation password aren't the same.", Toast.LENGTH_SHORT).show();
            isAllowed = false;
        }

        else if (userDao.is_takenUsername(Username)) {
            Toast.makeText(SignUp.this,"This username is already taken.", Toast.LENGTH_SHORT).show();
            isAllowed = false;
        }

        else if (userDao.is_takenEmail(Email)) {
            Toast.makeText(SignUp.this,"An account is already associated with this E-Mail.", Toast.LENGTH_SHORT).show();
            isAllowed = false;
        }

        else {
            UserTable user = new UserTable(0,Username,Email,Password, false);
            userDao.insertUser(user);
            isAllowed = true;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        username = binding.usernameField;
        email = binding.emailField;
        password = binding.passwordField;
        confirmPassword = binding.confirmPasswordField;

        userDao = Room.databaseBuilder(this,Database.class,Database.USER_TABLE)
                .allowMainThreadQueries()
                .build()
                .TMWDao();

        ImageButton backButton = findViewById(R.id.imageButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this,MainActivity.class);
                startActivity(intent);
            }
        });

        Button signUp = findViewById(R.id.signUpButton);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSignUp();
                if (isAllowed) {
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn",true);
                    editor.putString("emailOrUsername",username.getText().toString());
                    editor.apply();
                    Intent intent = new Intent(SignUp.this, LandingPage.class);
                    startActivity(intent);
                }
            }
        });

    }
}