package com.example.psyyf2.parent.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.psyyf2.parent.Adapter.DBHelper;
import com.example.psyyf2.parent.Database.MyProviderContract;
import com.example.psyyf2.parent.R;
import com.example.psyyf2.parent.Adapter.TestNormalAdapter;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String Stu_ID, Group_ID, cid, name, stuName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        RollPagerView mRollViewPager = (RollPagerView) findViewById(R.id.roll_view_pager);
        mRollViewPager.setPlayDelay(1000);
        mRollViewPager.setAnimationDurtion(1000);
        mRollViewPager.setAdapter(new TestNormalAdapter());
        mRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW,Color.WHITE));

        Bundle bundle = getIntent().getExtras();    //get the current ID
        name = bundle.getString("Name");

        DBHelper dbHelper = new DBHelper(this,"dissertation" + ".db",null,7);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from parent where pName=? ";
        Cursor cursor = db.rawQuery(sql, new String[] {name});

        //obtain the student ID
        if (cursor.moveToFirst()) {
            Stu_ID = cursor.getString(cursor.getColumnIndex("Stu_ID"));   //set the instruct
        }
        cursor.close();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //obtain the view in the drawer
        View headerView = navigationView.getHeaderView(0);
        TextView username = (TextView)headerView.findViewById(R.id.username);
        TextView useremail = (TextView)headerView.findViewById(R.id.useremail);

        SharedPreferences userSettings= getSharedPreferences("configParent", 0);
        username.setText(userSettings.getString("name","null"));
        useremail.setText(userSettings.getString("email","null"));

        String[] projection = new String[]{
                MyProviderContract.Stu_ID,
                MyProviderContract.C_ID,
                MyProviderContract.sGROUP,
                MyProviderContract.sNAME
        };

        cursor = getContentResolver().query(MyProviderContract.Student_URI, projection, MyProviderContract.Stu_ID + "=" + "'" + Stu_ID + "'", null, null);    //obtain context objection

        while(cursor.moveToNext()){
            Group_ID = cursor.getString(cursor.getColumnIndex("sGroup"));   //set the instructi
            cid = cursor.getString(cursor.getColumnIndex("_id"));
            stuName = cursor.getString(cursor.getColumnIndex("sName"));
        }
        cursor.close();
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

        if (id == R.id.nav_setting) {
            Intent intent = new Intent(BaseActivity.this, Setting.class);
            startActivity(intent);
        }else if (id == R.id.nav_out) {
            //clear the sharereference file
            SharedPreferences userSettings= getSharedPreferences("configParent", 0);
            userSettings.edit().remove("name").apply();
            userSettings.edit().remove("email").apply();
            Intent intent = new Intent(BaseActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void homework(View v){
        Intent intent = new Intent(BaseActivity.this, Homework.class);

        Bundle bundle = new Bundle();
        bundle.putString("Stu_ID", Stu_ID);
        bundle.putString("Group_ID", Group_ID);
        bundle.putString("Name", name);
        intent.putExtras(bundle);   //send ID and Title to another activity

        startActivity(intent);
    }

    public void grade(View v){
        Intent intent = new Intent(BaseActivity.this, Grade.class);

        Bundle bundle = new Bundle();
        bundle.putString("Stu_ID", Stu_ID);
        bundle.putString("Group_ID", Group_ID);
        bundle.putString("CID", cid);
        bundle.putString("Name", name);
        intent.putExtras(bundle);   //send ID and Title to another activity

        startActivity(intent);
    }

    public void chooseSubject(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.choose_subject, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                String subject = item.getTitle().toString();
                Intent intent = new Intent(BaseActivity.this, MarkAnalysis.class);

                Bundle bundle = new Bundle();
                bundle.putString("Stu_ID", Stu_ID);
                bundle.putString("CID", cid);
                bundle.putString("Name", name);
                bundle.putString("Subject", subject);
                bundle.putString("stuName", stuName);
                intent.putExtras(bundle);   //send ID and Title to another activity

                startActivity(intent);
                return true;
            }
        });

        popup.show(); //showing popup menu
    }
}
