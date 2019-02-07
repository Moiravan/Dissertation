package com.example.psyyf2.dissertation.adapter;

/**
 * Created by moiravan on 2018/4/12.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.psyyf2.dissertation.fragment.Fragment1;
import com.example.psyyf2.dissertation.fragment.FragmentGrade;
import com.example.psyyf2.dissertation.fragment.FragmentHomework;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


    private String[] mTitles = new String[]{"Main", "Homework", "Grade"};

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //return with proper fragment
    @Override
    public Fragment getItem(int position) {
        if(position == 0)
        {
            return new Fragment1();
        }
        if (position == 1) {
            return new FragmentHomework();
        }
        else if (position == 2)
        {
            return new FragmentGrade();
        }

        return new Fragment1();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
