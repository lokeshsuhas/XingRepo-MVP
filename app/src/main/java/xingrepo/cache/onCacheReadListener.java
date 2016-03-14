package xingrepo.cache;

/**
 * Created by Lokesh on 23-02-2016.
 */
public interface onCacheReadListener<T> {
    void onSuccess(T data);
    void onEmpty();
    void onError(String error);
}
