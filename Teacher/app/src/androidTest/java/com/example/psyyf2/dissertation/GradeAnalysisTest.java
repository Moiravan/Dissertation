package com.example.psyyf2.dissertation;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.psyyf2.dissertation.activity.GradeAnalysis;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by moiravan on 2018/4/16.
 */

@RunWith(AndroidJUnit4.class)
public class GradeAnalysisTest {


    @Rule
    public ActivityTestRule<GradeAnalysis> gradeAnalysisActivityTestRule = new ActivityTestRule<>(
            GradeAnalysis.class);

    @Test
    public void classTest(){
        onView(withText("Class")).perform(click());
        onView(withText("class A")).perform(click());
    }

    @Test
    public void subjectTest(){
        onView(withText("Subject")).perform(click());
        onView(withText("English")).perform(click());
        onView(withText("English")).check(matches(isDisplayed()));

    }
}