package com.example.donationapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataManager {

    private SQLiteDatabase db;

    public static final String TABLE_ROW_ID = "id";
    public static final String TABLE_ROW_NAME = "name";
    public static final String TABLE_ROW_AMOUNT = "amount";

    private static final String DB_NAME = "donator_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_DONATOR = "donator";

    public DataManager(Context context){
        CustomSQLiteOpenHelper helper = new CustomSQLiteOpenHelper(context);
        db = helper.getWritableDatabase();
    }

    //Insert record
    public void insert(String name, String amount){
        //Add all the details to the table
        Log.i("SQLQuery", "Insert function running");
        //INSERT INTO TABLE_DONATOR (TABLE_ROW_NAME, TABLE_ROW_AMOUNT) VALUES ('john', '1000');
        String query = "INSERT INTO " + TABLE_DONATOR
                + "(" + TABLE_ROW_NAME + "," + TABLE_ROW_AMOUNT + ") "
                + "VALUES (" + "'" + name + "'" + "," + "'" + amount + "'" + ");";

        Log.i("SQLQuery", "Insert Query: " + query);

        //Execute the query
        db.execSQL(query);
    }

    //Delete record
    public void delete(String name){
        //Delete the details from the table id already exists

        //DELETE FROM TABLE_DONATOR WHERE TABLE_ROW_NAME = 'name';
        String query = "DELETE FROM " + TABLE_DONATOR + " WHERE " + TABLE_ROW_NAME + " = '" + name + "';";

        Log.i("SQLQuery", "Delete Query: " + query);

        //Execute the query
        db.execSQL(query);
    }

    //Get all records
    public Cursor selectAll(){
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_DONATOR, null);
        return c;
    }

    //Get records by searching name
    public Cursor searchName(String name){

        //SELECT TABLE_ROW_ID, TABLE_ROW_NAME, TABLE_ROW_AMOUNT
        //FROM TABLE_DONATOR
        //WHERE TABLE_ROW_NAME = 'john';
        String query = "SELECT " + TABLE_ROW_ID + ", " + TABLE_ROW_NAME + ", " + TABLE_ROW_AMOUNT
                + " FROM " + TABLE_DONATOR
                + " WHERE " + TABLE_ROW_NAME + " = '" + name + "';";

        Log.i("SQLQuery", "Select name query: " + query);
        Cursor c = db.rawQuery(query, null);
        return c;
    }

    private class CustomSQLiteOpenHelper extends SQLiteOpenHelper {
        public CustomSQLiteOpenHelper(Context context){
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //CREATE TABLE TABLE_DONATOR (
            // TABLE_ROW_ID INT PRIMARY KEY AUTOINCREMENT NOT NULL,
            // TABLE_ROW_NAME TEXT NOT NULL,
            // TABLE_ROW_AMOUNT TEXT NOT NULL
            //);

            String newTableQueryString = "CREATE TABLE " + TABLE_DONATOR +
                    "("
                    + TABLE_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                    + TABLE_ROW_NAME + " TEXT NOT NULL, "
                    + TABLE_ROW_AMOUNT + " TEXT NOT NULL" +
                    ");";
            db.execSQL(newTableQueryString);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        }
    }
}
