package com.example.android.whizbang;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.whizbang.database.WhizBangContract;

import java.util.ArrayList;

import static com.example.android.whizbang.R.id.amount_owed;

public class ConfirmActivity extends AppCompatActivity {

    private static final String TAG = "ConfirmActivity";

    ArrayList<String> emailAddress = new ArrayList<>();

    FloatingActionButton mFloatingActionButton;
    TextView mTextView;
    EditText mAmountOwed;
    String email_address;
    String phone_number;
    String connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.send_mail_fab);
        mTextView = (TextView) findViewById(R.id.confirm_text);
        mAmountOwed = (EditText) findViewById(amount_owed);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        mTextView.setText("How much does " + name + " owe?");

        // TODO: 4/6/2017 get correct date
        final String date = "hello";

        // TODO: 4/2/2017 fix database problems with no quering for email address at a specific name
        final String[] mSelection = new String[]{name};

        Cursor editCursor = getContentResolver().query(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY, null, "first_name=?", mSelection, null);


        if (editCursor.moveToFirst()) {
            while (!editCursor.isAfterLast()) {
                email_address = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.EMAIL_COLUMN));
                phone_number = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.PHONE_NUMBER));
                editCursor.moveToNext();
            }
        }


        Log.d(TAG, "onCreate: finished " + connection);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mAmountOwed.length() == 0) {
                    Snackbar.make(v, getResources().getString(R.string.no_amount_owed), Snackbar.LENGTH_LONG).show();
                    return;
                }

                String amount_owed = mAmountOwed.getText().toString();

                // TODO: 4/3/2017 query database to get email body and email subject to use instead of hard string

                final String email_subject = "Main Street Cafe Billing";
                final String body_of_email = "Hello there, This is your monthly notice. As of " + date + " you owe Main Street Cafe $" + amount_owed +
                        ". You can drop payment by on your next visit. Thank you for your patronage! Notice: This email inbox is not monitored.";

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("*/*");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email_address, phone_number});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, email_subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, body_of_email);

                if (emailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(emailIntent);
                } else {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.no_clients_installed), Toast.LENGTH_LONG).show();
                }
            }
        });

        editCursor.close();
    }
}
