package com.example.android.whizbang;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.whizbang.database.WhizBangContract;

import java.util.ArrayList;

import static com.example.android.whizbang.MainActivity.EMAIL_SUBJECT;

public class ConfirmActivity extends AppCompatActivity {

    private static final String TAG = "ConfirmActivity";

    ArrayList<String> emailAddress = new ArrayList<>();
    FloatingActionButton mFloatingActionButton;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.send_mail_fab);
        mTextView = (TextView) findViewById(R.id.confirm_text);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        mTextView.setText("How much does " + name + " owe?");
        String[] selectedFromList = new String[]{name};
        String[] projection = new String[]{WhizBangContract.WhizBangEntry.EMAIL_COLUMN};

        Cursor cr = getContentResolver().query(WhizBangContract.WhizBangEntry.CONTENT_URI, null, null, null, null);

        if (cr.moveToFirst()) {
            while (!cr.isAfterLast()) {
                String email_address = cr.getString(cr.getColumnIndex(WhizBangContract.WhizBangEntry.EMAIL_COLUMN));
                Log.d(TAG, "onCreate: " + email_address);
//            emailAddress.add(cr.getString(0));
                cr.moveToNext();
            }
        }

        Log.d(TAG, "onCreate: " + emailAddress.toString());

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress.get(0));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
                emailIntent.putExtra(Intent.EXTRA_TEXT, "This is the body of the email");

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
