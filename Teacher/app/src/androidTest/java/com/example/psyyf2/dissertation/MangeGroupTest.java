package com.example.psyyf2.dissertation;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.psyyf2.dissertation.activity.BaseActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

/**
 * Created by moiravan on 2018/4/16.
 */

@RunWith(AndroidJUnit4.class)
public class MangeGroupTest {

    @Rule
    public ActivityTestRule<BaseActivity> baseActivityActivityTestRule = new ActivityTestRule<>(
            BaseActivity.class);


    @Test
    public void studentTest() {

        onData(anything()).atPosition(0).inAdapterView(withId(R.id.HorizontalListView)).perform(click());
        onView(withText("class A")).check(matches(isDisplayed()));
        onData(anything()).atPosition(0).inAdapterView(withId(R.id.managelist)).perform(click());
        onView(withText("Student Info")).check(matches(isDisplayed()));
    }

    @Test
    public void groupTest() {
        onData(anything()).atPosition(0).inAdapterView(withId(R.id.HorizontalListView)).perform(click());
        onView(withId(R.id.spinnergroup)).perform(click());
        onView(withText("All")).perform(click());
        onView(withText("All")).check(matches(isDisplayed()));
    }

    @Test
    public void editTest() {
        onData(anything()).atPosition(0).inAdapterView(withId(R.id.HorizontalListView)).perform(click());
        onView(withId(R.id.Edit)).perform(click());
        onData(anything()).atPosition(0).inAdapterView(withId(R.id.managelist))
                .onChildView((withId(R.id.checkBox2)))
                .perform(click());

        onView(withId(R.id.change)).perform(click());
        onView(withText("1")).perform(click());
        onView(withId(R.id.Edit)).perform(click());
    }
}