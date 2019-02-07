package com.example.psyyf2.dissertation.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.psyyf2.dissertation.adapter.GuideAdapter;
import com.example.psyyf2.dissertation.R;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        RollPagerView welRollViewPager = (RollPagerView)findViewById(R.id.roll_view_pager1);

        GuideAdapter welcomepage = new GuideAdapter();

        //load the images from adapter
        welRollViewPager.setAdapter(welcomepage);
        welRollViewPager.setHintView(new ColorPointHintView(this, Color.YELLOW,Color.WHITE));
        welRollViewPager.getViewPager().getCurrentItem();

        welRollViewPager.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                //the last page
                if(position == 3)
                {
                    Intent intent = new Intent(WelcomePage.this, BaseActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

}
