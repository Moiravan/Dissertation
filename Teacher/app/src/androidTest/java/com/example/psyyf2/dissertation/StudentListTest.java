package com.example.psyyf2.dissertation;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

/**
 * Created by moiravan on 2018/4/16.
 */

@RunWith(AndroidJUnit4.class)

public class StudentListTest {

    @Rule
    public ActivityTestRule<StudentList> studentListTestActivityTestRule = new ActivityTestRule<>(
            StudentList.class);

    @Test
    public void chooseStudentTest(){

        onData(anything()).atPosition(0).inAdapterView(withId(R.id.list_student)).perform(click());
        onView(withText("Student Info")).check(matches(isDisplayed()));
    }

    @Test
    public void chatTest(){
        chooseStudentTest();
        onView(withId(R.id.sendemail)).perform(click());
        onView(withId(R.id.input)).perform(typeText("chat test"),closeSoftKeyboard());
        onView(withId(R.id.imageButtonChat)).perform(click());

        onView(withId(R.id.listchat)).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.listchat)).atPosition(0).
                onChildView(withId(R.id.message_text)).
                check(matches(withText("chat test")));
    }


}