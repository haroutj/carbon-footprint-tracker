package com.codingbhs.carbontracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TableControllerEntries extends DatabaseHandler {

    public TableControllerEntries(Context context) {
        super(context);
    }

    public boolean create(EntryClass obj) {

        ContentValues values = new ContentValues();

        values.put("date", obj.date);
        values.put("lbCO2", obj.lbCO2);
        values.put("miles", obj.miles);
        values.put("mpg", obj.mpg);
        values.put("radio", obj.radio);

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert("entries", null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public List<EntryClass> read() {

        List<EntryClass> recordsList = new ArrayList<EntryClass>();

        String sql = "SELECT * FROM entries ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
                String entryDate = cursor.getString(cursor.getColumnIndex("date"));
                double entryLbCO2 = Double.parseDouble(cursor.getString(cursor.getColumnIndex("lbCO2")));
                double entryMiles = Double.parseDouble(cursor.getString(cursor.getColumnIndex("miles")));
                double entryMpg = Double.parseDouble(cursor.getString(cursor.getColumnIndex("mpg")));
                String entryRadio = cursor.getString(cursor.getColumnIndex("radio"));

                EntryClass obj = new EntryClass();
                obj.id = id;
                obj.date = entryDate;
                obj.lbCO2 = entryLbCO2;
                obj.miles = entryMiles;
                obj.mpg = entryMpg;
                obj.radio = entryRadio;

                recordsList.add(obj);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return recordsList;
    }

    public int count() {

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM entries";
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();

        return recordCount;
    }

    public boolean delete(int id) {

        boolean deleteSuccessful = false;

        SQLiteDatabase db = this.getWritableDatabase();
        deleteSuccessful = db.delete("entries", "id ='" + id + "'", null) > 0;
        db.close();

        return deleteSuccessful;

    }

    public int countDays() {
        int days = 1;

        List<EntryClass> entriesList = read();

        if (entriesList.size() > 0)
        for (int i = 1; i < entriesList.size(); i++) {
            if (!entriesList.get(i).date.equals(entriesList.get(i-1).date)) {
                days++;
            }
        }

        return days;
    }

    public double average() {
        double sum = 0;

        String sql = "SELECT * FROM entries ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                sum += Double.parseDouble(cursor.getString(cursor.getColumnIndex("lbCO2")));
            } while (cursor.moveToNext());
        }
        else {
            return 99;
        }

        cursor.close();
        db.close();

        return sum / countDays();
    }
}