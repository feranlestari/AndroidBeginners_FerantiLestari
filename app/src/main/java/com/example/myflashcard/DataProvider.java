package com.example.myflashcard;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by SRIN on 6/9/2017.
 */
public class DataProvider extends ContentProvider {
    private static final int CARD = 100;

    private SQLiteDatabase db;

    public static final String AUTHORITY = "com.example.myflashcard.DataProvider";
    public static final String BASE_PATH = DatabaseHelper.DATABASE_NAME + "/";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri CARD_URI = Uri.withAppendedPath(CONTENT_URI, BASE_PATH + CardContract.CardData.TABLE_NAME);

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, BASE_PATH + CardContract.CardData.TABLE_NAME, CARD);
    }

    @Override
    public boolean onCreate() {
        db = new DatabaseHelper(getContext()).getWritableDatabase();
        return (db != null);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID;
        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case CARD:
                rowID = db.insert(CardContract.CardData.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return Uri.parse(BASE_PATH + "/" + rowID);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case CARD:
                cursor = db.query(CardContract.CardData.TABLE_NAME, projection, null, null, null, null, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deletedRows;
        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case CARD:
                deletedRows = db.delete(CardContract.CardData.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updatedRows;
        int uriType = URI_MATCHER.match(uri);

        switch (uriType) {
            case CARD:
                updatedRows = db.update(CardContract.CardData.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRows;
    }

    @Override
    public String getType(Uri uri) {
        return (uri != null) ? uri.toString() : null;
    }

}