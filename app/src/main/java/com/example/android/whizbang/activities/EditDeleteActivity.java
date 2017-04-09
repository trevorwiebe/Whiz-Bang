package com.example.android.whizbang.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.whizbang.R;
import com.example.android.whizbang.database.WhizBangContract;

public class EditDeleteActivity extends AppCompatActivity {

    private static final String TAG = "EditDeleteActivity";

    EditText mEditFirstName;
    EditText mEditLastName;
    EditText mEditEmailAddress;
    EditText mEditPhoneNumber;
    Button mSave;
    Button mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);

        mEditFirstName = (EditText) findViewById(R.id.edit_first_name_ed);
        mEditLastName = (EditText) findViewById(R.id.edit_last_name_ed);
        mEditEmailAddress = (EditText) findViewById(R.id.edit_email_address_ed);
        mEditPhoneNumber = (EditText) findViewById(R.id.edit_phone_number_ed);
        mSave = (Button) findViewById(R.id.save);
        mDelete = (Button) findViewById(R.id.delete);

        Intent editIntent = getIntent();

        final String transfer_int = editIntent.getStringExtra("int");
        final String[] mSelection = new String[]{transfer_int};

        final Cursor editCursor = getContentResolver().query(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY, null, "_id=?", mSelection, null);


        if (editCursor.moveToFirst()) {
            while (!editCursor.isAfterLast()) {
                String first_name = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN));
                String last_name = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN));
                String email_address = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.EMAIL_COLUMN));
                String phone_number = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.PHONE_NUMBER_COLUMN));

                mEditFirstName.setText(first_name);
                mEditLastName.setText(last_name);
                mEditEmailAddress.setText(email_address);
                mEditPhoneNumber.setText(phone_number);
                editCursor.moveToNext();
            }
        }

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY;
                uri = uri.buildUpon().appendPath(transfer_int).build();

                getContentResolver().delete(uri, null, null);

                Toast.makeText(getBaseContext(), getResources().getString(R.string.record_deleted), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEditFirstName.length() == 0 || (mEditEmailAddress.length() == 0 && mEditPhoneNumber.length() == 0)) {
                    Snackbar.make(v, getResources().getString(R.string.blanks_not_filled), Snackbar.LENGTH_LONG).show();
                    return;
                }

                String edited_first_name = mEditFirstName.getText().toString();
                String edited_last_name = mEditLastName.getText().toString();
                String edited_email_address = mEditEmailAddress.getText().toString();
                String edited_phone_number = mEditPhoneNumber.getText().toString();

                ContentValues contentValues = new ContentValues();

                contentValues.put(WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN, edited_first_name);
                contentValues.put(WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN, edited_last_name);
                contentValues.put(WhizBangContract.WhizBangEntry.EMAIL_COLUMN, edited_email_address);
                contentValues.put(WhizBangContract.WhizBangEntry.PHONE_NUMBER_COLUMN, edited_phone_number);

                getContentResolver().update(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY, contentValues, "id=?", mSelection);

                finish();
            }
        });

        editCursor.close();

    }
}
