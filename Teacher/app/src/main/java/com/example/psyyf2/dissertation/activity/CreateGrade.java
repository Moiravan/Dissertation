package com.example.psyyf2.dissertation.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.psyyf2.dissertation.R;
import com.example.psyyf2.dissertation.adapter.MyAdapter;
import com.example.psyyf2.dissertation.database.MyProviderContract;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CreateGrade extends AppCompatActivity {

    SimpleCursorAdapter dataAdapter;
    final Map<String, String> map = new HashMap<String, String>();

    String cid, examid, stuName;
    int aveGrade, count, sumGrade;
    String[] projection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_grade);

        Bundle bundle = getIntent().getExtras();    //get the current ID
        cid = bundle.getString("CID");
        examid = bundle.getString("ExamID");

        projection = new String[]{
                MyProviderContract.Stu_ID,
                MyProviderContract.C_ID,
                MyProviderContract.sNAME
        };

        //display the information from database layout
        String colsToDisplay[] = new String[]{
                MyProviderContract.sNAME,
                MyProviderContract.Stu_ID
        };

        int[] colResIds = new int[]{
                R.id.stuName1,
                R.id.stuid
        };

        Cursor cursor = getContentResolver().query(MyProviderContract.Student_URI, projection, MyProviderContract.C_ID + "=" + "'" + cid + "'", null, null);    //obtain context objection

        dataAdapter = new MyAdapter(
                this,
                R.layout.db_assign_grade,
                cursor,
                colsToDisplay,
                colResIds,
                0, map, examid);

        //load the listview and use setOnItemClickListener to manage each line
        ListView listView = (ListView) findViewById(R.id.gradelist);
        listView.setAdapter(dataAdapter);

        cursor.close();
    }

    public void insertGrade(View v){
        for (Map.Entry<String, String> entry : map.entrySet()) {    //traverse the map

            Cursor cursor1 = getContentResolver().query(MyProviderContract.Student_URI, projection, MyProviderContract.Stu_ID + "=" + "'" + entry.getKey() + "'", null, null);    //obtain context objection

            while(cursor1.moveToNext()){
                stuName = cursor1.getString(cursor1.getColumnIndex("sName")); ;
                break;
            }

            cursor1.close();

            ContentValues newValues = new ContentValues();
            newValues.put(MyProviderContract.Stu_ID, entry.getKey());
            newValues.put(MyProviderContract.sNAME, stuName);
            newValues.put(MyProviderContract.GRADE, entry.getValue());
            newValues.put(MyProviderContract.Exam_ID, examid);

            getContentResolver().insert(MyProviderContract.Grade_URI, newValues);    //update into the databases
        }

        String[] projection = new String[]{
                MyProviderContract.GRADE,
                MyProviderContract.Exam_ID,
        };

        Cursor cursor2 = getContentResolver().query(MyProviderContract.Grade_URI, projection, MyProviderContract.Exam_ID + "=" + "'" + examid + "'" , null, null);  //search for the database to obtaion instruction

        //count the average mark
        while(cursor2.moveToNext()) {

            Integer grade = cursor2.getInt(cursor2.getColumnIndex("grade"));   //set the instruction
            sumGrade = sumGrade + grade;
            count ++;
        }

        aveGrade = sumGrade / count;

        ContentValues newValues = new ContentValues();
        newValues.put(MyProviderContract.AVEGRADE, aveGrade);

        int count = getContentResolver().update(MyProviderContract.Exam_URI, newValues, MyProviderContract.Exam_ID + "= ?", new String[]{examid});    //update into the databases

        if (count > 0)
        {
            new SweetAlertDialog(this)
                    .setTitleText("Update successfully")
                    .show();

            Intent intent = new Intent(CreateGrade.this, GradeDetail.class);
            Bundle bundle = new Bundle();
            bundle.putString("ExamID", examid);
            bundle.putString("CID", cid);

            intent.putExtras(bundle);   //send ID and Title to another activity
            startActivity(intent);
        }
        else
        {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Something wrong!")
                    .show();
        }
    }
}
