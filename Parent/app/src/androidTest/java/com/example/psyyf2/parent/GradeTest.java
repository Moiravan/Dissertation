package com.example.psyyf2.parent;

import android.content.Intent;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;

import com.example.psyyf2.parent.activity.Grade;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

/**
 * Created by moiravan on 2018/4/16.
 */
public class GradeTest {

    // private MyObject myObj;

    @Rule
    // third parameter is set to false which means the activity is not started automatically
    public ActivityTestRule<Grade> gradeActivityTestRule = new ActivityTestRule<Grade>(Grade.class, false, false);


    @Test
    public void loadActivity() {

        Intent i = new Intent();
        i.putExtra("CID", "1");
        i.putExtra("Stu_ID", "1");
        i.putExtra("Name", "tom");
        gradeActivityTestRule.launchActivity(i);

    }

    @Test
    public void dataTest() {

        loadActivity();

        int year = 2018;
        int month = 03;
        int day = 16;

        onView(withId(R.id.examdate)).perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month + 1, day));
        onView(withText("OK")).perform(click());

        onData(anything()).inAdapterView(withId(R.id.examlist)).atPosition(0).
                onChildView(withId(R.id.date)).
                check(matches(withText("2018/4/16")));
    }

}