package com.example.psyyf2.parent;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.view.Gravity;

import com.example.psyyf2.parent.activity.BaseActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by moiravan on 2018/4/16.
 */
public class BaseActivityTest {

   // private MyObject myObj;

    @Rule
    // third parameter is set to false which means the activity is not started automatically
    public ActivityTestRule<BaseActivity> baseActivityActivityTestRule = new ActivityTestRule<BaseActivity>(BaseActivity.class, false, false);



    @Test
    public void loadActivity() {

        Intent i = new Intent();
        i.putExtra("Name", "tom");
        baseActivityActivityTestRule.launchActivity(i);

    }

    @Test
    public void gradeTest() {
        loadActivity();
        onView(withId(R.id.imageButtonGrade)).perform(click());
        onView(withText("Grade")).check(matches((isDisplayed())));
    }

    @Test
    public void homeworkTest() {
        loadActivity();
        onView(withId(R.id.imageButtonHome)).perform(click());
        onView(withText("Homework")).check(matches((isDisplayed())));
    }

    @Test
    public void openDrawerTest(){
        loadActivity();
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(open()); // Open Drawer
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
    }

    @Test
    public void subjectTest(){
        loadActivity();
        onView(withId(R.id.choosesubject)).perform(click());
        onView(withText("Math")).perform(click());
        onView(withText("Math")).check(matches(isDisplayed()));
    }
}