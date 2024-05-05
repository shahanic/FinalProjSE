package com.example.finalprojectse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context) {
        super(context, "user_database", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qry1 = "create table users(username text,email text,password text)";
        sqLiteDatabase.execSQL(qry1);

        String qry2 = "create table cart(username text,product text,price float,otype text)";
        sqLiteDatabase.execSQL(qry2);

        String qry3 = "create table orderplace(username text,fullname text,address text,contactno text,pincode int,date text,time text,amount float,otype text)";
        sqLiteDatabase.execSQL(qry3);

        // Create profile table
        String qry4 = "create table profile(username text,fullname text,address text,marital_status text,age int,birthday text,allergies text,image blob)";
        sqLiteDatabase.execSQL(qry4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop tables if they exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS cart");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS orderplace");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS profile");
        onCreate(sqLiteDatabase);
    }

    public void register(String username, String email, String password) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("email", email);
        cv.put("password", password);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("users", null, cv);
        db.close();
    }

    public int login(String username, String password) {
        int result = 0;
        String str[] = new String[2];
        str[0] = username;
        str[1] = password;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from users where username=? and password=?", str);
        if (c.moveToFirst()) {
            result = 1;
        }
        c.close();
        db.close();
        return result;
    }

    public void addUserProfile(String username, String fullname, String address, String maritalStatus,
                               int age, String birthday, String allergies, byte[] image) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("fullname", fullname);
        cv.put("address", address);
        cv.put("marital_status", maritalStatus);
        cv.put("age", age);
        cv.put("birthday", birthday);
        cv.put("allergies", allergies);
        cv.put("image", image);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("profile", null, cv);
        db.close();
    }

    public Profile getUserProfile(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM profile WHERE username=?", new String[]{username});
        if (cursor != null && cursor.moveToFirst()) {
            String fullname = cursor.getString(cursor.getColumnIndex("fullname"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String maritalStatus = cursor.getString(cursor.getColumnIndex("marital_status"));
            int age = cursor.getInt(cursor.getColumnIndex("age"));
            String birthday = cursor.getString(cursor.getColumnIndex("birthday"));
            String allergies = cursor.getString(cursor.getColumnIndex("allergies"));
            byte[] image = null;
            int imageIndex = cursor.getColumnIndex("image");
            if (imageIndex >= 0) {
                image = cursor.getBlob(imageIndex);
            } else {
                // Handle the case where the column index is not found
                // For example, you can log an error message or take appropriate action
                Log.e("Database", "Column 'image' not found in the profile table");
            }
        }

        return null;
    }
    public boolean updateUserProfile(String oldUsername, String newUsername, String newEmail, String newAddress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", newUsername);
        values.put("email", newEmail);
        values.put("address", newAddress);
        // Update other profile attributes as needed

        // Updating row
        int rowsAffected = db.update("profile", values, "username = ?", new String[]{oldUsername});
        db.close();

        // Return true if the update was successful (at least one row affected)
        return rowsAffected > 0;
    }
}
