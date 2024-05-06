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

import com.example.trackmyworkout.LandingPage.LandingPage;
import com.example.trackmyworkout.SettingPage.SettingsPage;
import com.example.trackmyworkout.WorkoutPage.WorkoutPage;

@RunWith(AndroidJUnit4.class)
public class IntentSettingsTest {

    @Rule
    public IntentsTestRule<SettingsPage> intentsTestRule = new IntentsTestRule<>(SettingsPage.class);

    // Test Landing Intent
    @Test
    public void ToLanding() {
        onView(withId(R.id.home)).perform(click());
        intended(hasComponent(LandingPage.class.getName()));
    }

    // Test Workout Intent
    @Test
    public void ToWorkout() {
        onView(withId(R.id.workout)).perform(click());
        intended(hasComponent(WorkoutPage.class.getName()));
    }

    // Test Converter Intent
    @Test
    public void ToConverter() {
        onView(withId(R.id.converter)).perform(click());
        intended(hasComponent(ConversionPage.class.getName()));
    }

    // Test Settings Intent
    @Test
    public void ToSettings() {
        onView(withId(R.id.settings)).perform(click());
        intended(hasComponent(SettingsPage.class.getName()));
    }
}
