package com.example.psyyf2.parent.Adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by moiravan on 2018/2/4.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    @Override
    /*create database*/
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE parent (" +
                "Par_ID INTEGER PRIMARY KEY AUTOINCREMENT," + "pEmail VARCHAR (128) NOT NULL," +
                "pName VARCHAR(128) NOT NULL," + "Stu_ID VARCHAR(128) NOT NULL," +
                "pPassword VARCHAR(128) NOT NULL," +"FOREIGN KEY(Stu_ID) REFERENCES student(Stu_ID)" +
                ");");

        db.execSQL("INSERT INTO parent (pEmail, pName, Stu_ID, pPassword) VALUES ('parent1@nottingham.ac.uk','parent1', 1, 'PARENTpassword1');"); //Insert the initial instruction
        db.execSQL("INSERT INTO parent (pEmail, pName, Stu_ID, pPassword) VALUES ('parent2@nottingham.ac.uk','parent2', 2, 'PARENTpassword2');"); //Insert the initial instruction
        db.execSQL("INSERT INTO parent (pEmail, pName, Stu_ID, pPassword) VALUES ('parent3@nottingham.ac.uk', 'parent3',3, 'PARENTpassword3');"); //Insert the initial instruction
        db.execSQL("INSERT INTO parent (pEmail, pName, Stu_ID, pPassword) VALUES ('parent4@nottingham.ac.uk', 'parent4',4, 'PARENTpassword4');"); //Insert the initial instruction
        db.execSQL("INSERT INTO parent (pEmail, pName, Stu_ID, pPassword) VALUES ('parent5@nottingham.ac.uk', 'parent5',5, 'PARENTpassword5');"); //Insert the initial instruction
        db.execSQL("INSERT INTO parent (pEmail, pName, Stu_ID, pPassword) VALUES ('parent6@nottingham.ac.uk', 'parent6',6, 'PARENTpassword6');"); //Insert the initial instruction
    }

    @Override
    /*upgrade the database*/
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS parent");
        onCreate(db);
    }
}