package com.thomasdomingues.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movies.db";

    /*
     * If you change the database schema, you must increment the database version or the onUpgrade
     * method will not be called.
     */
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our movies data.
         */
        final String SQL_CREATE_MOVIES_TABLE =

                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +

                        MovieContract.MovieEntry._ID                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "    +

                        MovieContract.MovieEntry.COLUMN_TITLE           + " TEXT NOT NULL, "                        +

                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE    + " INTEGER NOT NULL, "                     +

                        MovieContract.MovieEntry.COLUMN_POSTER_PATH     + " TEXT NOT NULL, "                        +

                        MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE    + " REAL NOT NULL, "                        +

                        MovieContract.MovieEntry.COLUMN_SYNOPSIS        + " TEXT NOT NULL, "                        +

                        MovieContract.MovieEntry.COLUMN_FAVORITE        + " INTEGER NOT NULL"                       +

                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
