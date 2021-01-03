package com.codingbhs.carbontracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    protected static final String DATABASE_NAME = "EntriesDatabase";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE entries " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT, " +
                "lbCO2 TEXT, " +
                "miles TEXT, " +
                "mpg TEXT, " +
                "radio TEXT ) ";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS entries";
        db.execSQL(sql);

        onCreate(db);
    }
}
