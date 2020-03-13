package ru.mobile.beerhoven.activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.presenter.ui.activity.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

   @Rule
   public ActivityTestRule<MainActivity> activity = new ActivityTestRule<>(MainActivity.class);

   public ActivityTestRule<MainActivity> getActivity() {
      return activity;
   }

   @Test
   public void test_isActivityInView() {
      onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
      onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
      onView(withId(R.id.container_catalog)).check(matches(isDisplayed()));
   }
}