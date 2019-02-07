package com.example.psyyf2.parent.activity;

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

import com.example.psyyf2.parent.Adapter.DBHelper;
import com.example.psyyf2.parent.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private EditText username;
    private EditText userpassword;
    private CheckBox checkBox = null;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this,"dissertation.db",null,7);

        username = (EditText) findViewById(R.id.textView3);
        userpassword = (EditText) findViewById(R.id.editText4);
        checkBox=(CheckBox) findViewById(R.id.checkBox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    //如果选中，显示密码
                    userpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
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

            SharedPreferences userSettings=getSharedPreferences("configParent",0);
            int verify = userSettings.getInt("Times",0);

            SharedPreferences.Editor editor=userSettings.edit();

            editor.putString("email",userEmail);
            editor.putString("name",name);
            editor.apply();

            //check the log in information before
            if(verify == 1)
            {
                Intent intent = new Intent(MainActivity.this, BaseActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                intent.putExtras(bundle);   //send ID and Title to another activity
                startActivity(intent);
            }
            else
            {
                editor.putInt("Times", 1);
                editor.commit();

                Intent intent = new Intent(MainActivity.this, WelcomePage.class);
                Bundle bundle = new Bundle();
                bundle.putString("Name", name);
                intent.putExtras(bundle);   //send ID and Title to another activity
                startActivity(intent);
            }
        }
        else {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Something went wrong!")
                    .show();
        }
    }

    //verify
    public boolean login(String username,String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String sql = "select * from parent where pEmail=? and pPassword=?";
        Cursor cursor = db.rawQuery(sql, new String[] {username, password});
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex("pName"));   //set the instruction

            cursor.close();
            return true;
        }
        return false;
    }
}
