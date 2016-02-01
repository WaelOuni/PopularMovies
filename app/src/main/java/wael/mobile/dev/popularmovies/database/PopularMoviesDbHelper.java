package wael.mobile.dev.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import wael.mobile.dev.popularmovies.database.tables.GenresMoviesTable;
import wael.mobile.dev.popularmovies.database.tables.ListsTable;
import wael.mobile.dev.popularmovies.database.tables.PopularMoviesTable;


public class PopularMoviesDbHelper extends SQLiteOpenHelper {

    // wael.mobile.dev.popularmovies.database name
    private static final String DATABASE_NAME = "popularMovies.db";
    // data base version
    private static final int DATABASE_VERSION = 1;

    /**
     * Basic constructor
     *
     * @param context
     */
    public PopularMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Constructor
     *
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public PopularMoviesDbHelper(Context context, String name,
                                 SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PopularMoviesTable.onCreate(db);
        GenresMoviesTable.onCreate(db);
        ListsTable.onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        PopularMoviesTable.onUpgrade(db, oldVersion, newVersion);
        GenresMoviesTable.onUpgrade(db, oldVersion, newVersion);
        ListsTable.onUpgrade(db, oldVersion, newVersion);
    }

}