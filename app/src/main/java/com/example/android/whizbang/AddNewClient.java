package com.example.android.whizbang;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.android.whizbang.database.WhizBangContract;

public class AddNewClient extends AppCompatActivity {

    EditText mFirstName;
    EditText mLastName;
    EditText mEmailAddress;
    FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_client);

        mFirstName = (EditText) findViewById(R.id.first_name);
        mLastName = (EditText) findViewById(R.id.last_name);
        mEmailAddress = (EditText)findViewById(R.id.email_address);

        mFloatingActionButton = (FloatingActionButton)findViewById(R.id.fab);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name = mFirstName.getText().toString();
                String last_name = mLastName.getText().toString();
                String emailAddress = mEmailAddress.getText().toString();

                if (mFirstName.length() == 0 || mEmailAddress.length() == 0) {
                    Snackbar.make(v, getResources().getString(R.string.blanks_not_filled), Snackbar.LENGTH_LONG).show();
                    return;
                }

                ContentValues contentValues = new ContentValues();

                contentValues.put(WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN, first_name);
                contentValues.put(WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN, last_name);
                contentValues.put(WhizBangContract.WhizBangEntry.EMAIL_COLUMN, emailAddress);

                getContentResolver().insert(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY, contentValues);

                finish();
            }
        });
    }
}