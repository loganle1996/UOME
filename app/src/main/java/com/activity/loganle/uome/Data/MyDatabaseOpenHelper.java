package com.activity.loganle.uome.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by LoganLe on 11/5/16.
 */

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    private static String LOGTAG = "LOGTAG";
    //Database's name and version
    public static final int CONTACT_DB_VERSION = 1; // Everytime when changing the structure of the database, you should increment by 1
    public static final String DB_NAME = "Database.db";


    //Statements for creating, updating,deleting tables
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String INTEGER_TYPE =" INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String SQL_CREATE_CONTACT_ENTRIES =
    //1. Contact table
            "CREATE TABLE " + Contract.Contact.Contact_table_name + " (" +
                    Contract.Contact._ID + " INTEGER PRIMARY KEY," +
                    Contract.Contact.Contact_name + TEXT_TYPE + COMMA_SEP +
                    Contract.Contact.Contact_address+ TEXT_TYPE + COMMA_SEP +
                    Contract.Contact.Contact_phone + INTEGER_TYPE + COMMA_SEP+
                    Contract.Contact.Contact_email + TEXT_TYPE + COMMA_SEP +
                    Contract.Contact.Contact_owed_money + REAL_TYPE + COMMA_SEP +
                    Contract.Contact.Contact_Profile_image + TEXT_TYPE + COMMA_SEP+
                    Contract.Contact.Contact_deadline_date + TEXT_TYPE + " )";

            private static final String SQL_DELETE_CONTACT_ENTRY =
                    "DROP TABLE IF EXISTS " + Contract.Contact.Contact_table_name;

    //Constructor
    public MyDatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, CONTACT_DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONTACT_ENTRIES);
        Log.i(LOGTAG, "contact table has been created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CONTACT_ENTRY);
        onCreate(db);
    }
}
