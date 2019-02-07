package com.example.psyyf2.dissertation.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.psyyf2.dissertation.database.AssetsDatabaseManager;
import com.example.psyyf2.dissertation.database.SQLdm;
import com.example.psyyf2.dissertation.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private EditText username, userpassword;
    SQLiteDatabase db;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLdm s = new SQLdm();
        db =s.openDatabase(getApplicationContext());

        username = (EditText) findViewById(R.id.textView3);
        userpassword = (EditText) findViewById(R.id.editText4);
        CheckBox checkBox=(CheckBox) findViewById(R.id.checkBox);

        // show and hide the password
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    userpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    userpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    public void loginClicked(View view) {

        String userEmail=username.getText().toString();
        String passWord=userpassword.getText().toString();

        if (login(userEmail,passWord)) {

            Toast.makeText(MainActivity.this, "Log in successfully!", Toast.LENGTH_SHORT).show();

            SharedPreferences userSettings=getSharedPreferences("config",0);
            int verify = userSettings.getInt("Times",0);

            SharedPreferences.Editor editor=userSettings.edit();

            editor.putString("email",userEmail);
            editor.putString("name",name);
            editor.apply();

            //check the log in information befores
            if(verify == 1)
            {
                Intent intent = new Intent(MainActivity.this, BaseActivity.class);
                startActivity(intent);
            }
            else
            {
                editor.putInt("Times", 1);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, WelcomePage.class);
                startActivity(intent);
            }
        }
        else
        {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Something wrong with your account!")
                    .show();
        }
    }

    //Certificate the account
    public boolean login(String username,String password) {

        AssetsDatabaseManager.initManager(getApplication());
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();  //obtain the manager object
        SQLiteDatabase db = mg.getDatabase("dissertation.db");     //obtain the database

        String sql = "select * from teacher where tEmail=? and tPassword=?";
        Cursor cursor = db.rawQuery(sql, new String[] {username, password});

        if (cursor.moveToFirst()) {

            name = cursor.getString(cursor.getColumnIndex("tName"));   //set the instruction
            cursor.close();

            return true;
        }
        return false;
    }
}
