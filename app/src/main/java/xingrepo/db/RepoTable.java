package xingrepo.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Lokesh on 23-02-2016.
 */
public class RepoTable {
    // Database table
    public static final String TABLE_REPO = "repo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PAGER = "pager";
    public static final String COLUMN_DATA = "data";
    public static final String COLUMN_COUNT = "count";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_REPO
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_PAGER + " integer not null, "
            + COLUMN_DATA + " text not null,"
            + COLUMN_COUNT
            + " integer not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_REPO);
        onCreate(database);
    }
}
