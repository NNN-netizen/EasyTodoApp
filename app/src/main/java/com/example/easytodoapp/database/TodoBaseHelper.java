package com.example.easytodoapp.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AL_META on 12/07/2015.
 */

import com.example.easytodoapp.database.TodoDbSchema.TodoTable;

public class TodoBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "todoBase.db";

    public TodoBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TodoTable.CREATE_TABLE);
        db.execSQL(TodoTable.ALTER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                db.execSQL(TodoTable.ALTER_TABLE);
            default:
        }
    }
}
