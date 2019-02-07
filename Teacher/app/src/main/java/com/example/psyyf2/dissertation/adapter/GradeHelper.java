package com.example.psyyf2.dissertation.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.psyyf2.dissertation.database.MyProviderContract;
import com.example.psyyf2.dissertation.R;

import java.util.Calendar;

/**
 * Created by moiravan on 2018/4/13.
 */

public class GradeHelper {

    private Context mContext;
    private String Subject, Name, examDate,classname;
    private AppCompatActivity details;
    private String cid = null;
    private EditText date;

    public GradeHelper(Context context, AppCompatActivity detail) {
        mContext = context;
        details = detail;
    }

    public void onCreateGrade() {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View layout = inflater.inflate(R.layout.activity_create_exam, null);

        final EditText examName = (EditText) layout.findViewById(R.id.examName);
        Spinner spsubject = (Spinner) layout.findViewById(R.id.spinnerSub);
        Spinner spclass = (Spinner) layout.findViewById(R.id.spinnerClass);


        spsubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] languages = details.getResources().getStringArray(R.array.Subject);
                Subject = languages[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        spclass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] languages = details.getResources().getStringArray(R.array.Class);
                classname = languages[pos];
                cid = getClassid(classname);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        //load the calendar
        Calendar c = Calendar.getInstance();
        date = (EditText) layout.findViewById(R.id.day);
        date.setText( c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH));
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

        Button btn_sure = (Button) layout.findViewById(R.id.addexam);
        Button btn_cancel = (Button) layout.findViewById(R.id.cancelexam);

        final Dialog dialog = builder.create();
        android.view.Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.show();

        //Parameter settings
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 1300;
        params.height = 800;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().setGravity(Gravity.CENTER);
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Name = examName.getText().toString();
                examDate = date.getText().toString();

                //limitations situations
                if (Name.equals("") || Subject.equals("") || examDate.equals("")) {
                    Toast.makeText(details.getApplicationContext(), "You can not add!", Toast.LENGTH_LONG).show();
                } else {
                    ContentValues newValues = new ContentValues();
                    newValues.put(MyProviderContract.ExamName, Name);
                    newValues.put(MyProviderContract.Subject, Subject);
                    newValues.put(MyProviderContract.ExamDate, examDate);
                    newValues.put(MyProviderContract.C_ID, cid);

                    details.getContentResolver().insert(MyProviderContract.Exam_URI, newValues);    //update into the databases
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

    //pick up the date
    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(details, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // TODO Auto-generated method stub
                date.setText(year + "/" + (month + 1) + "/" + day);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    /*obtain the class id */
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
