package wael.mobile.dev.popularmovies.database.tables;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class PopularMoviesTable implements BaseColumns {

    // Records wael.mobile.dev.popularmovies.database table
    public static final String TABLE_RECORDS = "movies";

    // table records fields
    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String OVERVIEW = "overview";
    public static final String BACKDROPPATH = "backdroppath";
    public static final String ORIGINALLANGAGE = "originallangage";
    public static final String VOTECOUNT = "votecount";
    public static final String VOTEAVERAGE = "voteaverage";
    public static final String GENREIDS = "genreids";
    // info for content provider
    public static final String CONTENT_PATH = "movies";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vnd.testprovider.movies";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/vnd.testprovider.movies";
    public static final String[] PROJECTION_ALL = {_ID, ID, TITLE, OVERVIEW, BACKDROPPATH, ORIGINALLANGAGE, VOTECOUNT, GENREIDS, VOTEAVERAGE};
    // records table creation statement
    private static final String CREATE_RECORDS_TABLE = "CREATE TABLE "
            + TABLE_RECORDS + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ID + " INTEGER, "
            + TITLE + " TEXT, "
            + OVERVIEW + " TEXT, "
            + BACKDROPPATH + " TEXT, "
            + ORIGINALLANGAGE + " TEXT, "
            + VOTECOUNT + " INTEGER, "
            + GENREIDS + " TEXT, "
            + VOTEAVERAGE + " REAL); ";
//_id, title, overview, backdroppath, originallangage, votecount, voteaverage
    /**
     * create records table
     * 
     * @param database
     */
    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_RECORDS_TABLE);
    }
    /**
     * upgrade the records table
     * 
     * @param database
     * @param oldVersion
     * @param newVersion
     */
    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
            int newVersion) {
        // TODO
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        onCreate(database);
    }
}
