package com.example.psyyf2.dissertation.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psyyf2.dissertation.database.MyProviderContract;
import com.example.psyyf2.dissertation.R;

import java.util.Calendar;

/**
 * Created by moiravan on 2018/4/13.
 */

public class HomeworkListener{

    private Context mContext;
    private String classname, title, description, hDate, groupname;
    private SimpleCursorAdapter dataAdapter;
    AppCompatActivity details;
    private String cid = null;

    private EditText date;

    public HomeworkListener(Context context, AppCompatActivity detail){
        mContext = context;
        details = detail;
    }
    public void onCreateHome() {

        //define the layout for the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout = inflater.inflate(R.layout.activity_assign_homework, null);

        final Spinner spinner = (Spinner) layout.findViewById(R.id.spinnerGroup);
        Spinner spclass = (Spinner) layout.findViewById(R.id.spinnerClass);

        final String[] projection = new String[]{
                MyProviderContract.Group_ID,
                MyProviderContract.C_ID
        };

        spclass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] languages = details.getResources().getStringArray(R.array.Class);
                classname = languages[pos];
                cid = getClassid(classname);

                String colsToDisplay[] = new String[]{
                        MyProviderContract.Group_ID
                };

                int[] colResIds = new int[]{
                        R.id.groupid
                };
                Cursor cursor = details.getContentResolver().query(MyProviderContract.Group_URI, projection, MyProviderContract.C_ID + "=" + "'" + cid + "'", null, null);    //obtain context objection

                dataAdapter = new SimpleCursorAdapter(
                        details,
                        R.layout.db_spinner_layout,
                        cursor,
                        colsToDisplay,
                        colResIds,
                        0);

                spinner.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        final EditText hometitle1 = (EditText) layout.findViewById(R.id.hometitle1);
        final EditText homedescription1 = (EditText) layout.findViewById(R.id.homedescription1);

        Calendar c = Calendar.getInstance();
        date = (EditText) layout.findViewById(R.id.day1);
        date.setText(c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH));
        date.setInputType(InputType.TYPE_NULL); //hide system keyboard

        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                showDatePickerDialog();
            }
        });

        Button btn_sure = (Button) layout.findViewById(R.id.buttonSure);
        Button btn_cancel = (Button) layout.findViewById(R.id.buttonCancel);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                groupname = ((TextView) view.findViewById(R.id.groupid)).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final Dialog dialog = builder.create();
        android.view.Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.show();

        dialog.getWindow().setContentView(layout);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 1300;
        params.height = 1500 ;
        dialog.getWindow().setAttributes(params);
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                title = hometitle1.getText().toString();
                description = homedescription1.getText().toString();
                hDate = date.getText().toString();

                //limitations situations
                if (title.equals("") || description.equals("")){
                    Toast.makeText(details.getApplicationContext(), "You can not add!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    ContentValues newValues = new ContentValues();
                    newValues.put(MyProviderContract.TITLE, title);
                    newValues.put(MyProviderContract.DESCRIPTION, description);
                    newValues.put(MyProviderContract.C_ID, cid);
                    newValues.put(MyProviderContract.HDate, hDate);
                    newValues.put(MyProviderContract.hGROUP, groupname);
                    details.getContentResolver().insert(MyProviderContract.Homework_URI, newValues);    //update into the databases

                    Toast.makeText(details.getApplicationContext(), "Add Successful", Toast.LENGTH_LONG).show(); //if update sucessessfully
                }
                dialog.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
    }

    /*pick up the date*/
    private void showDatePickerDialog()
    {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(details, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // TODO Auto-generated method stub
                date.setText(year+"/"+(month+1)+ "/"+ day);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private String getClassid(String name){
        String[] projection = new String[]{
                MyProviderContract.cNAME,
                MyProviderContract.C_ID
        };

        Cursor cursor = details.getContentResolver().query(MyProviderContract.Class_URI, projection, MyProviderContract.cNAME + "=" + "'" + name + "'", null, null);    //obtain context objection

        while(cursor.moveToNext()){
            cid = cursor.getString(cursor.getColumnIndex("_id"));   //set the instruction
        }
        cursor.close();
        return cid;
    }
}
