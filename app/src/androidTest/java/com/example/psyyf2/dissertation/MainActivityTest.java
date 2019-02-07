package com.example.psyyf2.dissertation;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.psyyf2.dissertation.activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by moiravan on 2018/4/15.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void login(){
        //login
        onView(withId(R.id.textView3)).perform(typeText("psyyf2@nottingham.ac.uk"),closeSoftKeyboard());
        onView(withId(R.id.editText4)).perform(typeText("TEACHERpassword2"),closeSoftKeyboard());
        onView(withId(R.id.Log)).perform(click());
        onView(withText("Contact")).check(matches(isDisplayed()));
    }

    @Test
    public void loginFail() {
        //login
        onView(withId(R.id.textView3)).perform(typeText("Moira"), closeSoftKeyboard());
        onView(withId(R.id.editText4)).perform(typeText("aaaa"), closeSoftKeyboard());
        onView(withId(R.id.Log)).perform(click());
        onView(withText("Ok")).check(matches(isDisplayed()));
    }

    @Test
    public void checkPasswordView() {
        //login
        onView(withId(R.id.textView3)).perform(typeText("Moira"), closeSoftKeyboard());
        onView(withId(R.id.editText4)).perform(typeText("aaaa"), closeSoftKeyboard());
        onView(withId(R.id.checkBox)).perform(click());
        onView(withText("aaaa")).check(matches(isDisplayed()));
    }
}