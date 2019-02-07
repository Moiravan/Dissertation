package com.example.psyyf2.dissertation;

import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import android.widget.DatePicker;

import com.example.psyyf2.dissertation.activity.BaseActivity;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;



/**
 * Created by moiravan on 2018/4/16.
 */

@RunWith(AndroidJUnit4.class)
public class BaseActivityTest {

    @Rule
    public ActivityTestRule<BaseActivity> baseActivityActivityTestRule = new ActivityTestRule<>(
            BaseActivity.class);

    @Test
    public void openDrawerTest(){
        onView(withId(R.id.drawer_layout)).check(matches(isDisplayed()));
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(open()); // Open Drawer
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
    }

    @Test
    public void swapHomeTest(){
        int year = 2018;
        int month = 03;
        int day = 20;

        onView(withId(R.id.viewPager)).perform(swipeLeft());
        onView(withId(R.id.buttonclose)).perform(click());
        onView(withId(R.id.homedate1)).perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month + 1, day));
        onView(withText("OK")).perform(click());
        onData(anything()).atPosition(0).inAdapterView(withId(R.id.homelist1)).perform(click());
    }

    @Test
    public void swapExamTest(){
        onView(withId(R.id.viewPager)).perform(swipeLeft());
        onView(withId(R.id.viewPager)).perform(swipeLeft());
        onView(withId(R.id.buttonno)).perform(click());

        int year = 2018;
        int month = 03;
        int day = 20;


        onView(withId(R.id.gradedate)).perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month + 1, day));
        onView(withText("OK")).perform(click());

    }

    @Test
    public void gradeTest(){

        onView(withId(R.id.imageButton1)).perform(click());
        onView(withText("Grade Analysis")).check(matches(isDisplayed()));
    }

    @Test
    public void studentTest(){
        onView(withId(R.id.imageButton2)).perform(click());
        onView(withText("Student List")).check(matches(isDisplayed()));
    }

    @Test
    public void createExamTest(){
        openDrawerTest();
        int year = 2018;
        int month = 03;
        int day = 20;


        onView(withText("EXAM")).perform(click());
        onView(withId(R.id.spinnerSub)).perform(click());

        onData(allOf(is(instanceOf(String.class)),
                is("English")))
                .inRoot(isPlatformPopup())
                .perform(click());

        onView(withText("English")).check(matches(isDisplayed()));

        onView(withId(R.id.spinnerClass)).perform(click());

        onData(allOf(is(instanceOf(String.class)),
                is("class A")))
                .inRoot(isPlatformPopup())
                .perform(click());

        onView(withText("class A")).check(matches(isDisplayed()));
        onView(withId(R.id.day)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions
                        .setDate(year, month + 1, day));

        onView(withText("OK")).perform(click());
        onView(withId(R.id.examName)).perform(typeText("Exam test"),closeSoftKeyboard());
        onView(withText("Add")).perform(click());

    }

    @Test
    public void createHomeworkTest(){
        openDrawerTest();
        int year = 2018;
        int month = 03;
        int day = 20;


        onView(withText("HOMEWORK")).perform(click());

        onView(withId(R.id.spinnerClass)).perform(click());

        onData(allOf(is(instanceOf(String.class)),
                is("class A")))
                .inRoot(isPlatformPopup())
                .perform(click());

        onView(withId(R.id.day1)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month + 1, day));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.hometitle1)).perform(typeText("Homework test"),closeSoftKeyboard());
        onView(withId(R.id.homedescription1)).perform(typeText("Homework description test"),closeSoftKeyboard());

        onView(withText("Add")).perform(click());

    }
}