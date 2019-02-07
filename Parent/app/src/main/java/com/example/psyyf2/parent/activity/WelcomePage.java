package com.example.psyyf2.parent.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.psyyf2.parent.Adapter.GuideApapter;
import com.example.psyyf2.parent.R;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

public class WelcomePage extends AppCompatActivity {
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        Bundle bundle = getIntent().getExtras();    //get the current ID
        name = bundle.getString("Name");


        RollPagerView mRollViewPager = (RollPagerView)findViewById(R.id.roll_view_pager1);

        GuideApapter test = new GuideApapter();

        mRollViewPager.setAdapter(test);
        mRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW,Color.WHITE));


        mRollViewPager.getViewPager().getCurrentItem();

        mRollViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //last page
                if(position == 3)
                {
                    Intent intent = new Intent(WelcomePage.this, BaseActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Name", name);
                    intent.putExtras(bundle);   //send ID and Title to another activity
                    startActivity(intent);
                }
            }
        });
    }

}
