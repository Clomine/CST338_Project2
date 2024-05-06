package com.example.trackmyworkout;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class IntentMainTest {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    // Test SignIn Intent
    @Test
    public void MainToSignInTest() {
        onView(withId(R.id.signIn)).perform(click());
        intended(hasComponent(SignIn.class.getName()));
    }

    // Test SignUp Intent
    @Test
    public void MainToSignUpTest() {
        onView(withId(R.id.signUp)).perform(click());
        intended(hasComponent(SignUp.class.getName()));
    }
}
