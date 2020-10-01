package com.example.sqlite_test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "customer.db", null, 1);
    }

    // this is called the first time a database is accessed. There should be code in here to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE "+ CUSTOMER_TABLE +" ("+ COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ COLUMN_CUSTOMER_NAME +" TEXT, "+ COLUMN_CUSTOMER_AGE +" INT, "+ COLUMN_ACTIVE_CUSTOMER +" BOOL)";
        db.execSQL(createTableStatement);

    }
    // this is called if the database version number changes. It prevent previous users apps from breaking when you change the database design
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addCustomer(CustomerModel customerModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CUSTOMER_NAME, customerModel.getName());
        cv.put(COLUMN_CUSTOMER_AGE, customerModel.getAge());
        cv.put(COLUMN_ACTIVE_CUSTOMER, customerModel.isActive());

        long insert = db.insert(CUSTOMER_TABLE, null, cv);

        if (insert == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteCustomer(CustomerModel customerModel){
        //find customerModel in the database. if it found, delete it and return true.
        //if it is not found, return false

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM "+ CUSTOMER_TABLE + " WHERE " + COLUMN_ID + " = " + customerModel.getId();
        System.out.println(queryString);
        Cursor cursor = db.rawQuery(queryString, null);
        if(cursor.moveToFirst()){
            return true;
        }
        else {
            return false;
        }
    }

    public List<CustomerModel> getAll(){

        List<CustomerModel> resultList = new ArrayList<>();
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst()){
            //loop through the cursor (result set) and create new customer objects. put them into the return list
            do {
                int customerID = cursor.getInt(0);
                String customerName = cursor.getString(1);
                int customerAge = cursor.getInt(2);
                boolean customerActive = cursor.getInt(3) == 1? true: false;

                CustomerModel newCustomer = new CustomerModel(customerID, customerName, customerAge, customerActive);
                resultList.add(newCustomer);
            }while (cursor.moveToNext());
        }
        else {
            // failer. do not add anything to the list
        }

        //close both the cursor and db when the list is done
        cursor.close();
        db.close();
        return resultList;

    }
}
