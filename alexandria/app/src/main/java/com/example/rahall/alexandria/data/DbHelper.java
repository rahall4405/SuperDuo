package com.example.rahall.alexandria.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



/**
 * Created by saj on 22/12/14.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "alexandria.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_BOOK_TABLE = "CREATE TABLE " + com.example.rahall.alexandria.data.AlexandriaContract.BookEntry.TABLE_NAME + " ("+
                com.example.rahall.alexandria.data.AlexandriaContract.BookEntry._ID + " INTEGER PRIMARY KEY," +
                com.example.rahall.alexandria.data.AlexandriaContract.BookEntry.TITLE + " TEXT NOT NULL," +
                com.example.rahall.alexandria.data.AlexandriaContract.BookEntry.SUBTITLE + " TEXT ," +
                com.example.rahall.alexandria.data.AlexandriaContract.BookEntry.DESC + " TEXT ," +
                com.example.rahall.alexandria.data.AlexandriaContract.BookEntry.IMAGE_URL + " TEXT, " +
                "UNIQUE ("+ com.example.rahall.alexandria.data.AlexandriaContract.BookEntry._ID +") ON CONFLICT IGNORE)";

        final String SQL_CREATE_AUTHOR_TABLE = "CREATE TABLE " + com.example.rahall.alexandria.data.AlexandriaContract.AuthorEntry.TABLE_NAME + " ("+
                com.example.rahall.alexandria.data.AlexandriaContract.AuthorEntry._ID + " INTEGER," +
                com.example.rahall.alexandria.data.AlexandriaContract.AuthorEntry.AUTHOR + " TEXT," +
                " FOREIGN KEY (" + com.example.rahall.alexandria.data.AlexandriaContract.AuthorEntry._ID + ") REFERENCES " +
                com.example.rahall.alexandria.data.AlexandriaContract.BookEntry.TABLE_NAME + " (" + com.example.rahall.alexandria.data.AlexandriaContract.BookEntry._ID + "))";

        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + com.example.rahall.alexandria.data.AlexandriaContract.CategoryEntry.TABLE_NAME + " ("+
                com.example.rahall.alexandria.data.AlexandriaContract.CategoryEntry._ID + " INTEGER," +
                com.example.rahall.alexandria.data.AlexandriaContract.CategoryEntry.CATEGORY + " TEXT," +
                " FOREIGN KEY (" + com.example.rahall.alexandria.data.AlexandriaContract.CategoryEntry._ID + ") REFERENCES " +
                com.example.rahall.alexandria.data.AlexandriaContract.BookEntry.TABLE_NAME + " (" + AlexandriaContract.BookEntry._ID + "))";


        Log.d("sql-statments",SQL_CREATE_BOOK_TABLE);
        Log.d("sql-statments",SQL_CREATE_AUTHOR_TABLE);
        Log.d("sql-statments",SQL_CREATE_CATEGORY_TABLE);

        db.execSQL(SQL_CREATE_BOOK_TABLE);
        db.execSQL(SQL_CREATE_AUTHOR_TABLE);
        db.execSQL(SQL_CREATE_CATEGORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
