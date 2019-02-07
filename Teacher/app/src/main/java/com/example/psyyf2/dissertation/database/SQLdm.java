package com.example.psyyf2.dissertation.database;

/**
 * Created by moiravan on 2018/4/11.
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SQLdm {


    String filePath = "data/data/com.example.psyyf2.dissertation/databases/dissertation" + ".db";
    String pathStr = "data/data/com.example.psyyf2.dissertation/databases";

    SQLiteDatabase database;
    public  SQLiteDatabase openDatabase(Context context){
        System.out.println("filePath:"+filePath);
        File jhPath=new File(filePath);

        //verify if the file is exist
        if(jhPath.exists())
        {
            //open the database if exit
            return SQLiteDatabase.openOrCreateDatabase(jhPath, null);
        }else
        {
            //create the file
            File path=new File(pathStr);
            if (path.mkdir()){
            }
            try {
                //obtain the resources
                AssetManager am= context.getAssets();

                InputStream is=am.open("dissertation.db");
                //output flow into SD card
                FileOutputStream fos=new FileOutputStream(jhPath);

                byte[] buffer=new byte[1024];
                int count = 0;
                while((count = is.read(buffer))>0){
                    fos.write(buffer,0,count);
                }

                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
            //return the database if not exist
            return openDatabase(context);
        }
    }
}
