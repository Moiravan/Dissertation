package com.example.psyyf2.dissertation.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psyyf2.dissertation.R;
import com.example.psyyf2.dissertation.adapter.MyAdapterCheck;
import com.example.psyyf2.dissertation.database.MyProviderContract;

import java.util.HashMap;
import java.util.Map;

public class MangeGroup extends AppCompatActivity {

    TextView mark;
    Button change,Edit;
    String[] projection;
    MyAdapterCheck dataAdapter;
    SimpleCursorAdapter adapter1;
    String cid, classname, groupname;

    private Context mContext;
    private Map<String, Boolean> map = new HashMap<String, Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_mange_group);
        mContext = this;

        Bundle bundle = getIntent().getExtras();    //get the current ID
        cid = bundle.getString("ID");
        classname = bundle.getString("CLASSNAME");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle(classname);
        setSupportActionBar(toolbar);

        change = (Button) findViewById(R.id.change);
        Edit = (Button) findViewById(R.id.Edit);
        mark = (TextView) findViewById(R.id.mark);

        String[] projection = new String[]{
                MyProviderContract.Group_ID,
                MyProviderContract.C_ID
        };

        //display the information from database layout
        String colsToDisplay[] = new String[]{
                MyProviderContract.Group_ID
        };

        int[] colResIds = new int[]{
                R.id.groupid
        };

        Cursor cursor = getContentResolver().query(MyProviderContract.Group_URI, projection, MyProviderContract.C_ID + "=" + "'" + cid + "'", null, null);    //obtain context objection

        adapter1 = new SimpleCursorAdapter(
                this,
                R.layout.db_spinner_layout,
                cursor,
                colsToDisplay,
                colResIds,
                0);

        //set the adapter in spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinnergroup);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                groupname = ((TextView) view.findViewById(R.id.groupid)).getText().toString();
                Toast.makeText(MangeGroup.this, "You select: Group"+groupname, Toast.LENGTH_LONG).show();
                query(groupname);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });
    }


    /* Change the layout */
    public void changeMode(View v){
        dataAdapter.flipEditMode();
        ListView listView = (ListView) findViewById(R.id.managelist);
        listView.setAdapter(dataAdapter);

        if(Edit.getText().toString().equals("Edit"))
        {
            change.setVisibility(View.VISIBLE);
            mark.setVisibility(View.VISIBLE);
            Edit.setText(R.string.Cancel);
        }
        else if(Edit.getText().toString().equals("Cancel")){
            change.setVisibility(View.INVISIBLE);
            mark.setVisibility(View.INVISIBLE);
            Edit.setText(R.string.Edit);
        }
    }

    public void moveGroup(View v){
        // relevant layout
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.getMenuInflater().inflate(R.menu.group, popupMenu.getMenu());

        // set click listener
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                for (Map.Entry<String, Boolean> entry : map.entrySet()) {

                 ContentValues newValues = new ContentValues();

                 if(entry.getValue())
                 {
                     newValues.put(MyProviderContract.sGROUP, item.getTitle().toString());

                     int count = getContentResolver().update(MyProviderContract.Student_URI, newValues, MyProviderContract.Stu_ID + "= ?", new String[]{entry.getKey()});    //update into the databases

                     if (count > 0)
                     {
                         Toast.makeText(MangeGroup.this, "Update Successful", Toast.LENGTH_LONG).show(); //if update sucessessfully
                         query(groupname);
                     }
                 }
                }

                return false;
            }
        });
        popupMenu.show();
    }


    public void query(String group){
        Cursor cursor;
        projection = new String[]{
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
                R.id.stuID4,
                R.id.stuName4
        };

        //verify the input value
        if (group.equals("All"))
        {
            cursor = getContentResolver().query(MyProviderContract.Student_URI, projection, MyProviderContract.C_ID + "=" + "'" + cid + "'", null, null);    //obtain context objection

        }
        else
        {
            cursor = getContentResolver().query(MyProviderContract.Student_URI, projection, MyProviderContract.C_ID + "=" + "'" + cid + "'" + "and" + " " + MyProviderContract.sGROUP + "=" + "'" + group + "'", null, null);    //obtain context objection
        }

        dataAdapter = new MyAdapterCheck(
                this,
                R.layout.db_group_layout,
                cursor,
                colsToDisplay,
                colResIds,
                0, map);

        //load the listview and use setOnItemClickListener to manage each line
        ListView listView = (ListView) findViewById(R.id.managelist);
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
            {
                Intent intent = new Intent(MangeGroup.this, StudentDetail.class);

                final String stuID = ((TextView) myView.findViewById(R.id.stuID4)).getText().toString();
                final String stuName = ((TextView) myView.findViewById(R.id.stuName4)).getText().toString(); //get the text

                Bundle bundle = new Bundle();
                bundle.putString("stuID", stuID);
                bundle.putString("stuName", stuName);
                intent.putExtras(bundle);   //send ID and Title to another activity
                startActivity(intent);
            }
        });
    }

    /* load the toorbar information*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addgroup,menu);

        return true;
    }

    /* add a group from toorbar*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_assign_group, (ViewGroup) findViewById((R.id.log)));

        final EditText groupid = (EditText) layout.findViewById(R.id.groupnumber);
        final EditText groupinfo = (EditText) layout.findViewById(R.id.groupinfo);
        Button btn_sure = (Button) layout.findViewById(R.id.buttonAddGroup);
        Button btn_cancel = (Button) layout.findViewById(R.id.buttonCancel);

        //custerm the layout design
        final Dialog dialog = builder.create();
        android.view.Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.bg_dialog);
        dialog.show();

        //the parameters setting should be implemented after show()

        dialog.getWindow().setContentView(layout);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 1200;
        params.height = 1000;
        dialog.getWindow().setAttributes(params);

        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String group_id = groupid.getText().toString();
                String group_info = groupinfo.getText().toString();

                //limitations situations
                if (group_id.equals("") || group_info.equals("")) {
                    Toast.makeText(getApplicationContext(), "You can not add!", Toast.LENGTH_LONG).show();
                } else
                {
                    ContentValues newValues = new ContentValues();
                    newValues.put(MyProviderContract.Group_ID, group_id);
                    newValues.put(MyProviderContract.GROUPINFO, group_info);
                    newValues.put(MyProviderContract.C_ID, cid);

                    getContentResolver().insert(MyProviderContract.Group_URI, newValues);    //update into the databases

                    Toast.makeText(MangeGroup.this, "Add Successful", Toast.LENGTH_LONG).show(); //if update sucessessfully
                    query(groupname);
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

        return  true;
    }
}
