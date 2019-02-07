package com.example.psyyf2.dissertation;

/**
 * Created by moiravan on 2018/2/5.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.psyyf2.dissertation.database.AssetsDatabaseManager;
import com.example.psyyf2.dissertation.database.MyProviderContract;

/**
 * Created by psyyf2 on 02/12/2017.
 */

public class MyProvider extends ContentProvider {

    private static final UriMatcher uriMatcher;
    private static final int CLASS_LIST = 0;
    private static final int CLASS_ITEM= 1;
    private static final int HOMEWORK_LIST= 2;
    private static final int HOMEWORK_ITEM= 3;
    private static final int STUDENT_LIST= 4;
    private static final int EXAM_LIST= 5;
    private static final int GRADE_LIST= 6;
    private static final int GROUP_LIST= 7;
    private static final int STATU_LIST= 8;
    private static final int TEACHER_LIST= 9;

    //definition for the match contentprovider URI
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "class", CLASS_LIST);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "class/#", CLASS_ITEM);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "homework", HOMEWORK_LIST);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "homework/#", HOMEWORK_ITEM);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "student", STUDENT_LIST);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "exam", EXAM_LIST);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "grade", GRADE_LIST);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "groups", GROUP_LIST);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "homeworkstatu", STATU_LIST);
        uriMatcher.addURI(MyProviderContract.AUTHORITY, "teacher", TEACHER_LIST);

    }

    @Override
    public boolean onCreate() {

        return true;
    }

    @Override
    /*query the data*/
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        AssetsDatabaseManager.initManager(this.getContext());
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        SQLiteDatabase db = mg.getDatabase("dissertation.db");

        switch(uriMatcher.match(uri))
        {
            case TEACHER_LIST:
                return db.query("teacher", projection, selection, selectionArgs, null, null, sortOrder);
            case CLASS_LIST:
                return db.query("class", projection, selection, selectionArgs, null, null, sortOrder);
            case HOMEWORK_LIST:
                return db.query("homework", projection, selection, selectionArgs, null, null, sortOrder);
            case STUDENT_LIST:
                return db.query("student", projection, selection, selectionArgs, null, null, sortOrder);
            case EXAM_LIST:
                return db.query("exam", projection, selection, selectionArgs, null, null, sortOrder);
            case GRADE_LIST:
                return db.query("grade", projection, selection, selectionArgs, null, null, sortOrder);
            case GROUP_LIST:
                return db.query("groups", projection, selection, selectionArgs, null, null, sortOrder);
            case STATU_LIST:
                return db.query("homeworkstatu", projection, selection, selectionArgs, null, null, sortOrder);
            default:
                return null;
        }
    }

    @Override
    /* match with the registed URI */
    public String getType(Uri uri) {
        String contentType;

        if (uri.getLastPathSegment()==null)
        {
            contentType = MyProviderContract.CONTENT_TYPE_MULTIPLE;
        }
        else
        {
            contentType = MyProviderContract.CONTENT_TYPE_SINGLE;
        }
        return contentType;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        SQLiteDatabase db = mg.getDatabase("dissertation.db");

        long id = 0;
        Uri uris;
        switch(uriMatcher.match(uri))
        {
            case HOMEWORK_LIST:
                id = db.insert("homework", null, contentValues);
                break;
            case EXAM_LIST:
                id = db.insert("exam", null, contentValues);
                break;
            case GRADE_LIST:
                id = db.insert("grade", null, contentValues);
                break;
            case GROUP_LIST:
                id = db.insert("groups", null, contentValues);
                break;
            case STATU_LIST:
                id = db.insert("homeworkstatu", null, contentValues);
                break;
        }

        uris = ContentUris.withAppendedId(uri, id);
        getContext().getContentResolver().notifyChange(uris, null); //send the notification to observer the data has chang

        return uris;
    }

    @Override
    public int delete(Uri uri, String situation, String[] strings) {

        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        SQLiteDatabase db = mg.getDatabase("dissertation.db");

        int count = 0;
        switch(uriMatcher.match(uri))
        {
            case HOMEWORK_LIST:
                count = db.delete("homework", situation, strings);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count; //can check whether delete successfully
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String situation, String[] strings) {

        AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();
        SQLiteDatabase db = mg.getDatabase("dissertation.db");

        int count = 0;
        switch(uriMatcher.match(uri))
        {
            case HOMEWORK_LIST:
                count = db.update("homework", contentValues, situation, strings);
                break;
            case STUDENT_LIST:
                count = db.update("student", contentValues, situation, strings);
                break;
            case STATU_LIST:
                count = db.update("homeworkstatu", contentValues, situation, strings);
                break;
            case EXAM_LIST:
                count = db.update("exam", contentValues, situation, strings);
                break;

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count; //can check whether update successfully
    }

}
