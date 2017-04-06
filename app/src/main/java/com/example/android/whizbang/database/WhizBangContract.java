package com.example.android.whizbang.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by thisi on 3/30/2017.
 */

public class WhizBangContract {

    public static final String AUTHORITY = "com.example.android.whizbang";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_INFORMATION = "information";
    public static final String PATH_EMAIL = "email";

    public static final class WhizBangEntry implements BaseColumns{

        public static final Uri CONTENT_URI_ENTRY = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INFORMATION).build();

        public static final String TABLE_NAME = "contact_info";

        public static final String FIRST_NAME_COLUMN = "first_name";
        public static final String LAST_NAME_COLUMN = "last_name";
        public static final String PHONE_NUMBER = "phone_number";
        public static final String EMAIL_COLUMN = "email";
    }

    public static final class WhizBangEmail implements BaseColumns {
        public static final Uri CONTENT_URI_EMAIL = BASE_CONTENT_URI.buildUpon().appendPath(PATH_EMAIL).build();

        public static final String TABLE_NAME = "email_info";

        public static final String EMAIL_BODY_COLUMN = "email_body";
        public static final String EMAIL_SUBJECT_COLUMN = "email_subject";
    }
}
