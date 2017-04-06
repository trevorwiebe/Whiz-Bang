package com.example.android.whizbang.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by thisi on 3/30/2017.
 */

public class WhizBangContentProvider extends ContentProvider {

    public static final int INFORMATION = 100;
    public static final int INFORMATION_WITH_ID = 101;

    public static final int EMAIL = 200;
    public static final int EMAIL_WITH_ID = 201;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(WhizBangContract.AUTHORITY, WhizBangContract.PATH_INFORMATION, INFORMATION);
        uriMatcher.addURI(WhizBangContract.AUTHORITY, WhizBangContract.PATH_INFORMATION + "/#", INFORMATION_WITH_ID);
        uriMatcher.addURI(WhizBangContract.AUTHORITY, WhizBangContract.PATH_EMAIL, EMAIL);
        uriMatcher.addURI(WhizBangContract.AUTHORITY, WhizBangContract.PATH_EMAIL + "/*", EMAIL_WITH_ID);

        return uriMatcher;
    }

    WhizBangDbHelper mWhizBangDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mWhizBangDbHelper = new WhizBangDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mWhizBangDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor informationCursor;

        switch (match){
            case INFORMATION:
                informationCursor = db.query(WhizBangContract.WhizBangEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case INFORMATION_WITH_ID:

                String id = uri.getPathSegments().get(1);

                String mSelection = "_string=?";
                String[] mSelectionArgs = new String[]{id};

                informationCursor = db.query(WhizBangContract.WhizBangEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case EMAIL:
                informationCursor = db.query(WhizBangContract.WhizBangEmail.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case EMAIL_WITH_ID:
                String id2 = uri.getPathSegments().get(1);

                String mSelection2 = "_id=?";
                String[] mSelectionArgs2 = new String[]{id2};

                informationCursor = db.query(WhizBangContract.WhizBangEmail.TABLE_NAME,
                        projection,
                        mSelection2,
                        mSelectionArgs2,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        informationCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return informationCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mWhizBangDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri locationUri;

        switch (match){

            case INFORMATION:
                long id = db.insert(WhizBangContract.WhizBangEntry.TABLE_NAME, null, values);
                if(id > 0){
                    locationUri = ContentUris.withAppendedId(WhizBangContract.WhizBangEntry.CONTENT_URI_ENTRY, id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            case EMAIL:
                long id2 = db.insert(WhizBangContract.WhizBangEmail.TABLE_NAME, null, values);
                if (id2 > 0) {
                    locationUri = ContentUris.withAppendedId(WhizBangContract.WhizBangEmail.CONTENT_URI_EMAIL, id2);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return locationUri;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mWhizBangDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int informationUpdated;

        switch (match) {
            case INFORMATION:
                informationUpdated = db.update(WhizBangContract.WhizBangEntry.TABLE_NAME, values, null, null);
                break;
            case INFORMATION_WITH_ID:
                String id = uri.getPathSegments().get(1);

                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};

                informationUpdated = db.update(WhizBangContract.WhizBangEntry.TABLE_NAME, values, mSelection, mSelectionArgs);
                break;
            case EMAIL:
                informationUpdated = db.update(WhizBangContract.WhizBangEmail.TABLE_NAME, values, null, null);
                break;
            case EMAIL_WITH_ID:
                String id2 = uri.getPathSegments().get(1);

                String mSelection2 = "_id=?";
                String[] mSelectionArgs2 = new String[]{id2};

                informationUpdated = db.update(WhizBangContract.WhizBangEmail.TABLE_NAME, values, mSelection2, mSelectionArgs2);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if (informationUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return informationUpdated;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mWhizBangDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int informationDeleted;

        switch (match){
            case INFORMATION:
                informationDeleted = db.delete(WhizBangContract.WhizBangEntry.TABLE_NAME, null, null);
                break;
            case INFORMATION_WITH_ID:
                String id = uri.getPathSegments().get(1);
                informationDeleted = db.delete(WhizBangContract.WhizBangEntry.TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
        if(informationDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return informationDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
