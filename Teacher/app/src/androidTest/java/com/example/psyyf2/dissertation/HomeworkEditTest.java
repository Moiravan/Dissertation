package com.example.psyyf2.dissertation;

/**
 * Created by moiravan on 2018/4/16.
 */

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.DatePicker;

import com.example.psyyf2.dissertation.activity.BaseActivity;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.not;

/**
 * Created by moiravan on 2018/4/16.
 */

@RunWith(AndroidJUnit4.class)
public class HomeworkEditTest {
    private int year = 2018;
    private int month = 03;
    private int day = 20;

    @Rule
    public ActivityTestRule<BaseActivity> baseActivityActivityTestRule = new ActivityTestRule<>(
            BaseActivity.class);

    @Test
    public void setUp(){

        onView(withId(R.id.viewPager)).perform(swipeLeft());
        onView(withId(R.id.buttonall1)).perform(click());

        onView(withId(R.id.homedate1)).perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month + 1, day));
        onView(withText("OK")).perform(click());
    }


    @Test
    public void openHomeTest(){

        //find the button or element on the view
        onView(withId(R.id.viewPager)).perform(swipeLeft());
        onView(withId(R.id.buttonclose)).perform(click());

        onView(withId(R.id.homedate1)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                    .perform(PickerActions.setDate(year, month + 1, day));
        onView(withText("OK")).perform(click());

        onData(anything()).atPosition(0)
                .inAdapterView(withId(R.id.homelist1))
                .perform(click());

        //click the switch to release the homework
        onView(withId(R.id.switch1)).perform(click());
        pressBack();

        //vertify the functionality for the action
        onView(withId(R.id.buttonopen)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.homelist1)).atPosition(0).
                onChildView(withId(R.id.hometitle)).
                check(matches(withText("Homework test")));

        onData(anything()).atPosition(0).inAdapterView(withId(R.id.homelist1)).perform(click());

        onView(withId(R.id.switch1))
               .check(matches(not(isDisplayed())));
    }

    @Test
    public void homeStatuTest(){

        setUp();

        onData(anything()).atPosition(0).inAdapterView(withId(R.id.homelist1)).perform(click());
        onView(withId(R.id.imageButton)).perform(click());
        onView(withText("Assign")).check(matches((isDisplayed())));

    }

    @Test
    public void homeUpdateTest(){

        setUp();

        onData(anything()).atPosition(0).inAdapterView(withId(R.id.homelist1)).perform(click());
        onView(withId(R.id.imageButtonEdit)).perform(click());
        onView(withId(R.id.buttonCancel)).perform(click());
        onView(withId(R.id.imageButtonEdit)).perform(click());
        onView(withId(R.id.hometitle4)).perform(replaceText("testtitle"),closeSoftKeyboard());
        onView(withId(R.id.homedescription4)).perform(replaceText("testdescription"),closeSoftKeyboard());

        onView(withId(R.id.buttonAssign)).perform(click());

        onView(withText("testtitle")).check(matches((isDisplayed())));
        onView(withText("testdescription")).check(matches((isDisplayed())));
    }

    @Test
    public void homeDeleteTest(){

        setUp();

        onData(anything()).atPosition(0).inAdapterView(withId(R.id.homelist1)).perform(click());
        onView(withId(R.id.imageButtonDelete)).perform(click());

        onView(withId(R.id.buttoncancel)).perform(click());
        onView(withId(R.id.imageButtonDelete)).check(matches((isDisplayed())));
        onView(withId(R.id.imageButtonDelete)).perform(click());
        onView(withId(R.id.buttonDelete)).perform(click());

    }

}