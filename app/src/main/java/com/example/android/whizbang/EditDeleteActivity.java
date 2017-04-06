package com.example.android.whizbang;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.android.whizbang.database.WhizBangContract;

public class EditDeleteActivity extends AppCompatActivity {

    private static final String TAG = "EditDeleteActivity";

    EditText mEditFirstName;
    EditText mEditLastName;
    EditText mEditEmailAddress;
    FloatingActionButton mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);

        mEditFirstName = (EditText) findViewById(R.id.edit_first_name_ed);
        mEditLastName = (EditText) findViewById(R.id.edit_last_name_ed);
        mEditEmailAddress = (EditText) findViewById(R.id.edit_email_address_ed);
        mSubmit = (FloatingActionButton) findViewById(R.id.submit_fab);

        Intent editIntent = getIntent();

        final String[] mSelection = new String[]{editIntent.getStringExtra("name")};

        Cursor editCursor = getContentResolver().query(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY, null, "first_name=?", mSelection, null);


        if (editCursor.moveToFirst()) {
            while (!editCursor.isAfterLast()) {
                String first_name = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN));
                String last_name = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN));
                String email_address = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.EMAIL_COLUMN));

                mEditFirstName.setText(first_name);
                mEditLastName.setText(last_name);
                mEditEmailAddress.setText(email_address);
                editCursor.moveToNext();
            }
        }

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edited_first_name = mEditFirstName.getText().toString();
                String edited_last_name = mEditLastName.getText().toString();
                String edited_email_address = mEditEmailAddress.getText().toString();

                if (mEditFirstName.length() == 0 && mEditLastName.length() == 0 && mEditEmailAddress.length() == 0) {


                    int numsDeleted = getContentResolver().delete(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY, "first_name=?", mSelection);

                    if (numsDeleted != 0) {
                        Snackbar.make(v, Integer.toString(numsDeleted), Snackbar.LENGTH_LONG).show();
                    }

                    return;
                }

                ContentValues contentValues = new ContentValues();

                contentValues.put(WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN, edited_first_name);
                contentValues.put(WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN, edited_last_name);
                contentValues.put(WhizBangContract.WhizBangEntry.EMAIL_COLUMN, edited_email_address);

                getContentResolver().update(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY, contentValues, null, null);

                finish();
            }
        });

        editCursor.close();

    }
}
