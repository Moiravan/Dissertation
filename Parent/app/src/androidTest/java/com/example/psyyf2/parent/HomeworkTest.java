package com.example.psyyf2.parent;

import android.content.Intent;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;

import com.example.psyyf2.parent.activity.Homework;

import org.hamcrest.Matchers;
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
public class HomeworkTest {

    public ActivityTestRule<Homework> homeworkActivityTestRule = new ActivityTestRule<Homework>(Homework.class, false, false);


    @Test
    public void loadActivity() {

        Intent i = new Intent();
        i.putExtra("CID", "1");
        i.putExtra("Stu_ID", "1");
        i.putExtra("Name", "tom");
        i.putExtra("Group_ID", "1");
        homeworkActivityTestRule.launchActivity(i);

    }

    @Test
    public void dataTest() {

        loadActivity();

        int year = 2018;
        int month = 03;
        int day = 16;

        onView(withId(R.id.homedate)).perform(click());

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(year, month + 1, day));
        onView(withText("OK")).perform(click());

        onData(anything()).inAdapterView(withId(R.id.homelist)).atPosition(0).
                check(matches(withText("2018/4/16")));
    }

    @Test
    public void markTest() {
        loadActivity();
        onData(anything()).atPosition(3).inAdapterView(withId(R.id.homelist)).perform(click());
        onView(withId(R.id.checkBox2)).perform(click());
        onView(withId(R.id.btnsave)).perform(click());

        onView(withId(R.id.item1)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.homelist)).atPosition(3).
                onChildView(withId(R.id.homestatu)).
                check(matches(withText("Finished")));
    }
}