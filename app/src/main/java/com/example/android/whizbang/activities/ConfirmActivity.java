package com.example.android.whizbang.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.whizbang.R;
import com.example.android.whizbang.database.WhizBangContract;

import java.text.DateFormat;
import java.util.Date;

import static com.example.android.whizbang.R.id.amount_owed;

public class ConfirmActivity extends AppCompatActivity {

    private static final String TAG = "ConfirmActivity";


    private static final int PERMISSION_CODE = 46;
    FloatingActionButton mFloatingActionButton;
    TextView mTextView;
    EditText mAmountOwed;
    TextView mConfirmSendingClient;
    String email_address;
    String phone_number;
    String body_of_sms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.send_mail_fab);
        mTextView = (TextView) findViewById(R.id.confirm_text);
        mAmountOwed = (EditText) findViewById(amount_owed);
        mConfirmSendingClient = (TextView) findViewById(R.id.confirm_sending_client);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String id = intent.getStringExtra("id");

        mTextView.setText("How much does " + name + " owe?");

        final String[] mSelection = new String[]{id};

        Cursor editCursor = getContentResolver().query(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY, null, "_id=?", mSelection, null);

        if (editCursor.moveToFirst()) {
            while (!editCursor.isAfterLast()) {
                email_address = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.EMAIL_COLUMN));
                phone_number = editCursor.getString(editCursor.getColumnIndex(WhizBangContract.WhizBangEntry.PHONE_NUMBER_COLUMN));
                editCursor.moveToNext();
            }
        }

        if (email_address.length() == 0) {
            mConfirmSendingClient.setText(getResources().getString(R.string.send_via_text));
            mFloatingActionButton.setOnClickListener(sendViaText);
        } else {
            mConfirmSendingClient.setText(getResources().getString(R.string.send_via_email));
            mFloatingActionButton.setOnClickListener(sendViaEmail);
        }
        editCursor.close();
    }

    private View.OnClickListener sendViaText = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setUpAndSendSmsMessage();
        }
    };

    private View.OnClickListener sendViaEmail = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (mAmountOwed.length() == 0) {
                Snackbar.make(v, getResources().getString(R.string.no_amount_owed), Snackbar.LENGTH_LONG).show();
                return;
            }

            final String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
            final String final_amount_owed = mAmountOwed.getText().toString();
            final String email_subject = "Main Street Cafe Billing";
            final String email_body = "Hello. This is your monthly notice. As of " + currentDateTimeString + ", you owe Main Street Cafe $" +
                    final_amount_owed + ". You can drop payment by on your next visit. Thank you for your patronage! Notice: This email inbox is not " +
                    "monitored.";

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email_address});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, email_subject);
            emailIntent.putExtra(Intent.EXTRA_TEXT, email_body);

            if (emailIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(emailIntent);
            } else {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.no_clients_installed), Toast.LENGTH_LONG).show();
            }

        }
    };

    private void sendSms(String number, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(number, null, message, null, null);
        Toast.makeText(getApplicationContext(), "Text message sent", Toast.LENGTH_SHORT).show();
    }

    private void setUpAndSendSmsMessage() {
        String total_amount_owed = mAmountOwed.getText().toString();
        String body_of_sms = "Hello. This is a reminder from Main Street Cafe. You owe " + total_amount_owed + ". You can drop payment by on your next visit. Thank you.";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                Log.d(TAG, "setUpAndSendSmsMessage: permissions need to be granted");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_CODE);
            }
        } else {
            sendSms(phone_number, body_of_sms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    sendSms(phone_number, body_of_sms);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission not granted, Sending failed", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onRequestPermissionsResult: permission not granted");
                }
            }
        }
    }
}
