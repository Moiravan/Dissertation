package com.example.psyyf2.dissertation.fragment;

import android.app.DatePickerDialog;
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

import com.example.psyyf2.dissertation.activity.HomeworkEdit;
import com.example.psyyf2.dissertation.adapter.MyAdapterName;
import com.example.psyyf2.dissertation.database.MyProviderContract;
import com.example.psyyf2.dissertation.R;

import java.util.Calendar;

public class FragmentHomework extends Fragment {
    View inflate;
    private EditText date;
    String hDate;
    MyAdapterName dataAdapter;
    String status= "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.activity_fragment_homework, null);

        Calendar c = Calendar.getInstance();

        date = (EditText) inflate.findViewById(R.id.homedate1);
        date.setText(c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH));
        date.setInputType(InputType.TYPE_NULL); //不显示系统输入键盘</span>
        hDate = date.getText().toString();
        check(hDate, status);

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

        //use radio button to set limitations on listview
        RadioGroup radioGroup = (RadioGroup) inflate.findViewById(R.id.RadioGrade);
        final RadioButton all = (RadioButton) inflate.findViewById(R.id.buttonall1);
        final RadioButton release = (RadioButton) inflate.findViewById(R.id.buttonopen);
        final RadioButton notRelease = (RadioButton) inflate.findViewById(R.id.buttonclose);
        all.setChecked(true);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId == all.getId())
                {
                    status = "";
                }
                else if (checkedId == release.getId())
                {
                    status ="" + "and" + " " + MyProviderContract.REA + "=" + "'" + "open" + "'" + " ";
                }
                else if (checkedId == notRelease.getId())
                {
                    status = "" + "and" + " " +  MyProviderContract.REA + "=" + "'" + "close" + "'" + " ";
                }

                check(hDate, status);
            }
        });

        return inflate;

    }

    public void check(String s, String statu) {

        String[] projection = new String[]{
                MyProviderContract.Home_ID,
                MyProviderContract.C_ID,
                MyProviderContract.TITLE,
                MyProviderContract.DESCRIPTION,
                MyProviderContract.HDate,
                MyProviderContract.hGROUP,
                MyProviderContract.REA
        };

        //display the information from database layout
        String colsToDisplay[] = new String[]{
                MyProviderContract.Home_ID,
                MyProviderContract.TITLE,
                MyProviderContract.DESCRIPTION,
                MyProviderContract.hGROUP,
                MyProviderContract.C_ID,
        };

        int[] colResIds = new int[]{
                R.id.homeID,
                R.id.hometitle,
                R.id.homedescription,
                R.id.homegroup,
                R.id.obtainclassid1
        };

        Cursor cursor = FragmentHomework.this.getActivity().getContentResolver().query(MyProviderContract.Homework_URI, projection, MyProviderContract.HDate + "=" + "'" + s + "'" + " " + statu, null, null);    //obtain context objection

        //set the class name
        dataAdapter = new MyAdapterName(
                this.getContext(),
                R.layout.db_homework_layout,
                cursor,
                colsToDisplay,
                colResIds,
                0, "homework");

        //load the listview and use setOnItemClickListener to manage each line
        ListView listView = (ListView) inflate.findViewById(R.id.homelist1);
        listView.setAdapter(dataAdapter);
        // dataAdapter.notifyDataSetChanged();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                Intent intent = new Intent(FragmentHomework.this.getContext(), HomeworkEdit.class);

                final String id = ((TextView) myView.findViewById(R.id.homeID)).getText().toString();
                final String cid = ((TextView) myView.findViewById(R.id.obtainclassid1)).getText().toString();
                final String groupname = ((TextView) myView.findViewById(R.id.homegroup)).getText().toString(); //get the text

                Bundle bundle = new Bundle();
                bundle.putString("HID", id);
                bundle.putString("ID", cid);
                bundle.putString("GROUP", groupname);
                intent.putExtras(bundle);   //send ID and Title to another activity

                startActivity(intent);
            }
        });
    }

    private void showDatePickerDialog()
    {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(FragmentHomework.this.getContext(), new DatePickerDialog.OnDateSetListener() {

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

