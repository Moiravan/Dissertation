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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psyyf2.dissertation.R;
import com.example.psyyf2.dissertation.database.MyProviderContract;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeworkEdit extends AppCompatActivity {

    String homeID, title, description,cid,classname, statu, homegroup;
    ImageButton imageButtonDelete, imageButtonEdit;
    TextView ViewTitle, ViewDetail;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_homework_edit);
        mContext = this;

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar_normal3);
        toolbar.setTitle("Edit Homework");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeworkEdit.this, BaseActivity.class);
                startActivity(intent);
            }
        });

        imageButtonDelete = (ImageButton) findViewById(R.id.imageButtonDelete);
        imageButtonEdit = (ImageButton) findViewById(R.id.imageButtonEdit);

        //get the current ID
        Bundle bundle = getIntent().getExtras();
        homeID = bundle.getString("HID");
        cid = bundle.getString("ID");
        classname = bundle.getString("CLASSNAME");
        homegroup = bundle.getString("GROUP");

        ViewTitle = (TextView) findViewById(R.id.hometitle2);
        ViewDetail = (TextView) findViewById(R.id.homedescription2);

        check();

        final Switch mSwitch = (Switch) findViewById(R.id.switch1);
        final TextView openhomework = (TextView) findViewById(R.id.textView15);

        //hide the button if the homework is already released
        if (statu.equals("open") )
        {
            mSwitch.setVisibility(View.INVISIBLE);
            openhomework.setText(R.string.Notenter);
        }
        else
        {   // listen the check box status
            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked ){

                        openhomework();

                        mSwitch.setVisibility(View.INVISIBLE);
                        ContentValues newValues = new ContentValues();
                        statu = "open";
                        newValues.put(MyProviderContract.REA, statu);

                        int count = getContentResolver().update(MyProviderContract.Homework_URI, newValues, MyProviderContract.Home_ID + "= ?", new String[]{homeID});    //update into the databases

                        if (count > 0)
                        {
                            new SweetAlertDialog(HomeworkEdit.this)
                                    .setTitleText("Update successfully")
                                    .show();                        }
                        openhomework.setText(R.string.Notenter);

                    }
                }
            });
        }

        imageButtonEdit.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //define the layout for the alert dialog

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                LayoutInflater inflater = LayoutInflater.from(mContext);
                View layout = inflater.inflate(R.layout.activity_edit_homework, null);

                final EditText hometitle1 = (EditText) layout.findViewById(R.id.hometitle4);
                final EditText homedescription1 = (EditText) layout.findViewById(R.id.homedescription4);

                Button btn_sure = (Button) layout.findViewById(R.id.buttonAssign);
                Button btn_cancel = (Button) layout.findViewById(R.id.buttonCancel);

                hometitle1.setText(title);
                homedescription1.setText(description);

                final Dialog dialog = builder.create();
                android.view.Window window = dialog.getWindow();
                window.setBackgroundDrawableResource(R.drawable.bg_dialog);
                dialog.show();
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
                params.width = 1000;
                params.height = 1300 ;
                dialog.getWindow().setAttributes(params);
                dialog.getWindow().setContentView(layout);//自定义布局应该在这里添加，要在dialog.show()的后面
                dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
                btn_sure.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        title = hometitle1.getText().toString();
                        description = homedescription1.getText().toString();

                        //limitations situations
                        if (description.equals("") || title.equals("")){
                            Toast.makeText(getApplicationContext(), "You can not update!", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            ContentValues newValues = new ContentValues();
                            newValues.put(MyProviderContract.TITLE, title);
                            newValues.put(MyProviderContract.DESCRIPTION, description);

                            int count = getContentResolver().update(MyProviderContract.Homework_URI, newValues, MyProviderContract.Home_ID + "= ?", new String[]{homeID});    //update into the databases

                            if (count > 0)
                            {
                                Toast.makeText(HomeworkEdit.this, "Update Successful", Toast.LENGTH_LONG).show(); //if update sucessessfully
                            }

                            check();
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

    void check()
    {
        String[] projection = new String[] {
                MyProviderContract.Home_ID,
                MyProviderContract.TITLE,
                MyProviderContract.DESCRIPTION,
                MyProviderContract.REA
        };

        Cursor cursor = getContentResolver().query(MyProviderContract.Homework_URI, projection, MyProviderContract.Home_ID + "=" + "'" + homeID + "'" , null, null);  //search for the database to obtaion instruction

        while(cursor.moveToNext()){
            title = cursor.getString(cursor.getColumnIndex("title"));   //set the instruction
            description = cursor.getString(cursor.getColumnIndex("description"));   //set the instruction
            statu = cursor.getString(cursor.getColumnIndex("releases"));   //set the instruction releases
        }

        ViewTitle.setText(title);
        ViewDetail.setText(description);

        cursor.close();
    }

    public void deleteHome(View v){
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        int count = getContentResolver().delete(MyProviderContract.Homework_URI, MyProviderContract.Home_ID + "= ?", new String[]{homeID});
                        if (count > 0)  //if delete sucessessfully
                        {
                            sDialog.setTitleText("Deleted!")
                                    .setContentText("The homework has been deleted!")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                             Intent intent = new Intent(HomeworkEdit.this, BaseActivity.class);
                                             startActivity(intent);
                                        }
                                    })
                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        }
                    }
                })
                .show();
    }


    /* realse the homework */
    public void openhomework()
    {
        String[] projection = new String[]{
                MyProviderContract.Stu_ID,
                MyProviderContract.C_ID,
                MyProviderContract.sGROUP
        };

        Cursor cursor1 = getContentResolver().query(MyProviderContract.Student_URI, projection, MyProviderContract.C_ID + "=" + "'" + cid + "'"+ " "+ "or" + " " + MyProviderContract.sGROUP + "=" + "'" + homegroup + "'" , null, null);    //obtain context objection

        while(cursor1.moveToNext()) {

            String stuID = cursor1.getString(cursor1.getColumnIndex("Stu_ID"));   //set the instruction

            ContentValues newValues = new ContentValues();
            newValues.put(MyProviderContract.Stu_ID, stuID);
            newValues.put(MyProviderContract.Home_ID, homeID);
            newValues.put(MyProviderContract.STATU, "Not finished");

            getContentResolver().insert(MyProviderContract.Statu_URI, newValues);    //update into the databases
        }
    }

    /*jump to status page */
    public void checkStatu(View v){
        Intent intent = new Intent(HomeworkEdit.this, HomeworkStatu.class);
        Bundle bundle = new Bundle();
        bundle.putString("HID", homeID);
        bundle.putString("ID", cid);
        intent.putExtras(bundle);   //send ID and Title to another activity
        startActivity(intent);
    }
}
