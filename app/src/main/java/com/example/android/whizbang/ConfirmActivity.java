package com.example.android.whizbang;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.whizbang.database.WhizBangContract;

import java.util.ArrayList;

import static com.example.android.whizbang.R.id.amount_owed;

public class ConfirmActivity extends AppCompatActivity {

    public static final String EMAIL_SUBJECT = "Main Street Cafe Billing";
    private static final String TAG = "ConfirmActivity";

    ArrayList<String> emailAddress = new ArrayList<>();

    FloatingActionButton mFloatingActionButton;
    TextView mTextView;
    EditText mAmountOwed;

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

        final String date = "hello";

        String[] selectedFromList = new String[]{name};
        String[] projection = new String[]{WhizBangContract.WhizBangEntry.EMAIL_COLUMN};

        // TODO: 4/2/2017 fix database problems with no quering for email address at a specific name
        Cursor cr = getContentResolver().query(WhizBangContract.WhizBangEntry.CONTENT_URI, null, null, null, null);

        if (cr.moveToFirst()) {
            while (!cr.isAfterLast()) {
                String email_address = cr.getString(cr.getColumnIndex(WhizBangContract.WhizBangEntry.EMAIL_COLUMN));
                cr.moveToNext();
            }
        }

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {

            // TODO: 4/2/2017 fix once you choose email to always finish with that action

            @Override
            public void onClick(View v) {

                String amount_owed = mAmountOwed.getText().toString();

                final String Body_of_email = "Hello there, This is your monthly notice. As of " + date + " you owe Main Street Cafe $" + amount_owed +
                        ". You can drop payment by on your next visit. Thank you for your patronage! Notice: This email inbox is not monitored.";

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"thisistrevor4@gmail.com"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
                emailIntent.putExtra(Intent.EXTRA_TEXT, Body_of_email);

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getBaseContext(), "There are no email clients installed.", Toast.LENGTH_LONG).show();
                }

            }
        });

        cr.close();
    }
}
