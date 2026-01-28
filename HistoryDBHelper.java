package com.jg.scientificcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class HistoryDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "calculator_history.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "history";
    private static final String COL_ID = "id";
    private static final String COL_EXPR = "expression";
    private static final String COL_RESULT = "result";

    public HistoryDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EXPR + " TEXT, " +
                COL_RESULT + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertHistory(String expr, String result) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_EXPR, expr);
        cv.put(COL_RESULT, result);
        db.insert(TABLE_NAME, null, cv);
    }

    public ArrayList<HistoryItem> getAllHistory() {
        ArrayList<HistoryItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_ID + " DESC",
                null
        );

        while (cursor.moveToNext()) {
            String expr = cursor.getString(cursor.getColumnIndexOrThrow(COL_EXPR));
            String res = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESULT));
            list.add(new HistoryItem(expr, res));
        }

        cursor.close();
        return list;
    }

    public void clearHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}
