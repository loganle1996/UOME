package com.activity.loganle.uome.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.activity.loganle.uome.other.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LoganLe on 11/5/16.
 */

public class DataSource {
    public static final String imageFileName = "imageFileName";
    private static String LOGTAG = "LOGTAG";
    MyDatabaseOpenHelper myDatabaseOpenHelper; // This helps you get access to the database
    SQLiteDatabase sqLiteDatabase; // use this class to getWritableDatabase or getReadbleDatabase objects


    private String[] allColumns = {
            Contract.Contact._ID,
            Contract.Contact.Contact_name,
            Contract.Contact.Contact_address,
            Contract.Contact.Contact_phone,
            Contract.Contact.Contact_email,
            Contract.Contact.Contact_owed_money,
            Contract.Contact.Contact_Profile_image,
            Contract.Contact.Contact_deadline_date
    };

    //Constructor
    public DataSource(Context context) {
        myDatabaseOpenHelper = new MyDatabaseOpenHelper(context);
    }

    //Create a connection to database which will be used for both READING AND WRITING
    public void open() {
        sqLiteDatabase = myDatabaseOpenHelper.getWritableDatabase();
        Log.i(LOGTAG, "DATASOURCE has been opened");
    }

    public void close() {
        myDatabaseOpenHelper.close();
        Log.i(LOGTAG, "DATASOURCE has been closed");
    }

    public Contact createContact(Contact contact) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Contact.Contact_name, contact.getName());
        contentValues.put(Contract.Contact.Contact_address, contact.getAddress());
        contentValues.put(Contract.Contact.Contact_phone, contact.getPhone());
        contentValues.put(Contract.Contact.Contact_email, contact.getEmail());
        contentValues.put(Contract.Contact.Contact_owed_money, contact.getOwedMoney());
        contentValues.put(Contract.Contact.Contact_Profile_image, contact.getImagePath());
        contentValues.put(Contract.Contact.Contact_deadline_date, contact.getDeadlineDate());
        // Pass the value content to insert method
        long rowId = sqLiteDatabase.insert(Contract.Contact.Contact_table_name, null, contentValues);
        contact.setIdContact(rowId);
        return contact;
    }

    public void updateContactImage(long id, String imagePath) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.Contact.Contact_Profile_image, imagePath);
        String selection = Contract.Contact._ID + " == ?";
        String[] selectionArgs = {String.valueOf(id)};
        sqLiteDatabase.update(Contract.Contact.Contact_table_name,
                contentValues,
                selection,
                selectionArgs);
    }

    public void deleteContact(Contact contact){
        String selection = Contract.Contact._ID + " == ?" ;
        String [] selectionArgs = {String.valueOf(contact.getIdContact())};
        sqLiteDatabase.delete(Contract.Contact.Contact_table_name,selection,selectionArgs);
    }

    public List<Contact> getAllContactsFromDatabase() {
        List<Contact> contacts = new ArrayList<Contact>();
        sqLiteDatabase = myDatabaseOpenHelper.getReadableDatabase(); // This is only needed when there is some problem like full disk, which requires read-only mode.
        Cursor cursor = sqLiteDatabase.query(
                Contract.Contact.Contact_table_name, // the table to query
                allColumns,                       // columns to return (Do projection)
                null,                       // columns for where clause (Do selection)
                null,                       // the values for where clause
                null,                       // dont group the rows
                null,                       // don't filter by row groups
                null                        // don't sort the order
        );
        if (cursor.getCount() > 0) { // if the number of items from database > 0
            while (cursor.moveToNext()) {
                Contact contact = new Contact();
                contact.setIdContact(cursor.getLong(cursor.getColumnIndex(Contract.Contact._ID)));
                contact.setName(cursor.getString(cursor.getColumnIndex(Contract.Contact.Contact_name)));
                contact.setAddress(cursor.getString(cursor.getColumnIndex(Contract.Contact.Contact_address)));
                contact.setPhone(cursor.getString(cursor.getColumnIndex(Contract.Contact.Contact_phone)));
                contact.setEmail(cursor.getString(cursor.getColumnIndex(Contract.Contact.Contact_email)));
                contact.setOwedMoney(cursor.getDouble(cursor.getColumnIndex(Contract.Contact.Contact_owed_money)));
                contact.setPersonalpic(cursor.getString(cursor.getColumnIndex(Contract.Contact.Contact_Profile_image)));
                contact.setDeadlineDate(cursor.getString(cursor.getColumnIndex(Contract.Contact.Contact_deadline_date)));
                contacts.add(contact);
            }
        }
        return contacts;
    }

}
