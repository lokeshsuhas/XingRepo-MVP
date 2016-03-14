package xingrepo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lokesh on 23-02-2016.
 */
public class RepoDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "repotable.db";
    private static final int DATABASE_VERSION = 1;

    public RepoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        RepoTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        RepoTable.onUpgrade(db, oldVersion, newVersion);
    }
}
