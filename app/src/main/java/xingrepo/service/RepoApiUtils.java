package xingrepo.service;

import android.content.ContentResolver;
import android.content.ContentValues;

import xingrepo.cache.RepoCacheReader;
import xingrepo.cache.onCacheWriteListener;
import xingrepo.contentproviders.RepoContentProvider;
import xingrepo.db.RepoTable;

/**
 * Created by Lokesh on 22-02-2016.
 */
public class RepoApiUtils {

    public final static int PAGE_COUNT = 10;
    private final static String XING_REPO_URL = "https://api.github.com/users/xing/repos?page=%d&per_page=%d&client_id=44a4c43512e961630d91&client_secret=cffbf3805b3e4caa3bdf28879444cb807dc9a9e4";


    public static void getRepos(final ContentResolver resolver, Class<?> decoderType, final int page, onResultListener listener) {
        final ApiFactory apiFactory = new ApiFactory.Builder(decoderType)
                .setUrl(String.format(XING_REPO_URL, page, PAGE_COUNT))
                .setReadFromCache(true)
                .setListener(listener)
                .setCacheReader(new RepoCacheReader(resolver,page))
                .setCacheWriteListener(new onCacheWriteListener() {
                    @Override
                    public void insert(String data) {
                        ContentValues values = new ContentValues();
                        values.put(RepoTable.COLUMN_PAGER, page);
                        values.put(RepoTable.COLUMN_COUNT, RepoApiUtils.PAGE_COUNT);
                        values.put(RepoTable.COLUMN_DATA, data);
                        resolver.insert(RepoContentProvider.CONTENT_URI, values);
                    }
                })
                .build();
        apiFactory.get();
    }
}
