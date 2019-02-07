package com.example.psyyf2.dissertation.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.psyyf2.dissertation.database.MyProviderContract;
import com.example.psyyf2.dissertation.R;

/**
 * Created by moiravan on 2018/3/4.
 */

public class MyAdapterName extends SimpleCursorAdapter {
    private Context mContext;
    String name, Class_ID;


    public MyAdapterName(Context context, int layout, Cursor c, String[] from, int[] to, int flags, String verify) {
        super(context, layout, c, from, to, flags);
        mContext = context;
        name = verify;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View myView = super.getView(position, convertView, parent);

        String[] projection = new String[]{
                MyProviderContract.cNAME,
                MyProviderContract.C_ID
        };


        if(name.equals("grade"))
        {
            Class_ID = ((TextView) myView.findViewById(R.id.obtainclassid)).getText().toString(); //get the text

            Cursor cursor = mContext.getContentResolver().query(MyProviderContract.Class_URI, projection, MyProviderContract.C_ID + "=" + "'" + Class_ID + "'", null, null);  //search for the database to obtaion instruction
            while(cursor.moveToNext()){
                String classname = cursor.getString(cursor.getColumnIndex("classname"));   //set the instruction
                TextView name = (TextView) myView.findViewById(R.id.obtainclass);
                name.setText(classname);
            }
            cursor.close();
        }
        //set the classname
        else if(name.equals("homework") )
        {
            Class_ID = ((TextView) myView.findViewById(R.id.obtainclassid1)).getText().toString(); //get the text

            Cursor cursor = mContext.getContentResolver().query(MyProviderContract.Class_URI, projection, MyProviderContract.C_ID + "=" + "'" + Class_ID + "'", null, null);  //search for the database to obtaion instruction
            while(cursor.moveToNext()){
                String classname = cursor.getString(cursor.getColumnIndex("classname"));   //set the instruction
                TextView name = (TextView) myView.findViewById(R.id.obtainclass1);
                name.setText(classname);
            }
            cursor.close();
        }
        //set the student name
        else if(name.equals("names")){
            String Stu_ID = ((TextView) myView.findViewById(R.id.stuID5)).getText().toString(); //get the text

            String[] projection1 = new String[]{
                    MyProviderContract.sNAME,
                    MyProviderContract.Stu_ID
            };

            Cursor cursor = mContext.getContentResolver().query(MyProviderContract.Student_URI, projection1, MyProviderContract.Stu_ID + "=" + "'" + Stu_ID + "'", null, null);  //search for the database to obtaion instruction
            while(cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("sName"));   //set the instruction
                TextView stuName = (TextView) myView.findViewById(R.id.obtainstuname);
                stuName.setText(name);
            }
            cursor.close();
        }
        return myView;
    }
}
