package wael.mobile.dev.popularmovies.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

import wael.mobile.dev.popularmovies.database.tables.ListsTable;
import wael.mobile.dev.popularmovies.database.tables.PopularMoviesTable;


public class PopularMoviesProvider extends ContentProvider {


    // wael.mobile.dev.popularmovies.database
    private PopularMoviesDbHelper mOpenHelper;

    private static final String AUTHORITY = "wael.mobile.dev.popularmovies.provider";

    public static final Uri RECORDS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + PopularMoviesTable.CONTENT_PATH);

    public static final Uri LISTS_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + ListsTable.CONTENT_PATH);

    private static final int RECORDS_ALL = 10;
    private static final int RECORD_ID = 11;
    private static final int LISTS_ALL = 12;
    private static final int LIST_ID = 13;

    private static final UriMatcher TEST_PROVIDER_URI_MATCHER;

    static {
        TEST_PROVIDER_URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        TEST_PROVIDER_URI_MATCHER.addURI(AUTHORITY, PopularMoviesTable.CONTENT_PATH,
                RECORDS_ALL);
        TEST_PROVIDER_URI_MATCHER.addURI(AUTHORITY, PopularMoviesTable.CONTENT_PATH
                + "/#", RECORD_ID);
        TEST_PROVIDER_URI_MATCHER.addURI(AUTHORITY, ListsTable.CONTENT_PATH,
                LISTS_ALL);
        TEST_PROVIDER_URI_MATCHER.addURI(AUTHORITY, ListsTable.CONTENT_PATH
                + "/#", LIST_ID);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqlDB = mOpenHelper.getWritableDatabase();
        int rowsDeleted = 0;
        String id;
        switch (TEST_PROVIDER_URI_MATCHER.match(uri)) {
            case RECORDS_ALL:
                rowsDeleted = sqlDB.delete(PopularMoviesTable.TABLE_RECORDS, selection,
                        selectionArgs);
                break;
            case RECORD_ID:
                //retrieve the record id to delete
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(PopularMoviesTable.TABLE_RECORDS,
                            PopularMoviesTable._ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(PopularMoviesTable.TABLE_RECORDS,
                            PopularMoviesTable._ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        switch (TEST_PROVIDER_URI_MATCHER.match(uri)) {
            case RECORDS_ALL:
                return PopularMoviesTable.CONTENT_TYPE;
            case RECORD_ID:
                return PopularMoviesTable.CONTENT_ITEM_TYPE;
            case LISTS_ALL:
                return ListsTable.CONTENT_TYPE;
            case LIST_ID:
                return ListsTable.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        Uri itemUri = null;

        long id = 0;


        switch (TEST_PROVIDER_URI_MATCHER.match(uri)) {
            case RECORDS_ALL:

                id = database.insert(PopularMoviesTable.TABLE_RECORDS, null, values);
                if (id > 0) {
                    // notify all listeners of changes and return itemUri:
                    itemUri = ContentUris.withAppendedId(uri, id);
                    getContext().getContentResolver().notifyChange(itemUri, null);
                } else {
                    // something went wrong:
                    throw new SQLException("Problem while inserting into "
                            + PopularMoviesTable.TABLE_RECORDS + ", uri: " + uri);
                }
                break;
            case LISTS_ALL:

                id = database.insert(ListsTable.TABLE_LISTS, null, values);
                if (id > 0) {
                    // notify all listeners of changes and return itemUri:
                    itemUri = ContentUris.withAppendedId(uri, id);
                    getContext().getContentResolver().notifyChange(itemUri, null);
                } else {
                    // something went wrong:
                    throw new SQLException("Problem while inserting into "
                            + ListsTable.TABLE_LISTS + ", uri: " + uri);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        return itemUri;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new PopularMoviesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // Using SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (TEST_PROVIDER_URI_MATCHER.match(uri)) {
            case RECORDS_ALL:
                // Check if the caller has requested a column which does not
                // exists
                checkRecordsTableColumns(projection);
                // Set the table
                queryBuilder.setTables(PopularMoviesTable.TABLE_RECORDS);
                break;
            case RECORD_ID:
                // Check if the caller has requested a column which does not
                // exists
                checkRecordsTableColumns(projection);
                // Set the table
                queryBuilder.setTables(PopularMoviesTable.TABLE_RECORDS);
                // Adding the ID to the original query
                queryBuilder.appendWhere(PopularMoviesTable._ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // Make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase database = mOpenHelper.getWritableDatabase();
        int rowsUpdated = 0;
        String id;
        switch (TEST_PROVIDER_URI_MATCHER.match(uri)) {
            case RECORD_ID:

                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = database.update(PopularMoviesTable.TABLE_RECORDS,
                            values, PopularMoviesTable._ID + "=" + id, null);
                } else {
                    rowsUpdated = database.update(PopularMoviesTable.TABLE_RECORDS,
                            values, PopularMoviesTable._ID + "=" + id + " and "
                                    + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkRecordsTableColumns(String[] projection) {

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(PopularMoviesTable.PROJECTION_ALL));
            // Check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }
}