package com.example.stockportfolio.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

public class FavouriteProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.stockportfolio.FavouriteProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/favourite";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String ID = "id";
    static final String SYMBOL = "symbol";
    static final int FAVOURITE = 1;
    static final int FAVOURITE_ID = 2;
    static final UriMatcher uriMatcher;
    static final String DATABASE_NAME = "Historical_Data";
    static final String TABLE_NAME = "favourite";

    // Database specific constant declarations
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + TABLE_NAME +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " symbol TEXT NOT NULL);";
    private static HashMap<String, String> FAVOURITE_PROJECTION_MAP;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "favourite", FAVOURITE);
        uriMatcher.addURI(PROVIDER_NAME, "favourite/#", FAVOURITE_ID);
    }

    private SQLiteDatabase db;

    public static String getSymbol() {
        return SYMBOL;
    }

    public static Uri getContentUri() {
        return CONTENT_URI;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        // Create db if not exists
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case FAVOURITE:
                qb.setProjectionMap(FAVOURITE_PROJECTION_MAP);
                break;
            case FAVOURITE_ID:
                qb.appendWhere(ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }

        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = ID;
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // Register to watch a content URI for changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(TABLE_NAME, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            // All records
            case FAVOURITE:
                return "vnd.android.cursor.dir/vnd.com.example.provider.favourite";
            // Single particular record
            case FAVOURITE_ID:
                return "vnd.android.cursor.item/vnd.com.example.provider.favourite";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}