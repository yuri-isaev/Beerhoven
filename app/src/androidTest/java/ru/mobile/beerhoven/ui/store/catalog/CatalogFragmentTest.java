package ru.mobile.beerhoven.ui.store.catalog;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mobile.beerhoven.R;
import ru.mobile.beerhoven.presentation.activity.MainActivity;

@RunWith(AndroidJUnit4.class)
public class CatalogFragmentTest {

   @Rule
   public ActivityTestRule<MainActivity> activity = new ActivityTestRule<>(MainActivity.class);

   @Test
   public void test_is_activity_in_view() {
      onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
      onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
      onView(withId(R.id.container_catalog)).check(matches(isDisplayed()));
      onView(withId(R.id.recycler_view_catalog)).check(matches(isDisplayed()));
   }

   @Test
   public void test_select_list_item_is_fragment_visible() {
      onView(withId(R.id.recycler_view_catalog)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
   }
}