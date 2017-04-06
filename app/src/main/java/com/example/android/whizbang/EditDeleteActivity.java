package com.example.android.whizbang;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.example.android.whizbang.database.WhizBangContract;

public class EditDeleteActivity extends AppCompatActivity {

    private static final String TAG = "EditDeleteActivity";

    EditText mEditFirstName;
    EditText mEditLastName;
    EditText mEditEmailAddress;
    EditText mEditPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);

        mEditFirstName = (EditText) findViewById(R.id.edit_first_name_ed);
        mEditLastName = (EditText) findViewById(R.id.edit_last_name_ed);
        mEditEmailAddress = (EditText) findViewById(R.id.edit_email_address_ed);
        mEditPhoneNumber = (EditText) findViewById(R.id.edit_phone_number_ed);

        Intent editIntent = getIntent();
        String name = editIntent.getStringExtra("name");

        String arr[] = name.split(" ");

        String[] mSelection = new String[]{arr[0]};

        Log.d(TAG, "onCreate: " + mSelection);

        Cursor editCursor = getContentResolver().query(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY, null, "first_name=?", mSelection, null);


        if (editCursor.moveToFirst()) {
            while (!editCursor.isAfterLast()) {
                String first_name = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN));
                String last_name = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN));
                String email_address = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.EMAIL_COLUMN));
                String phone_number = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.PHONE_NUMBER));

                mEditFirstName.setText(first_name);
                mEditLastName.setText(last_name);
                mEditEmailAddress.setText(email_address);
                mEditPhoneNumber.setText(phone_number);
                editCursor.moveToNext();
            }
        }


        editCursor.close();

    }
}
