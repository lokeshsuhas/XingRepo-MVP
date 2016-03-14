package xingrepo.service;

/**
 * Created by Lokesh on 22-02-2016.
 */
public interface onResultListener<T> {
    void onSuccess(T data);

    void onError(String error);
}
