package com.example.psyyf2.dissertation.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.psyyf2.dissertation.database.MyProviderContract;
import com.example.psyyf2.dissertation.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class StudentDetail extends AppCompatActivity {

    String stuID, stuName, group, telephone;
    private LineChartView lineChart;
    TextView SetID, SetName, SetGroup;
    String grade1, subject;
    private ArrayList<String> date = new ArrayList<String>();
    private ArrayList<Integer> grade = new ArrayList<Integer>();

    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);

        lineChart = (LineChartView)findViewById(R.id.line_chart1);

        Bundle bundle = getIntent().getExtras();    //get the current ID
        stuID = bundle.getString("stuID");
        stuName = bundle.getString("stuName");

        check();

        //select the subject
        Spinner spinner = (Spinner) findViewById(R.id.spinnerstudent);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] Subject = getResources().getStringArray(R.array.Subject);
                subject = Subject[pos];
                showgrade(subject);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        ImageButton callphone  = (ImageButton)findViewById(R.id.call);
        callphone.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + telephone));
                startActivity(intent);
            }
        });

        ImageButton email  = (ImageButton)findViewById(R.id.sendemail);
        email.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(StudentDetail.this, ChatPage.class);
                Bundle bundle = new Bundle();
                bundle.putString("stuName", stuName);
                intent.putExtras(bundle);   //send ID and Title to another activity
                startActivity(intent);
            }
        });
    }

    /* initial the chart parameters */
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(false);
        line.setFilled(false);
        line.setHasLabels(true);
        line.setHasLines(true);
        line.setHasPoints(true);
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.BLACK);

        axisX.setName("Exam Date");
        axisX.setTextSize(11);

        //maximize elements
        axisX.setMaxLabelChars(7);
        axisX.setValues(mAxisXValues);
        data.setAxisXBottom(axisX);
        axisX.setHasLines(true);


        Axis axisY = new Axis();
        axisY.setTextColor(Color.BLACK);
        axisY.setName("GRADE");
        axisY.setTextSize(11);
        data.setAxisYLeft(axisY);

        //action settings
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 3);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        Viewport v1 = new Viewport(lineChart.getMaximumViewport());
        v1.bottom = 0;
        v1.top = 100;
        lineChart.setMaximumViewport(v1);

        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
    }

    private void getAxisXLables(){

        for (int i = 0; i < date.size(); i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date.get(i)));
        }
    }

    private void getAxisPoints(){
        for (int i = 0; i < grade.size(); i++) {
            mPointValues.add(new PointValue(i, grade.get(i)));
        }
    }

    private void showgrade(String s){

        //clear the current diagram
        mPointValues.clear();
        mAxisXValues.clear();
        date.clear();
        grade.clear();
        List<String> exam = new ArrayList<String>();

        String[] projection = new String[] {
                MyProviderContract.Exam_ID,
                MyProviderContract.Subject,
                MyProviderContract.ExamDate
        };

        Cursor cursor = getContentResolver().query(MyProviderContract.Exam_URI, projection, MyProviderContract.Subject + "=" + "'" + s + "'", null, null);  //search for the database to obtaion instruction

        while(cursor.moveToNext()){
            String examid = cursor.getString(cursor.getColumnIndex("Exam_ID"));   //set the instruction
            String examdate = cursor.getString(cursor.getColumnIndex("mDate"));   //set the instruction

            exam.add(examid);
            date.add(examdate);
        }

        cursor.close();

        String[] projection1 = new String[] {
                MyProviderContract.Stu_ID,
                MyProviderContract.GRADE,
                MyProviderContract.Exam_ID
        };

        Cursor cursor1 = getContentResolver().query(MyProviderContract.Grade_URI, projection1, MyProviderContract.Stu_ID + "=" + "'" + stuID + "'", null, null);  //search for the database to obtaion instruction

        while(cursor1.moveToNext()){
            grade1 = cursor1.getString(cursor1.getColumnIndex("grade"));   //set the instruction
            String examid2 = cursor1.getString(cursor1.getColumnIndex("Exam_ID"));   //set the instruction

           if(exam.contains(examid2) && !grade1.equals("NOT RELEASE"))
           {
               grade.add(Integer.parseInt(grade1));
           }
        }
        cursor1.close();

        getAxisXLables();
        getAxisPoints();
        initLineChart();
    }

    /* Load student information */
    void check(){

        String[] projection = new String[] {
                MyProviderContract.Stu_ID,
                MyProviderContract.sNAME,
                MyProviderContract.sGROUP,
                MyProviderContract.sTELEPHONE
        };

        Cursor cursor = getContentResolver().query(MyProviderContract.Student_URI, projection, MyProviderContract.Stu_ID + "=" + "'" + stuID + "'" , null, null);  //search for the database to obtaion instruction

        while(cursor.moveToNext()){
            group = cursor.getString(cursor.getColumnIndex("sGroup"));   //set the instruction
            telephone = cursor.getString(cursor.getColumnIndex("sTelephone"));   //set the instruction
        }

        cursor.close();

        SetID = (TextView) findViewById(R.id.SetID);
        SetName = (TextView) findViewById(R.id.SetName);
        SetGroup = (TextView) findViewById(R.id.SetGroup);

        SetID.setText(stuID);
        SetName.setText(stuName);
        SetGroup.setText(group);
    }
}
