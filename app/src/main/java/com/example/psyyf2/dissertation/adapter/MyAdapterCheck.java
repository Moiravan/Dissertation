package com.example.psyyf2.dissertation.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.psyyf2.dissertation.R;

import java.util.Map;

/**
 * Created by moiravan on 2018/3/4.
 */

public class MyAdapterCheck extends SimpleCursorAdapter {
    private Map<String, Boolean> map;
    private Boolean editMode;

    public MyAdapterCheck(Context context, int layout, Cursor c, String[] from, int[] to, int flags, Map<String, Boolean> map) {
        super(context, layout, c, from, to, flags);
        this.map = map;
        editMode = false;
    }

    public void flipEditMode() {
        editMode = !editMode;
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

        final String stuID = ((TextView) myView.findViewById(R.id.stuID4)).getText().toString();
        final CheckBox isChecked = (CheckBox)myView.findViewById(R.id.checkBox2);

        if (editMode) {
            isChecked.setVisibility(View.VISIBLE);
        } else {
            isChecked.setVisibility(View.INVISIBLE);
        }

        isChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                map.put(stuID, isChecked);  //use the map to log the choose status
            }
        });
        return myView;
    }
}
