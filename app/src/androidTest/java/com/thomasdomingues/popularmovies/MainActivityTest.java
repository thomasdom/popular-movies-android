package com.thomasdomingues.popularmovies;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.thomasdomingues.popularmovies.ui.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest
{
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityLaunches()
    {
        onView(withId(R.id.rv_movie_list))
                .check(matches(isDisplayed()));
    }

    @Test
    public void canScroll()
    {
        onView(withId(R.id.rv_movie_list)).perform(RecyclerViewActions.scrollToPosition(4)).check(matches(isDisplayed()));
    }
}
