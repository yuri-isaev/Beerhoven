package ru.mobile.beerhoven.activity;

import static androidx.test.core.app.ActivityScenario.launch;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobile.beerhoven.presentation.activity.MainActivity;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

   @Rule
   public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

   @Test
   public void test_activity_state_create() {
      ActivityScenario<MainActivity> scenario = launch(MainActivity.class);
      scenario.moveToState(Lifecycle.State.CREATED);
      scenario.close();
   }

   @Test
   public void test_activity_recreate() {
      activityRule.getActivity().runOnUiThread(() -> activityRule.getActivity().recreate());
   }
}