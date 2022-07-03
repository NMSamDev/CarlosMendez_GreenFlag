package com.example.carlosmendez_greenflag;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String USER = "USER";
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableStatement = "CREATE TABLE " + USER + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + EMAIL + " TEXT UNIQUE, " + PASSWORD + " TEXT)";
        sqLiteDatabase.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addUser(UserModel userModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(EMAIL, userModel.getEmail());
        cv.put(PASSWORD, userModel.getPassword());

        long insert = db.insert(USER, null, cv);
        if(insert == -1)
            return false;
        else
            return true;
    }
}
