package com.example.android.whizbang.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thisi on 3/30/2017.
 */

public class WhizBangDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "whizBang.db";

    public static final int DATABASE_VERSION = 8;


    public WhizBangDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_WHIZBANG_TABLE = "CREATE TABLE "
                + WhizBangContract.WhizBangEntry.TABLE_NAME + " ("
                + WhizBangContract.WhizBangEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + WhizBangContract.WhizBangEntry.EMAIL_COLUMN + " TEXT NOT NULL, "
                + WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN + " TEXT NOT NULL, "
                + WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN + " TEXT NOT NULL, "
                + WhizBangContract.WhizBangEntry.PHONE_NUMBER + " TEXT NOT NULL);";

        final String CREATE_WHIZBANG_EMAIL_TABLE = "CREATE TABLE "
                + WhizBangContract.WhizBangEmail.TABLE_NAME + " ("
                + WhizBangContract.WhizBangEmail.EMAIL_BODY_COLUMN + " TEXT "
                + WhizBangContract.WhizBangEmail.EMAIL_SUBJECT_COLUMN + " TEXT);";


        db.execSQL(CREATE_WHIZBANG_TABLE);
        db.execSQL(CREATE_WHIZBANG_EMAIL_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WhizBangContract.WhizBangEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WhizBangContract.WhizBangEmail.TABLE_NAME);
        onCreate(db);
    }
}
