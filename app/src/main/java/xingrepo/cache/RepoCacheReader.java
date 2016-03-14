package xingrepo.cache;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import xingrepo.contentproviders.RepoContentProvider;
import xingrepo.db.RepoTable;
import xingrepo.service.RepoApiUtils;
import xingrepo.service.model.CallResult;

/**
 * Created by Lokesh on 23-02-2016.
 */
public class RepoCacheReader implements ICacheReader {

    private ContentResolver resolver;
    private onCacheReadListener readListener;
    private int page;

    public RepoCacheReader(ContentResolver r, int pageNo) {
        this.resolver = r;
        this.page = pageNo;
    }

    @Override
    public void call(onCacheReadListener listener) {
        this.readListener = listener;
        new DbRepoAsyncTask().execute(page, null, null);
    }

    //Repo specific call to database
    private class DbRepoAsyncTask extends AsyncTask<Integer, String, CallResult> {
        @Override
        protected CallResult doInBackground(Integer... params) {
            CallResult result = new CallResult();
            try {
                String[] projection = {RepoTable.COLUMN_DATA};
                Uri uri = Uri.parse(RepoContentProvider.CONTENT_URI + "?" + RepoTable.COLUMN_COUNT + "=" + RepoApiUtils.PAGE_COUNT + "&" + RepoTable.COLUMN_PAGER + "=" + params[0]);
                Cursor cursor = resolver.query(uri, projection, null, null, null);
                result.IsSuccess = true;
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    String data = cursor.getString(cursor
                            .getColumnIndexOrThrow(RepoTable.COLUMN_DATA));

                    result.Data = data;
                } else {
                    result.Data = null;
                }
            } catch (Exception err) {
                result.IsSuccess = false;
                result.ErrorMessage = err.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(CallResult result) {
            super.onPostExecute(result);
            if (result.IsSuccess) {
                if (result.Data == null)
                    readListener.onEmpty();
                else
                    readListener.onSuccess(result.Data);
            } else {
                readListener.onError(result.ErrorMessage);
            }
        }
    }

}
