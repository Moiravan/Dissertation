package com.example.psyyf2.dissertation.activity;

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

import com.example.psyyf2.dissertation.R;
import com.example.psyyf2.dissertation.database.AssetsDatabaseManager;

public class AccountPage extends AppCompatActivity {

    SQLiteDatabase db;
    String email, username, phone;
    EditText name, telephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_page);

        //obtain the database
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        SQLiteDatabase db = mg.getDatabase("dissertation.db");

        TextView emails = (TextView) findViewById(R.id.setemail);
        name = (EditText) findViewById(R.id.setname);
        telephone = (EditText) findViewById(R.id.setphone);

        SharedPreferences userSettings= getSharedPreferences("config", 0);
        email = userSettings.getString("email","null");

        String sql = "select * from teacher where tEmail=?";
        Cursor cursor = db.rawQuery(sql, new String[] {email});

        while(cursor.moveToNext()){
            username = cursor.getString(cursor.getColumnIndex("tName"));   //set the instruction
            phone = cursor.getString(cursor.getColumnIndex("tPhone"));   //set the instruction

            name.setText(username);
            emails.setText(email);
            telephone.setText(phone);
        }
        cursor.close();
    }

    /* update the personal information */
    public void updatainfo(View v){

        ContentValues values = new ContentValues();

        username = name.getText().toString();
        phone = telephone.getText().toString();

        values.put("tName", username);
        values.put("tPhone", phone);

        db.update("teacher", values, "tEmail = ?", new String[] {email});

        Intent intent = new Intent(AccountPage.this, BaseActivity.class);
        startActivity(intent);
    }
}
