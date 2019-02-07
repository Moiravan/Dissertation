package com.example.psyyf2.parent.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.psyyf2.parent.Adapter.DBHelper;
import com.example.psyyf2.parent.R;

public class Setting extends AppCompatActivity {

    SQLiteDatabase db;
    String email, username, Stu_ID;
    EditText name, telephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        DBHelper dbHelper = new DBHelper(this,"dissertation.db",null,7);
        db = dbHelper.getWritableDatabase();

        name = (EditText) findViewById(R.id.setname);
        TextView emails = (TextView) findViewById(R.id.setemail);

        SharedPreferences userSettings= getSharedPreferences("configParent", 0);
        email = userSettings.getString("email","null");

        String sql = "select * from parent where pEmail=?";
        Cursor cursor = db.rawQuery(sql, new String[] {email});

        while(cursor.moveToNext()){
            username = cursor.getString(cursor.getColumnIndex("pName"));   //set the instruction
            name.setText(username);
            emails.setText(email);
        }

        cursor.close();
    }

    public void updatainfo(View v){

        ContentValues values = new ContentValues();

        username = name.getText().toString();
        values.put("pName", username);

        db.update("parent", values, "pEmail = ?", new String[] {email});

        Intent intent = new Intent(Setting.this, BaseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Name", username);
        intent.putExtras(bundle);   //send
        startActivity(intent);
    }
}
