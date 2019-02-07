package com.example.psyyf2.dissertation.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.psyyf2.dissertation.database.MyProviderContract;
import com.example.psyyf2.dissertation.R;

import java.util.Map;

/**
 * Created by moiravan on 2018/3/4.
 */

public class MyAdapter extends SimpleCursorAdapter {
    private Map<String, String> map;
    private String examid;
    private Context mContext;

    public MyAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags, Map<String, String> map, String id) {
        super(context, layout, c, from, to, flags);
        this.map = map;
        mContext = context;
        this.examid = id;

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

        final String stuID = ((TextView) myView.findViewById(R.id.stuid)).getText().toString(); //get the text

        final EditText grade = (EditText) myView.findViewById(R.id.grade);

        String[] projection = new String[]{
                MyProviderContract.GRADE,
                MyProviderContract.sNAME,
                MyProviderContract.Stu_ID
        };

        Cursor cursor = mContext.getContentResolver().query(MyProviderContract.Grade_URI, projection, MyProviderContract.Stu_ID + "=" + "'" + stuID + "'" + " " +"and" + " " + MyProviderContract.Exam_ID + "=" + "'" + examid + "'" , null, null);  //search for the database to obtaion instruction

        while(cursor.moveToNext()) {

            String grade1 = cursor.getString(cursor.getColumnIndex("grade"));   //set the instruction
            grade.setText(grade1);
        }

        grade.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //do nothing
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //do nothing
            }
            @Override
            public void afterTextChanged(Editable s) {
                map.put(stuID,s.toString());    //bound the data into the map
            }
        });
        return myView;
    }
}
