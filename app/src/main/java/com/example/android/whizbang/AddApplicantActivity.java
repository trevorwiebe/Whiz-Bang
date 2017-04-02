package com.example.android.whizbang;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.whizbang.database.WhizBangContract;

public class AddApplicantActivity extends AppCompatActivity {

    EditText mFirstName;
    EditText mLastName;
    EditText mPhoneNumber;
    EditText mEmailAddress;
    FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_charge);

        mFirstName = (EditText) findViewById(R.id.first_name);
        mLastName = (EditText) findViewById(R.id.last_name);
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mEmailAddress = (EditText)findViewById(R.id.email_address);

        mFloatingActionButton = (FloatingActionButton)findViewById(R.id.fab);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            // TODO: 4/2/2017 add check mark icon to fab
            @Override
            public void onClick(View v) {
                String first_name = mFirstName.getText().toString();
                String last_name = mLastName.getText().toString();
                String phone_number = mPhoneNumber.getText().toString();
                String emailAddress = mEmailAddress.getText().toString();

                if (mFirstName.length() == 0 || (mEmailAddress.length() == 0 && mPhoneNumber.length() == 0)) {
                    Snackbar.make(v, "Please fill First Name, Email and/or Phone Number", Snackbar.LENGTH_LONG).show();
                    return;
                }

                ContentValues contentValues = new ContentValues();

                contentValues.put(WhizBangContract.WhizBangEntry.FIRST_NAME_COLUMN, first_name);
                contentValues.put(WhizBangContract.WhizBangEntry.LAST_NAME_COLUMN, last_name);
                contentValues.put(WhizBangContract.WhizBangEntry.PHONE_NUMBER, phone_number);
                contentValues.put(WhizBangContract.WhizBangEntry.EMAIL_COLUMN, emailAddress);

                Uri uri = getContentResolver().insert(WhizBangContract.WhizBangEntry.CONTENT_URI, contentValues);
                if(uri != null){
                    Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
}
