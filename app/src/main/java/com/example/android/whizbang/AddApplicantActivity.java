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

    EditText mName;
    EditText mEmailAddress;
    FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_applicant);

        mName = (EditText)findViewById(R.id.name);
        mEmailAddress = (EditText)findViewById(R.id.email_address);
        mFloatingActionButton = (FloatingActionButton)findViewById(R.id.fab);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mName.getText().toString();
                String emailAddress = mEmailAddress.getText().toString();

                if(mName == null || mEmailAddress == null){
                    Snackbar.make(v, "Please fill all blanks", Snackbar.LENGTH_LONG).show();
                    return;
                }

                ContentValues contentValues = new ContentValues();

                contentValues.put(WhizBangContract.WhizBangEntry.NAME_COLUMN, name);
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
