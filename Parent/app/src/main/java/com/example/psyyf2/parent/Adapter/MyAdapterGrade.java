package com.example.psyyf2.parent.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.psyyf2.parent.Database.MyProviderContract;
import com.example.psyyf2.parent.R;

/**
 * Created by moiravan on 2018/3/4.
 */

public class MyAdapterGrade extends SimpleCursorAdapter {

    private Context check;
    private String Stu_ID;

    public MyAdapterGrade(Context context, int layout, Cursor c, String[] from, int[] to, int flags, String id) {
        super(context, layout, c, from, to, flags);

        this.check = context;
        this.Stu_ID = id;
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

       // final String stuName = ((TextView) myView.findViewById(R.id.stuName1)).getText().toString(); //get the text
        final String examID = ((TextView) myView.findViewById(R.id.examid)).getText().toString(); //get the text
        final TextView grade = (TextView) myView.findViewById(R.id.examgrade);
        grade.setText(R.string.release);


        String[] projection = new String[]{
                MyProviderContract.Exam_ID,
                MyProviderContract.Stu_ID,
                MyProviderContract.GRADE
        };

        //set the grade in the listview
        Cursor cursor = check.getContentResolver().query(MyProviderContract.Grade_URI, projection, MyProviderContract.Exam_ID + "=" + "'" + examID + "'" + " " + "and" + " " + MyProviderContract.Stu_ID + "=" + "'" + Stu_ID + "'" , null, null);    //obtain context objection

        while(cursor.moveToNext()){
            String stugrade = cursor.getString(cursor.getColumnIndex("grade"));   //set the instructi
            grade.setText(stugrade);
        }

        cursor.close();

        return myView;
    }
}
