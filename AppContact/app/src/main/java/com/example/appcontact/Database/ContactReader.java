package com.example.appcontact.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appcontact.Contact.Contact;

import java.util.ArrayList;
import java.util.List;

import static com.example.appcontact.Database.DatabaseReader.COLUMN_ID;
import static com.example.appcontact.Database.DatabaseReader.COLUMN_NAME;
import static com.example.appcontact.Database.DatabaseReader.COLUMN_NUMBER;
import static com.example.appcontact.Database.DatabaseReader.COLUMN_XOA;
import static com.example.appcontact.Database.DatabaseReader.TABLE_NAME;

public class ContactReader  {

    private Context context;
    private DatabaseReader dbReader;
    private  SQLiteDatabase db;

    private List<Contact> contactList;
    public static final String SELECT_ALL_CONTACT ="SELECT * FROM "+ TABLE_NAME +" WHERE "+COLUMN_XOA  + " = 0";

    public ContactReader(Context context) {
        this.context = context;
    }
    public void CreateDB(){
        dbReader = new DatabaseReader(context);
    }


    public List<Contact> GetAllContact(){
        db = dbReader.getReadableDatabase();
        contactList = new ArrayList<>();
        Cursor cursor = db.rawQuery(SELECT_ALL_CONTACT,null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                contactList.add(new Contact(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER))));
                cursor.moveToNext();
            }
        }
        db.close();
        return contactList;
    }

    public long InsertContact(Contact contact){
       db = dbReader.getWritableDatabase();
       ContentValues values = new ContentValues();
       values.put(COLUMN_NAME,contact.getName());
       values.put(COLUMN_NUMBER,contact.getNumber());
       values.put(COLUMN_XOA,0);
       long result = db.insert(TABLE_NAME,null,values);
       db.close();
       return result;
    }
    public long UpdateContact(int id ,Contact contact ){
       db = dbReader.getWritableDatabase();
       ContentValues values = new ContentValues();
       values.put(COLUMN_NAME,contact.getName());
       values.put(COLUMN_NUMBER,contact.getNumber());
       long result = db.update(TABLE_NAME,values,COLUMN_ID + " = ?" , new String[]{String.valueOf(id)});
       db.close();
       return result;
    }

    public long DeleteContact(int id){
       db = dbReader.getWritableDatabase();
       ContentValues values = new ContentValues();
       values.put(COLUMN_XOA , 1);
       long result = db.update(TABLE_NAME,values,COLUMN_ID + " = ?" , new String[]{String.valueOf(id)});
       db.close();
       return result;
    }

    public int SelectIDContact(String name,String number){
        db = dbReader.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ COLUMN_ID +" FROM "+TABLE_NAME +" WHERE "+ COLUMN_NAME + " = '"+ name +"' AND " + COLUMN_NUMBER + " = '"+number+ "' ",null);
        int id = 0 ;
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            cursor.close();
        }
        db.close();
        return id;
    }

}
