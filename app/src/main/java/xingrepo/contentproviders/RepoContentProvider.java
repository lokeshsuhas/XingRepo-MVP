package xingrepo.contentproviders;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import xingrepo.db.RepoDatabaseHelper;
import xingrepo.db.RepoTable;
import xingrepo.service.RepoApiUtils;

/**
 * Created by Lokesh on 23-02-2016.
 */
public class RepoContentProvider extends ContentProvider {
    private static final int REPOS = 10;
    private static final String AUTHORITY = "xingrepo.contentproviders";
    private static final String BASE_PATH = "repos";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, REPOS);
    }

    // database
    private RepoDatabaseHelper database;

    @Override
    public boolean onCreate() {
        database = new RepoDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(RepoTable.TABLE_REPO);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case REPOS:
                queryBuilder.appendWhere(RepoTable.COLUMN_PAGER + "=" + uri.getQueryParameter(RepoTable.COLUMN_PAGER));
                queryBuilder.appendWhere(" AND " + RepoTable.COLUMN_COUNT + "=" + RepoApiUtils.PAGE_COUNT);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case REPOS:
                id = sqlDB.insert(RepoTable.TABLE_REPO, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        //optional as we are not using the content observer concept
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case REPOS:
                rowsDeleted = sqlDB.delete(RepoTable.TABLE_REPO, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        //optional as we are not using the content observer concept
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case REPOS:
                rowsUpdated = sqlDB.update(RepoTable.TABLE_REPO,
                        values,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        //optional as we are not using the content observer concept
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
