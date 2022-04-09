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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class HistoricalDataProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.stockportfolio.providers.HistoricalDataProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/history";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String ID = "id";
    static final String CLOSE = "close";
    static final String TICKER = "ticker";
    static final int HISTORY = 1;
    static final int HISTORY_ID = 2;
    static final UriMatcher uriMatcher;
    static final String DATABASE_NAME = "Historical_Data";
    static final String TABLE_NAME = "history";
    static Map<String, Integer> records = new HashMap<>();

    // Database specific constant declarations
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + TABLE_NAME +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " close DECIMAL(5,3) NOT NULL, " +
                    " ticker VARCHAR(50) NOT NULL);";
    private static HashMap<String, String> HISTORY_PROJECTION_MAP;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "history", HISTORY);
        uriMatcher.addURI(PROVIDER_NAME, "history/#", HISTORY_ID);
    }

    private SQLiteDatabase db;

    public static String getClose() {
        return CLOSE;
    }

    public static String getTicker() { return  TICKER; }

    public static Uri getContentUri() {
        return CONTENT_URI;
    }

    public static Map<String, Integer> getRecords() {
        return records;
    }



    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        // Create new db on start
        db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
        return db != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case HISTORY:
                qb.setProjectionMap(HISTORY_PROJECTION_MAP);
                break;
            case HISTORY_ID:
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
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            // All records
            case HISTORY:
                return "vnd.android.cursor.dir/vnd.com.example.provider.history";
            // Single particular record
            case HISTORY_ID:
                return "vnd.android.cursor.item/vnd.com.example.provider.history";
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
            // create new db on start
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}

