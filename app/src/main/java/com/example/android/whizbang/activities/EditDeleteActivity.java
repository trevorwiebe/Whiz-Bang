package com.example.android.whizbang.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
    Button mSave;
    Button mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_delete);

        mEditFirstName = (EditText) findViewById(R.id.edit_first_name_ed);
        mEditLastName = (EditText) findViewById(R.id.edit_last_name_ed);
        mEditEmailAddress = (EditText) findViewById(R.id.edit_email_address_ed);
        mSave = (Button) findViewById(R.id.save);
        mDelete = (Button) findViewById(R.id.delete);

        Intent editIntent = getIntent();

        final String name = editIntent.getStringExtra("first_name");
        final String last_name2 = editIntent.getStringExtra("last_name");
        final String transfer_int = editIntent.getStringExtra("int");
        final String[] mSelection = new String[]{name, last_name2};

        final Cursor editCursor = getContentResolver().query(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY, null, "first_name=? AND last_name=?", mSelection, null);


        if (editCursor.moveToFirst()) {
            while (!editCursor.isAfterLast()) {
                String first_name = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN));
                String last_name = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN));
                String email_address = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.EMAIL_COLUMN));

                Log.d(TAG, "onCreate: " + first_name + " " + last_name);
                mEditFirstName.setText(first_name);
                mEditLastName.setText(last_name);
                mEditEmailAddress.setText(email_address);
                editCursor.moveToNext();
            }
        }

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY;
                uri = uri.buildUpon().appendPath(transfer_int).build();

                int numsDeleted = getContentResolver().delete(uri, null, null);

                String stringNumsDeleted = Integer.toString(numsDeleted);

                Toast.makeText(getBaseContext(), stringNumsDeleted + " record was deleted.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edited_first_name = mEditFirstName.getText().toString();
                String edited_last_name = mEditLastName.getText().toString();
                String edited_email_address = mEditEmailAddress.getText().toString();

                ContentValues contentValues = new ContentValues();

                contentValues.put(WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN, edited_first_name);
                contentValues.put(WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN, edited_last_name);
                contentValues.put(WhizBangContract.WhizBangEntry.EMAIL_COLUMN, edited_email_address);

                getContentResolver().update(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY, contentValues, "first_name=? AND last_name=?", mSelection);

                finish();
            }
        });

        editCursor.close();

    }
}
