package com.example.psyyf2.dissertation.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.psyyf2.dissertation.R;
import com.example.psyyf2.dissertation.database.MyProviderContract;

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

public class GradeAnalysis extends AppCompatActivity {

    private LineChartView lineChart;
    String date1, grade1, cid;
    private ArrayList<String> date = new ArrayList<String>();
    private ArrayList<Integer> grade = new ArrayList<Integer>();

    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private int checkedItemId = R.id.id1;
    private int checkedItemIdsub = R.id.math;
    String classname = "1";
    String subject = "Math";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_grade_analysis);

        Toolbar mNormalToolbar= (Toolbar) findViewById(R.id.toolbar_normal);
        setSupportActionBar(mNormalToolbar);

        mNormalToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GradeAnalysis.this, BaseActivity.class);
                    startActivity(intent);
                }
            });

        lineChart = (LineChartView)findViewById(R.id.line_chart);
        chooseSubject(subject, classname);

        getAxisXLables();
        getAxisPoints();
        initLineChart();
    }

    /* initialize the diagram settings */
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);   //shape of the point
        line.setCubic(false);
        line.setFilled(false);
        line.setHasLabels(true);
        line.setHasLines(true);
        line.setHasPoints(true);
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //Axis X
        Axis axisX = new Axis();
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.BLACK);

        axisX.setName("Exam Date");
        axisX.setTextSize(11);
        axisX.setMaxLabelChars(7);  //number 7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);
        data.setAxisXBottom(axisX);
        axisX.setHasLines(true);

        //Axis Y
        Axis axisY = new Axis();
        axisY.setTextColor(Color.BLACK);
        axisY.setName("GRADE");
        axisY.setTextSize(11);
        data.setAxisYLeft(axisY);

        //Action settings
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 3);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        Viewport v1 = new Viewport(lineChart.getMaximumViewport());
        v1.bottom = 0;
        v1.top = 100;
        //set the range of axis Y
        lineChart.setMaximumViewport(v1);

        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
    }

    /* Load the data*/
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

    private void chooseSubject(String subject, String classname){

        //clear the current data
        mPointValues.clear();
        mAxisXValues.clear();
        date.clear();
        grade.clear();

        TextView gradesubject = (TextView) findViewById(R.id.gradesubject);
        gradesubject.setText(subject);

        String[] projection = new String[] {
                MyProviderContract.Exam_ID,
                MyProviderContract.Subject,
                MyProviderContract.AVEGRADE,
                MyProviderContract.ExamDate
        };

        Cursor cursor = getContentResolver().query(MyProviderContract.Exam_URI, projection, MyProviderContract.Subject + "=" + "'" + subject + "'" + "and" + " " + MyProviderContract.C_ID + "=" + "'" + classname + "'", null, null);  //search for the database to obtaion instruction

        while(cursor.moveToNext()){
            date1 = cursor.getString(cursor.getColumnIndex("mDate"));   //set the instruction
            grade1 = cursor.getString(cursor.getColumnIndex("aveGrade"));   //set the instruction

            if (!grade1.equals("NOT RELEASE"))
            {
                date.add(date1);
                grade.add(Integer.parseInt(grade1));
            }
        }

        cursor.close();

        getAxisXLables();
        getAxisPoints();
        initLineChart();
    }

    public void chooseClass(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.choose_class, popup.getMenu());
        popup.getMenu().findItem(checkedItemId).setChecked(true);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                checkedItemId = menuItem.getItemId();
                classname = menuItem.getTitle().toString();
                String[] projection1 = new String[] {
                        MyProviderContract.C_ID,
                        MyProviderContract.cNAME
                };

                Cursor cursor = getContentResolver().query(MyProviderContract.Class_URI, projection1, MyProviderContract.cNAME + "=" + "'" + classname + "'", null, null);  //search for the database to obtaion instruction

                while(cursor.moveToNext()){
                    cid = cursor.getString(cursor.getColumnIndex("_id"));   //set the instruction
                }

                cursor.close();
                chooseSubject(subject, cid);
                return true;
            }
        });

        popup.show(); //showing popup menu
    }

    public void chooseSubject(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.choose_subject, popup.getMenu());
        popup.getMenu().findItem(checkedItemIdsub).setChecked(true);

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                checkedItemIdsub = item.getItemId();
                subject = item.getTitle().toString();
                chooseSubject(subject, classname);
                return true;
            }
        });

        popup.show(); //showing popup menu
    }
}