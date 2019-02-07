package com.example.psyyf2.dissertation.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.psyyf2.dissertation.database.MyProviderContract;
import com.example.psyyf2.dissertation.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StudentList extends AppCompatActivity implements View.OnClickListener {

    Map<String, Integer> mapIndex;
    ListView listView;
    SimpleCursorAdapter dataAdapter;
    int i = 0;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_student_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarStudent);
        toolbar.setTitle("Student List");
        setSupportActionBar(toolbar);

        String[] projection = new String[]{
                MyProviderContract.Stu_ID,
                MyProviderContract.C_ID,
                MyProviderContract.sNAME
        };

        //display the information from database layout
        String colsToDisplay[] = new String[]{
                MyProviderContract.Stu_ID,
                MyProviderContract.sNAME
        };

        int[] colResIds = new int[]{
                R.id.stuID,
                R.id.stuName
        };

        Cursor cursor = getContentResolver().query(MyProviderContract.Student_URI, projection, null, null, "sName ASC");    //obtain context objection

        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.db_classdetail_layout,
                cursor,
                colsToDisplay,
                colResIds,
                0);

        //load the listview and use setOnItemClickListener to manage each line
        listView = (ListView) findViewById(R.id.list_student);
        listView.setAdapter(dataAdapter);

        //bound with alpha index
        getIndexList(listView);
        displayIndex();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
            {
                Intent intent = new Intent(StudentList.this, StudentDetail.class);

                final String stuID = ((TextView) myView.findViewById(R.id.stuID)).getText().toString();
                final String stuName = ((TextView) myView.findViewById(R.id.stuName)).getText().toString(); //get the text

                Bundle bundle = new Bundle();
                bundle.putString("stuID", stuID);
                bundle.putString("stuName", stuName);
                intent.putExtras(bundle);   //send ID and Title to another activity

                startActivity(intent);
            }
        });
    }

    private void getIndexList(ListView v) {
       mapIndex = new LinkedHashMap<String, Integer>();
       String[] projection = new String[]{
               MyProviderContract.Stu_ID,
               MyProviderContract.C_ID,
               MyProviderContract.sNAME
       };

       Cursor cursor = getContentResolver().query(MyProviderContract.Student_URI, projection, null, null, "sName ASC");  //search for the database to obtaion instruction

       while(cursor.moveToNext()){
           name = cursor.getString(cursor.getColumnIndex("sName"));   //set the instruction
           String index = name.substring(0,1);

           if (mapIndex.get(index) == null)
               mapIndex.put(index, i);

           i++;
       }
       cursor.close();
    }

    /* display the side index*/
    private void displayIndex() {
        LinearLayout indexLayout = (LinearLayout) findViewById(R.id.side_index);

        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        for (String index : indexList) {
            textView = (TextView) getLayoutInflater().inflate(
                    R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(this);
            indexLayout.addView(textView);
        }
    }

    /*set actions for the index list*/
    public void onClick(View view) {
        TextView selectedIndex = (TextView) view;
        listView.setSelection(mapIndex.get(selectedIndex.getText()));
    }

}