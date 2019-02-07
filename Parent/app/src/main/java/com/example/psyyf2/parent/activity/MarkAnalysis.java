package com.example.psyyf2.parent.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.psyyf2.parent.Database.MyProviderContract;
import com.example.psyyf2.parent.R;

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


public class MarkAnalysis extends AppCompatActivity {

    String name, subject, stuID, stuName, tName, tPhone;
    private LineChartView lineChart;
    private Toolbar NormalToolbar;
    private ArrayList<String> date = new ArrayList<String>();
    private ArrayList<Integer> grade = new ArrayList<Integer>();

    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mark_analysis);

        NormalToolbar= (Toolbar) findViewById(R.id.toolbar_normal3);
        setSupportActionBar(NormalToolbar);
        lineChart = (LineChartView) findViewById(R.id.line_chart);

        Bundle bundle = getIntent().getExtras();    //get the current ID
        name = bundle.getString("Name");
        subject = bundle.getString("Subject");
        stuID = bundle.getString("Stu_ID");
        stuName = bundle.getString("stuName");

        String[] projection = new String[] {
                MyProviderContract.tNAME,
                MyProviderContract.tPhone,
                MyProviderContract.tSubject
        };

        Cursor cursor = getContentResolver().query(MyProviderContract.Teacher_URI, projection, MyProviderContract.tSubject + "=" + "'" + subject + "'", null, null);  //search for the database to obtaion instruction

        while(cursor.moveToNext()) {
            tName = cursor.getString(cursor.getColumnIndex("tName"));   //set the instruction
            tPhone = cursor.getString(cursor.getColumnIndex("tPhone"));   //set the instruction
        }

        cursor.close();

        TextView subjectname = (TextView) findViewById(R.id.subjectAna);
        TextView teachername = (TextView) findViewById(R.id.teachername);

        subjectname.setText(subject);
        teachername.setText(tName);

        NormalToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MarkAnalysis.this, BaseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                intent.putExtras(bundle);   //send ID and Title to another activity
                startActivity(intent);
            }
        });

        showgrade(subject);

        ImageButton callphone  = (ImageButton)findViewById(R.id.call);
        callphone.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + tPhone));
                startActivity(intent);
            }
        });

        ImageButton email  = (ImageButton)findViewById(R.id.sendemail);
        email.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MarkAnalysis.this, ChatPageParent.class);
                Bundle bundle = new Bundle();
                bundle.putString("stuName", stuName);
                bundle.putString("teaName", tName);
                intent.putExtras(bundle);   //send ID and Title to another activity
                startActivity(intent);
            }
        });

    }

    /*Initial settings for chart*/
    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);
        line.setCubic(false);
        line.setFilled(false);
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);//

        axisX.setName("Exam Date");  //表格名称
        axisX.setTextSize(11);//设置字体大小


        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        axisX.setHasLines(true); //x 轴分割线


        Axis axisY = new Axis();  //Y轴
        axisY.setTextColor(Color.BLACK);
        axisY.setName("GRADE");//y轴标注
        axisY.setTextSize(11);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边

        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        lineChart.setMaxZoom((float) 3);//缩放比例
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);

        Viewport v1 = new Viewport(lineChart.getMaximumViewport());
        v1.bottom = 0;
        v1.top = 100;
        //固定Y轴的范围,如果没有这个,Y轴的范围会根据数据的最大值和最小值决定,这不是我想要的
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
            String examdate = cursor.getString(cursor.getColumnIndex("mDate"));   //set the instructi

            date.add(examdate);
            exam.add(examid);
        }

        cursor.close();

        String[] projection1 = new String[] {
                MyProviderContract.Stu_ID,
                MyProviderContract.GRADE,
                MyProviderContract.Exam_ID,
        };

        Cursor cursor1 = getContentResolver().query(MyProviderContract.Grade_URI, projection1, MyProviderContract.Stu_ID + "=" + "'" + stuID + "'", null, null);  //search for the database to obtaion instruction

        while(cursor1.moveToNext()) {
            String grade1 = cursor1.getString(cursor1.getColumnIndex("grade"));   //set the instruction
            String examID = cursor1.getString(cursor1.getColumnIndex("Exam_ID"));   //set the instruction

            if(exam.contains(examID) && !grade1.equals("NOT RELEASE"))
            {
                grade.add(Integer.parseInt(grade1));
            }
        }
        cursor.close();

        getAxisXLables();//获取x轴的标注
        getAxisPoints();//获取坐标点
        initLineChart();//初始化
    }


}
