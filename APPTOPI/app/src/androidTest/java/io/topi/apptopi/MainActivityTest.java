package io.topi.apptopi;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void changeTextSearch(){
        onView(withId(R.id.edt_search)).perform(ViewActions.pressMenuKey());
        onView(withId(R.id.rv_list_git)).check(ViewAssertions.matches(ViewMatchers.isEnabled()));
    }

    @Test
    public void checkSort(){
        onView(withId(R.id.iv_sort)).perform(ViewActions.click());
        onView(withId(R.id.ll_sort_name)).perform(ViewActions.click());
        onView(withId(R.id.rv_list_git)).check(ViewAssertions.matches(ViewMatchers.isEnabled()));
    }
}
