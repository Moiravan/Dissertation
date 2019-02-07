package com.example.psyyf2.parent.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psyyf2.parent.Adapter.MyAdapter;
import com.example.psyyf2.parent.Database.MyProviderContract;
import com.example.psyyf2.parent.R;

import java.util.Calendar;

public class Homework extends AppCompatActivity {

    MyAdapter dataAdapter;
    private EditText date;
    String Group_ID, Stu_ID, check, homeworkdate, Class_ID, name;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_homework);

        mContext = this;

        Toolbar NormalToolbar= (Toolbar) findViewById(R.id.toolbar_normal);
        setSupportActionBar(NormalToolbar);

        Bundle bundle = getIntent().getExtras();    //get the current ID
        Stu_ID = bundle.getString("Stu_ID");
        Group_ID = bundle.getString("Group_ID");
        Class_ID = bundle.getString("CID");
        name = bundle.getString("Name");

        //load the calendat
        Calendar c = Calendar.getInstance();
        date = (EditText)findViewById(R.id.homedate);
        date.setText(c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+ "/"+ c.get(Calendar.DAY_OF_MONTH));
        homeworkdate = date.getText().toString();

        querylist(homeworkdate);

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

    private void querylist(String date)
    {
        String[] projection = new String[]{
                MyProviderContract.C_ID,
                MyProviderContract.Home_ID,
                MyProviderContract.TITLE,
                MyProviderContract.DESCRIPTION,
                MyProviderContract.HDate,
                MyProviderContract.hGROUP,
                MyProviderContract.REA
        };

        //display the information from database layout
        String colsToDisplay [] = new String[] {
                MyProviderContract.Home_ID,
                MyProviderContract.TITLE,
                MyProviderContract.DESCRIPTION,
                MyProviderContract.HDate
        };

        int[] colResIds = new int[] {
                R.id.homeID,
                R.id.hometitle,
                R.id.homedescription,
                R.id.homedates
        };

        Cursor cursor = getContentResolver().query(MyProviderContract.Homework_URI, projection,  MyProviderContract.HDate + "=" + "'" + date + "'" + " "+ "and" + " "  + MyProviderContract.REA + "=" + "'" + "open" + "'", null, null);    //obtain context objection
        dataAdapter = new MyAdapter(
                this,
                R.layout.db_homework_layout,
                cursor,
                colsToDisplay,
                colResIds,
                0, Stu_ID);

        //load the listview and use setOnItemClickListener to manage each line
        ListView listView = (ListView) findViewById(R.id.homelist);
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View layout = inflater.inflate(R.layout.activity_homework_detail, null);

                TextView ViewTitle = (TextView) layout.findViewById(R.id.hometitle2);
                TextView ViewDetail = (TextView) layout.findViewById(R.id.homedescription2);

                Button btn_sure = (Button) layout.findViewById(R.id.btnsave);
                Button btn_cancel = (Button) layout.findViewById(R.id.btncancel);

                final String id = ((TextView) myView.findViewById(R.id.homeID)).getText().toString();
                final String title = ((TextView) myView.findViewById(R.id.hometitle)).getText().toString();
                final String description = ((TextView) myView.findViewById(R.id.homedescription)).getText().toString();
                final String statu = ((TextView) myView.findViewById(R.id.homestatu)).getText().toString();

                ViewTitle.setText(title);
                ViewDetail.setText(description);

                final Dialog dialog = builder.create();
                android.view.Window window = dialog.getWindow();
                window.setBackgroundDrawableResource(R.drawable.bg_dialog);
                dialog.show();
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = 1100;
                params.height = 1300 ;
                dialog.getWindow().setAttributes(params);
                dialog.getWindow().setContentView(layout);
                dialog.getWindow().setGravity(Gravity.CENTER);

                final CheckBox isChecked = (CheckBox)layout.findViewById(R.id.checkBox2);

                //monitor with homework status
                if(statu.equals("Finished"))
                {
                    isChecked.setChecked(true);
                }
                isChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {

                        if(isChecked)
                        {
                            check = "Finished";
                        }
                        else
                        {
                            check = "Not Finished";
                        }
                    }
                });

                btn_sure.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        ContentValues newValues = new ContentValues();
                        newValues.put(MyProviderContract.STATU, check);

                        int count = getContentResolver().update(MyProviderContract.Statu_URI, newValues, MyProviderContract.Home_ID + "= ?" + " "+ "and" + " " + MyProviderContract.Stu_ID + "=?", new String[]{id, Stu_ID});    //update into the databases

                        if (count > 0)
                        {
                            Toast.makeText(Homework.this, "Save Successful", Toast.LENGTH_LONG).show(); //if update sucessessfully
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
                querylist(homeworkdate);    //refresh the layout
                break;
            case R.id.item2:
                Intent intent = new Intent(Homework.this, BaseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                intent.putExtras(bundle);   //send ID and Title to another activity
                startActivity(intent);
                break;

        }
        return  true;
    }

    private void showDatePickerDialog()
    {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(Homework.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // TODO Auto-generated method stub
                date.setText(year+"/"+(month+1)+ "/"+ day);
                homeworkdate = date.getText().toString();
                querylist(homeworkdate);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }
}
