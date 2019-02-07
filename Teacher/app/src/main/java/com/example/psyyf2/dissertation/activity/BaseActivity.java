package com.example.psyyf2.dissertation.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.psyyf2.dissertation.adapter.GradeHelper;
import com.example.psyyf2.dissertation.adapter.HomeworkListener;
import com.example.psyyf2.dissertation.adapter.MyFragmentPagerAdapter;
import com.example.psyyf2.dissertation.R;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        //set actions on drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initViews();

        //find the textview in the drawer
        View headerView = navigationView.getHeaderView(0);
        TextView username = (TextView)headerView.findViewById(R.id.username);
        TextView useremail = (TextView)headerView.findViewById(R.id.useraddress);

        SharedPreferences userSettings= getSharedPreferences("config", 0);
        username.setText(userSettings.getString("name","null"));
        useremail.setText(userSettings.getString("email","null"));

    }

    private void initViews() {

        //Bound the ViewPage with the fragment
        ViewPager tabViewPager= (ViewPager) findViewById(R.id.viewPager);
        MyFragmentPagerAdapter tabFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        tabViewPager.setAdapter(tabFragmentPagerAdapter);

        //Bound the ViewPage with TabLayout
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(tabViewPager);

        //Set the position of Tab
        TabLayout.Tab one = mTabLayout.getTabAt(0);
        TabLayout.Tab two = mTabLayout.getTabAt(1);
        TabLayout.Tab three = mTabLayout.getTabAt(2);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            HomeworkListener createHome = new HomeworkListener(mContext, BaseActivity.this);
            createHome.onCreateHome();
        }
        else if (id == R.id.nav_grade) {
            GradeHelper createGrade = new GradeHelper(mContext, this);
            createGrade.onCreateGrade();

        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(BaseActivity.this, AccountPage.class);
            startActivity(intent);

        } else if (id == R.id.nav_send) {

            //clear the sharereference file
            SharedPreferences userSettings= getSharedPreferences("config", 0);
            userSettings.edit().remove("name").apply();
            userSettings.edit().remove("email").apply();
            Intent intent = new Intent(BaseActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
