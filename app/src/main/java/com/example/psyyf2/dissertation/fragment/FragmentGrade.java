package com.example.psyyf2.dissertation.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.psyyf2.dissertation.activity.GradeDetail;
import com.example.psyyf2.dissertation.adapter.MyAdapterName;
import com.example.psyyf2.dissertation.database.MyProviderContract;
import com.example.psyyf2.dissertation.R;

import java.util.Calendar;

public class FragmentGrade extends Fragment {
    View inflate;
    private EditText date;
    String cid,hDate, examid;
    MyAdapterName dataAdapter;
    String status = "";
    boolean check = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.activity_fragment_grade, null);
        Context mContext = this.getContext();


        Calendar c = Calendar.getInstance();
        date = (EditText) inflate.findViewById(R.id.gradedate);
        date.setText(c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH));
        date.setInputType(InputType.TYPE_NULL); //hide the keyboard
        hDate = date.getText().toString();
        check(hDate, status);

        //set actions with the calendar
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

        RadioGroup radioGroup = (RadioGroup) inflate.findViewById(R.id.RadioGrade);
        final RadioButton all = (RadioButton) inflate.findViewById(R.id.buttonall);
        final RadioButton release = (RadioButton) inflate.findViewById(R.id.buttonre);
        final RadioButton notRelease = (RadioButton) inflate.findViewById(R.id.buttonno);
        all.setChecked(true);

        //limitations on listview
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == all.getId())
                {
                    status = "";
                }
                else if (checkedId == release.getId())
                {
                    status ="" + "and" + " " + MyProviderContract.AVEGRADE + "!=" + "'" + "NOT RELEASE" + "'" + " ";
                }
                else if (checkedId == notRelease.getId())
                {
                    status = "" + "and" + " " +  MyProviderContract.AVEGRADE + "=" + "'" + "NOT RELEASE" + "'" + " ";
                }

                check(hDate, status);
            }
        });

        return inflate;
    }

    public void check(String examdate, String statu) {

        String[] projection = new String[]{
                MyProviderContract.Exam_ID,
                MyProviderContract.C_ID,
                MyProviderContract.ExamName,
                MyProviderContract.Subject,
                MyProviderContract.ExamDate,
                MyProviderContract.AVEGRADE
        };

        //display the information from database layout
        String colsToDisplay[] = new String[]{
                MyProviderContract.Exam_ID,
                MyProviderContract.ExamDate,
                MyProviderContract.ExamName,
                MyProviderContract.Subject,
                MyProviderContract.AVEGRADE,
                MyProviderContract.C_ID
        };

        int[] colResIds = new int[]{
                R.id.examid,
                R.id.examdate,
                R.id.examName,
                R.id.examSubject,
                R.id.exammark,
                R.id.obtainclassid
        };

        Cursor cursor = FragmentGrade.this.getActivity().getContentResolver().query(MyProviderContract.Exam_URI, projection, MyProviderContract.ExamDate + "=" + "'" + examdate + "'" + " " + statu, null, null);    //obtain context objection

        //use the adpter to obtain the classname
        dataAdapter = new MyAdapterName(
                this.getContext(),
                R.layout.db_exam_layout,
                cursor,
                colsToDisplay,
                colResIds,
                0, "grade");

        //load the listview and use setOnItemClickListener to manage each line
        ListView listView = (ListView) inflate.findViewById(R.id.gradelist1);
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
            {
                examid = ((TextView) myView.findViewById(R.id.examid)).getText().toString();
                cid = ((TextView) myView.findViewById(R.id.obtainclassid)).getText().toString();

                String[] projection1 = new String[]{
                        MyProviderContract.Exam_ID,
                };

                Cursor cursor1 = FragmentGrade.this.getActivity().getContentResolver().query(MyProviderContract.Grade_URI, projection1, MyProviderContract.Exam_ID + "=" + "'" + examid + "'", null, null);  //search for the database to obtaion instruction

                while(cursor1.moveToNext()){
                    check = true;
                    break;
                }
                cursor1.close();

                Bundle bundle = new Bundle();
                bundle.putString("CID", cid);
                bundle.putString("ExamID", examid);

                Intent intent = new Intent(FragmentGrade.this.getActivity(), GradeDetail.class);
                intent.putExtras(bundle);   //send ID and Title to another activity
                startActivity(intent);
            }
        });
    }

    private void showDatePickerDialog()
    {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(FragmentGrade.this.getContext(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // TODO Auto-generated method stub
                date.setText(year+"/"+(month+1)+ "/"+ day);
                hDate = year+"/"+(month+1)+ "/"+ day;
                check(hDate, status);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
}

