package com.example.psyyf2.dissertation.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.psyyf2.dissertation.R;
import com.example.psyyf2.dissertation.database.MyProviderContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

public class GradeDetail extends AppCompatActivity {

    String examid, cid;
    int low, less, medium, high;
    private ColumnChartView chart;
    SimpleCursorAdapter dataAdapter;
    final TreeMap<String, Integer> map = new TreeMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_grade_detail);

        Bundle bundle = getIntent().getExtras();    //get the current ID

        examid = bundle.getString("ExamID");
        cid = bundle.getString("CID");

        // setting toorbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GradeDetail.this, BaseActivity.class);
                startActivity(intent);
            }
        });

        String[] projection = new String[]{
                MyProviderContract.Exam_ID,
                MyProviderContract.GRADE,
                MyProviderContract.sNAME,
                MyProviderContract.C_ID
        };

        //display the information from database layout
        String colsToDisplay[] = new String[]{
                MyProviderContract.sNAME,
                MyProviderContract.GRADE
        };

        int[] colResIds = new int[]{
                R.id.stuName2,
                R.id.stuGrade
        };

        Cursor cursor = getContentResolver().query(MyProviderContract.Grade_URI, projection, MyProviderContract.Exam_ID + "=" + "'" + examid + "'" , null, null);  //search for the database to obtaion instruction

        while(cursor.moveToNext()){

            String grade1 = cursor.getString(cursor.getColumnIndex("grade"));   //set the instruction

            if (Integer.parseInt(grade1) < 40)
            {
                low ++;
            }
            else if (Integer.parseInt(grade1) < 60 && Integer.parseInt(grade1) > 40)
            {
                less ++;
            }
            else if (Integer.parseInt(grade1) < 70 && Integer.parseInt(grade1) > 60)
            {
                medium ++;
            }
            else if (Integer.parseInt(grade1) > 70)
            {
                high ++;
            }
        }

        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.db_grade_layout,
                cursor,
                colsToDisplay,
                colResIds,
                0);

        //load the listview and use setOnItemClickListener to manage each line
        ListView listView = (ListView) findViewById(R.id.gradelist);
        listView.setAdapter(dataAdapter);

        //bound the result into the map
        map.put("70 - 100",high);
        map.put("60 - 70",medium);
        map.put("40 - 60",less);
        map.put("40",low);

        initView();
        initData();
    }

    private void initView() {
        chart = (ColumnChartView) findViewById(R.id.test_content);
    }

    private void initData() {
        generateDefaultData();
    }

    private void generateDefaultData() {
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        List<AxisValue> axisValuess=new ArrayList<>();
        int i = 0;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {

            //traverse the value in the map
            values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue((float) entry.getValue(), ChartUtils.pickColor()));
            axisValuess.add(new AxisValue(i).setLabel(entry.getKey()));

            Column column = new Column(values);
            column.setHasLabels(true);
            columns.add(column);

            i++;
        }

        ColumnChartData data = new ColumnChartData(columns);

        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);

        axisX.setName("Grade Range");
        axisY.setName("Student Number");
        axisX.setValues(axisValuess);

        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);

        chart.setColumnChartData(data);
    }

    /* load the toorbar information*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addgrade,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("CID", cid);
        bundle.putString("ExamID", examid);

        Intent intent = new Intent(GradeDetail.this, CreateGrade.class);
        intent.putExtras(bundle);   //send ID and Title to another activity
        startActivity(intent);

        return  true;
    }
}

