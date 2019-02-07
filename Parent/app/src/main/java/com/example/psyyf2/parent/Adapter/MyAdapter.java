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

public class MyAdapter extends SimpleCursorAdapter {

    private Context check;
    private String Stu_ID;

    public MyAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, String id) {
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
        final String homeID = ((TextView) myView.findViewById(R.id.homeID)).getText().toString(); //get the text
        final TextView grade = (TextView) myView.findViewById(R.id.homestatu);


        String[] projection = new String[]{
                MyProviderContract.C_ID,
                MyProviderContract.Home_ID,
                MyProviderContract.STATU,
                MyProviderContract.Stu_ID
        };

       // Cursor cursor = check.getContentResolver().query(MyProviderContract.Statu_URI, projection, MyProviderContract.Home_ID + "=" + "'" + homeID + "'" + " "+ "and" + " " + MyProviderContract.Stu_ID + "=" + "'" + Stu_ID + "'" , null, null);    //obtain context objection
        Cursor cursor = check.getContentResolver().query(MyProviderContract.Statu_URI, projection, MyProviderContract.Home_ID + "=" + "'" + homeID + "'" + " " + "and" + " " + MyProviderContract.Stu_ID + "=" + "'" + Stu_ID + "'" , null, null);    //obtain context objection

        while(cursor.moveToNext()){
            String statu = cursor.getString(cursor.getColumnIndex("statu"));   //set the instructi
            grade.setText(statu);
        }

        cursor.close();

        return myView;
    }
}
