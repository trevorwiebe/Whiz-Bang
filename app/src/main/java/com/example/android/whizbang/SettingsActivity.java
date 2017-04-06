package com.example.android.whizbang;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.android.whizbang.database.WhizBangContract;

public class SettingsActivity extends AppCompatActivity {

    EditText mEmailSubject;
    EditText mEmailBody;
    FloatingActionButton mFloatingActionButton;

    String finished_email_body;
    String finished_email_subject;

    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mEmailBody = (EditText) findViewById(R.id.email_body_edittext);
        mEmailSubject = (EditText) findViewById(R.id.email_subject_edittext);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.submit_edit_fab);

        String mSelection = null;

        String[] mSelectionArgs = new String[]{WhizBangContract.WhizBangEmail.EMAIL_BODY_COLUMN, WhizBangContract.WhizBangEmail.EMAIL_SUBJECT_COLUMN};

        Cursor databaseCursor;
        String[] projection = new String[]{WhizBangContract.WhizBangEmail.EMAIL_BODY_COLUMN};

        databaseCursor = getContentResolver().query(WhizBangContract.WhizBangEmail.CONTENT_URI_EMAIL, projection, mSelection, mSelectionArgs, null);

        if (databaseCursor.moveToFirst()) {
            while (!databaseCursor.isAfterLast()) {
                finished_email_body = databaseCursor.getString(databaseCursor.getColumnIndex(WhizBangContract.WhizBangEmail.EMAIL_BODY_COLUMN));
                finished_email_subject = databaseCursor.getString(databaseCursor.getColumnIndex(WhizBangContract.WhizBangEmail.EMAIL_SUBJECT_COLUMN));
            }
        }

        mEmailBody.setText(finished_email_body);
        mEmailSubject.setText(finished_email_subject);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: 4/3/2017 finished email editing arm

                String edited_email_body = mEmailBody.getText().toString();
                String edited_email_subject = mEmailSubject.getText().toString();

                ContentValues contentValues = new ContentValues();
                contentValues.put(WhizBangContract.WhizBangEmail.EMAIL_BODY_COLUMN, edited_email_body);

                getContentResolver().update(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY, contentValues, null, null);
            }
        });
    }
}
