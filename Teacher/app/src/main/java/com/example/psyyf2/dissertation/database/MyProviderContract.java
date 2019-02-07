package com.example.psyyf2.dissertation.database;

/**
 * Created by moiravan on 2018/2/5.
 */

import android.net.Uri;

public class MyProviderContract {

    public static  final String AUTHORITY = "com.example.psyyf2.dissertation.MyProvider";

    public static final Uri Class_URI = Uri.parse("content://"+AUTHORITY+"/class");
    public static final Uri Homework_URI = Uri.parse("content://"+AUTHORITY+"/homework");
    public static final Uri Student_URI = Uri.parse("content://"+AUTHORITY+"/student");
    public static final Uri Exam_URI = Uri.parse("content://"+AUTHORITY+"/exam");
    public static final Uri Grade_URI = Uri.parse("content://"+AUTHORITY+"/grade");
    public static final Uri Group_URI = Uri.parse("content://"+AUTHORITY+"/groups");
    public static final Uri Statu_URI = Uri.parse("content://"+AUTHORITY+"/homeworkstatu");
    public static final Uri Teacher_URI = Uri.parse("content://"+AUTHORITY+"/teacher");



    public static final String C_ID = "_id";
    public static final String cNAME = "classname";

    public static final String Home_ID = "Home_ID";
    public static final String Stu_ID = "Stu_ID";
    public static final String Exam_ID = "Exam_ID";
    public static final String tNAME = "tName";
    public static final String tSubject= "tSubject";
    public static final String tPhone= "tPhone";

    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String HDate = "hDate";
    public static final String REA = "releases";

    public static final String sNAME = "sName";
    public static final String sGROUP = "sGroup";
    public static final String hGROUP = "hGroup";
    public static final String sTELEPHONE = "sTelephone";
    public static final String Subject = "subject";
    public static final String ExamName = "examName";

    public static final String ExamDate = "mDate";
    public static final String GRADE = "grade";

    public static final String Group_ID = "Group_ID";
    public static final String GROUPINFO = "groupinfo";
    public static final String STATU = "statu";
    public static final String AVEGRADE = "aveGrade";


    public static final String CONTENT_TYPE_SINGLE = "vnd.android.cursor.item/MyProvider.data.text";
    public static final String CONTENT_TYPE_MULTIPLE = "vnd.android.cursor.dir/MyProvider.data.text";
}
