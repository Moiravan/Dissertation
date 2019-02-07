package com.example.psyyf2.dissertation;

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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

/**
 * Created by moiravan on 2018/4/16.
 */

@RunWith(AndroidJUnit4.class)
public class FragmentGradeTest {

    private int year = 2018;
    private int month = 04;
    private int day = 20;

    @Rule
    public ActivityTestRule<BaseActivity> baseActivityActivityTestRule = new ActivityTestRule<>(
            BaseActivity.class);

    @Test
    public void setUp(){

        onView(withId(R.id.viewPager)).perform(swipeLeft());
        onView(withId(R.id.viewPager)).perform(swipeLeft());
        onView(withId(R.id.buttonall)).perform(click());
        onView(withId(R.id.gradedate)).perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month + 1, day));
        onView(withText("OK")).perform(click());
    }


    @Test
    public void assignTest(){

        setUp();
        onView(withId(R.id.buttonno)).perform(click());
        onData(anything()).atPosition(0).inAdapterView(withId(R.id.gradelist1)).perform(click());

        onData(anything()).atPosition(0).inAdapterView(withId(R.id.gradelist))
                .onChildView((withId(R.id.grade)))
                .perform(typeText("90"),closeSoftKeyboard());

        onData(anything()).atPosition(1).inAdapterView(withId(R.id.gradelist))
                .onChildView((withId(R.id.grade)))
                .perform(typeText("80"),closeSoftKeyboard());

        onData(anything()).atPosition(2).inAdapterView(withId(R.id.gradelist))
                .onChildView((withId(R.id.grade)))
                .perform(typeText("70"),closeSoftKeyboard());

        onView(withId(R.id.buttonSave)).perform(click());
        setUp();
        onView(withId(R.id.buttonre)).perform(click());
        onData(anything()).atPosition(0).inAdapterView(withId(R.id.gradelist1)).onChildView((withId(R.id.exammark))).check(matches(withText("80")));
    }

    @Test
    public void viewTest(){
        setUp();
        onView(withId(R.id.buttonre)).perform(click());
        onData(anything()).atPosition(0).inAdapterView(withId(R.id.gradelist1)).perform(click());
        onView(withId(R.id.test_content)).check(matches(isDisplayed()));
    }
}