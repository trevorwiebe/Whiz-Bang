package com.example.android.whizbang.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by thisi on 3/30/2017.
 */

public class WhizBangDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "whizBang.db";

    public static final int DATABASE_VERSION = 5;


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
                + WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN + " TEXT, "
                + WhizBangContract.WhizBangEntry.PHONE_NUMBER + " TEXT);";

        Log.d(TAG, "onCreate: table declaration " + CREATE_WHIZBANG_TABLE);

        db.execSQL(CREATE_WHIZBANG_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WhizBangContract.WhizBangEntry.TABLE_NAME);
        onCreate(db);
    }
}
