package com.example.appcontact.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.appcontact.Contact.Contact;

import java.util.ArrayList;
import java.util.List;

public class DatabaseReader extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ContactDB.db";
    public static final String TABLE_NAME = "tbl_Contact";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_XOA = "xoa";
    public static final String CREATE_TABLE_CONTACT = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME + " ( "
            + COLUMN_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
            + COLUMN_NAME   + " VARCHAR ,"
            + COLUMN_NUMBER  + " VARCHAR ,"
            + COLUMN_XOA    + " INTEGER )";


    public DatabaseReader(Context context) {
        super(context,DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_CONTACT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
