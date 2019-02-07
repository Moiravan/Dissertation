package com.example.psyyf2.parent.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.example.psyyf2.parent.Adapter.MyAdapterGrade;
import com.example.psyyf2.parent.Database.MyProviderContract;
import com.example.psyyf2.parent.R;

import java.util.Calendar;

public class Grade extends AppCompatActivity {
    MyAdapterGrade dataAdapter;
    String name, stuid, cid, examdate;
    private Toolbar NormalToolbar;
    private EditText date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_grade);

        Bundle bundle = getIntent().getExtras();    //get the current ID
        cid = bundle.getString("CID");
        stuid = bundle.getString("Stu_ID");
        name = bundle.getString("Name");

        NormalToolbar= (Toolbar) findViewById(R.id.toolbar_normal);
        setSupportActionBar(NormalToolbar);

        //load calendar for changing date
        Calendar c = Calendar.getInstance();
        date = (EditText)findViewById(R.id.examdate);
        date.setText(c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+ "/"+ c.get(Calendar.DAY_OF_MONTH));
        examdate = date.getText().toString();
        querylist(examdate);

        date.setInputType(InputType.TYPE_NULL);     //hide system keyboard
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toorbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                querylist(examdate);
                break;
            case R.id.item2:
                backMain();
                break;
        }
        return  true;
    }

    void backMain()
    {
        Intent intent = new Intent(Grade.this, BaseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Name", name);
        intent.putExtras(bundle);   //send ID and Title to another activity
        startActivity(intent);
    }

    void querylist(String date)
    {
        String[] projection = new String[]{
                MyProviderContract.C_ID,
                MyProviderContract.Exam_ID,
                MyProviderContract.Subject,
                MyProviderContract.ExamName,
                MyProviderContract.ExamDate,
                MyProviderContract.AVEGRADE
        };

        //display the information from database layout
        String colsToDisplay [] = new String[] {
                MyProviderContract.Subject,
                MyProviderContract.Exam_ID,
                MyProviderContract.ExamName,
                MyProviderContract.ExamDate,
                MyProviderContract.AVEGRADE
        };

        int[] colResIds = new int[] {
                R.id.subject,
                R.id.examid,
                R.id.name,
                R.id.date,
                R.id.avemark
        };

        Cursor cursor = getContentResolver().query(MyProviderContract.Exam_URI, projection, MyProviderContract.C_ID + "=" + "'" + cid + "'" + "and"+ " " + MyProviderContract.ExamDate + "=" + "'" + date + "'" + " ", null, null);    //obtain context objection

        dataAdapter = new MyAdapterGrade(
                this,
                R.layout.db_exam_layout,
                cursor,
                colsToDisplay,
                colResIds,
                0, stuid);

        //load the listview and use setOnItemClickListener to manage each line
        ListView listView = (ListView) findViewById(R.id.examlist);
        listView.setAdapter(dataAdapter);
    }
    private void showDatePickerDialog()
    {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(Grade.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // TODO Auto-generated method stub
                date.setText(year+"/"+(month+1)+ "/"+ day);
                examdate = date.getText().toString();
                querylist(examdate);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
}
