
package com.example.amit.dictionary.data;

/**
 * Created by AMIT YADAV
 * 10/10/18
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.amit.dictionary.data.DictContract.DictEntry;

public class DictDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dictionary.db";
    private static final int DATABASE_VERSION = 1;

    private String SQL_CREATE_HISTORY_TABLE = "CREATE TABLE "
            + DictEntry.HISTORY_TABLE_NAME
            + " ("
            + DictEntry._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , "
            + DictEntry.WORD + " TEXT NOT NULL UNIQUE ON CONFLICT IGNORE "
            + " );";

    private String SQL_CREATE_FAVOURITE_TABLE = "CREATE TABLE "
            + DictEntry.FAVOURITE_TABLE_NAME
            + " ("
            + DictEntry._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + DictEntry.WORD_ID + " TEXT NOT NULL UNIQUE ON CONFLICT IGNORE, "
            + DictEntry.WORD + " TEXT NOT NULL, "
            + DictEntry.LANGUAGE + " TEXT, "
            + DictEntry.SINGLE_DEFINITION + " TEXT, "
            + DictEntry.PRONNUNCIATION_URL + " TEXT, "
            + DictEntry.PRONNUNCIATION_TEXT + " TEXT, "
            + DictEntry.LEXICAL_ENTRIES + " TEXT"
            + " );";

    private String SQL_CREATE_DEFINITION_TABLE = "CREATE TABLE "
            + DictEntry.DEFINITION_TABLE_NAME
            + " ("
            + DictEntry._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + DictEntry.WORD_ID + " TEXT NOT NULL UNIQUE ON CONFLICT IGNORE,"
            + DictEntry.WORD + " TEXT NOT NULL, "
            + DictEntry.LANGUAGE + " TEXT, "
            + DictEntry.SINGLE_DEFINITION + " TEXT, "
            + DictEntry.PRONNUNCIATION_URL + " TEXT, "
            + DictEntry.PRONNUNCIATION_TEXT + " TEXT, "
            + DictEntry.LEXICAL_ENTRIES + " TEXT"
            + " );";

    private String SQL_CREATE_SEARCH_TABLE = "CREATE TABLE "
            + DictEntry.SEARCH_TABLE_NAME + " ("
            + DictEntry._ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + DictEntry.WORD_ID + " TEXT NOT NULL UNIQUE ON CONFLICT IGNORE,"
            + DictEntry.WORD + " TEXT "
            + " );";

    DictDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        sqLiteDatabase.execSQL(SQL_CREATE_HISTORY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SEARCH_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DEFINITION_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DictEntry.FAVOURITE_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DictEntry.SEARCH_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DictEntry.DEFINITION_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DictEntry.HISTORY_TABLE_NAME);

        sqLiteDatabase.execSQL(SQL_CREATE_HISTORY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SEARCH_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DEFINITION_TABLE);


    }
}
