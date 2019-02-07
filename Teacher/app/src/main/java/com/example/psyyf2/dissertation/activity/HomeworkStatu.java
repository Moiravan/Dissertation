package com.example.psyyf2.dissertation.activity;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.psyyf2.dissertation.R;
import com.example.psyyf2.dissertation.adapter.MyAdapterName;
import com.example.psyyf2.dissertation.database.MyProviderContract;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class HomeworkStatu extends AppCompatActivity {

    String homeID,cid, status;
    SimpleCursorAdapter dataAdapter;
    private PieChartView pieChart;
    List<SliceValue> values = new ArrayList<SliceValue>();
    private int finished, notfinished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_statu);

        pieChart = (PieChartView) findViewById(R.id.Chart1);

        Bundle bundle = getIntent().getExtras();    //get the current ID
        homeID = bundle.getString("HID");
        cid = bundle.getString("ID");

        String[] projection = new String[]{
                MyProviderContract.Home_ID,
                MyProviderContract.C_ID,
                MyProviderContract.Stu_ID,
                MyProviderContract.STATU
        };

        //display the information from database layout
        String colsToDisplay[] = new String[]{
                MyProviderContract.Stu_ID,
                MyProviderContract.STATU
        };

        int[] colResIds = new int[]{
                R.id.stuID5,
                R.id.statu1
        };

        Cursor cursor = getContentResolver().query(MyProviderContract.Statu_URI, projection, MyProviderContract.Home_ID + "=" + "'" + homeID + "'" , null, null);  //search for the database to obtaion instruction

        //cursor the homework status
        while(cursor.moveToNext()){
            status = cursor.getString(cursor.getColumnIndex("statu"));   //set the instruction

            if(status.equals("Not finished"))
            {
                notfinished++;
            }
            else
            {
                finished++;
            }
        }

        dataAdapter = new MyAdapterName(
                this,
                R.layout.db_statu_layout,
                cursor,
                colsToDisplay,
                colResIds,
                0, "names");

        //load the listview and use setOnItemClickListener to manage each line
        ListView listView = (ListView) findViewById(R.id.statulist);
        listView.setAdapter(dataAdapter);

        setPieChartData();
        initPieChart();
    }

    private void setPieChartData(){

        SliceValue sliceValue = new SliceValue((float) finished,  ChartUtils.pickColor());  //generate color randomly
        sliceValue.setLabel("Finished  "+(int)sliceValue.getValue());   //set labels
        values.add(sliceValue);
        SliceValue sliceValue1 = new SliceValue((float) notfinished,  ChartUtils.pickColor());
        sliceValue1.setLabel("Not Finished  "+(int)sliceValue1.getValue());
        values.add(sliceValue1);
    }

    /* Initial the pie chart*/
    private void initPieChart()
    {
        PieChartData pieChardata = new PieChartData();
        pieChardata.setHasLabels(true);
        pieChardata.setHasLabelsOnlyForSelected(false);
        pieChardata.setHasLabelsOutside(false);
        pieChardata.setHasCenterCircle(true);
        pieChardata.setValues(values);  //load data
        pieChardata.setCenterCircleColor(Color.WHITE);
        pieChardata.setCenterCircleScale(0.5f);
        pieChardata.setCenterText1("Status");
        pieChardata.setCenterText1Color(Color.BLACK);
        pieChardata.setCenterText1FontSize(14);


        pieChart.setPieChartData(pieChardata);
        pieChart.setValueSelectionEnabled(true);
        pieChart.setAlpha(0.9f);
        pieChart.setCircleFillRatio(1f);
    }
}
