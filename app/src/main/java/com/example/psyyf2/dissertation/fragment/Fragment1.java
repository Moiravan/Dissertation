package com.example.psyyf2.dissertation.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.psyyf2.dissertation.activity.GradeAnalysis;
import com.example.psyyf2.dissertation.activity.MangeGroup;
import com.example.psyyf2.dissertation.activity.StudentList;
import com.example.psyyf2.dissertation.adapter.TestNormalAdapter;
import com.example.psyyf2.dissertation.database.MyProviderContract;
import com.example.psyyf2.dissertation.R;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.meetme.android.horizontallistview.HorizontalListView;


public class Fragment1 extends Fragment{

    SimpleCursorAdapter dataAdapter;
    View inflate;
    ImageButton imageButtonAna, imageButtonList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        inflate = inflater.inflate(R.layout.activity_fragment1, null);

        queryContentProvider();

        imageButtonAna = (ImageButton) inflate.findViewById(R.id.imageButton1);
        imageButtonList = (ImageButton) inflate.findViewById(R.id.imageButton2);

        imageButtonAna.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Fragment1.this.getContext(), GradeAnalysis.class);
                startActivity(intent);
            }
        });

        imageButtonList.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Fragment1.this.getContext(), StudentList.class);
                startActivity(intent);
            }
        });

        RollPagerView mRollViewPager = (RollPagerView) inflate.findViewById(R.id.roll_view_pager);

        //set the time duration
        mRollViewPager.setPlayDelay(1000);
        mRollViewPager.setAnimationDurtion(1000);
        mRollViewPager.setAdapter(new TestNormalAdapter());
        mRollViewPager.setHintView(new ColorPointHintView(this.getContext(), Color.YELLOW,Color.WHITE));

        return inflate;
    }

    public void queryContentProvider() {
        String[] projection = new String[] {
                MyProviderContract.C_ID,
                MyProviderContract.cNAME
        };

        //display the information from database layout
        String colsToDisplay [] = new String[] {
                MyProviderContract.C_ID,
                MyProviderContract.cNAME
        };

        int[] colResIds = new int[] {
                R.id.cid,
                R.id.classname
        };

        Cursor cursor = this.getActivity().getContentResolver().query(MyProviderContract.Class_URI, projection, null, null, null);    //obtain context objection

        dataAdapter = new SimpleCursorAdapter(
                this.getContext(),
                R.layout.db_class_layout,
                cursor,
                colsToDisplay,
                colResIds,
                0);

        HorizontalListView horizontalList = (HorizontalListView) inflate.findViewById(R.id.HorizontalListView);
        horizontalList.setAdapter(dataAdapter);
        horizontalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
            {
                final String id = ((TextView) myView.findViewById(R.id.cid)).getText().toString();
                final String classname = ((TextView) myView.findViewById(R.id.classname)).getText().toString(); //get the text

                Intent intent = new Intent(Fragment1.this.getContext(), MangeGroup.class);
                Bundle bundle = new Bundle();
                bundle.putString("ID", id);
                bundle.putString("CLASSNAME", classname);
                intent.putExtras(bundle);   //send ID and Title to another activity

                startActivity(intent);
            }
        });

    }
}

