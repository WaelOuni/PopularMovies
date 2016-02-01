package wael.mobile.dev.popularmovies.database.tables;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class GenresMoviesTable implements BaseColumns {

    // Records wael.mobile.dev.popularmovies.database table
    public static final String TABLE_RECORDS = "genres";

    // table records fields
    public static final String IDGENRE = "id";
    public static final String NAMEGENRE = "name";
    // info for content provider
    public static final String CONTENT_PATH = "genres";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/vnd.testprovider.genres";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/vnd.testprovider.genres";
    public static final String[] PROJECTION_ALL = {_ID, IDGENRE, NAMEGENRE};
    // records table creation statement
    private static final String CREATE_RECORDS_TABLE = "CREATE TABLE "
            + TABLE_RECORDS + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + IDGENRE + " INTEGER, "
            + NAMEGENRE + " TEXT NOT NULL); ";

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
