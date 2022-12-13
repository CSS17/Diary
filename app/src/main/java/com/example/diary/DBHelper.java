package com.example.diary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper( Context context) {
        super(context,"Diary.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table DiaryInfo(title TEXT,body TEXT,rate INTEGER,date TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop table if exists DiaryInfo");
    }

    public Boolean InsertData(String title,String body,Integer rate,String date){
        SQLiteDatabase DB=this.getReadableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("title",title);
        contentValues.put("body",body);
        contentValues.put("rate",rate);
        contentValues.put("date",date);
        long result=DB.insert("DiaryInfo",null,contentValues);
        if(result==-1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase DB=this.getReadableDatabase();
        Cursor cursor=DB.rawQuery("Select *"+"from DiaryInfo",null);
        return cursor;
    }


}
