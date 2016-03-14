package xingrepo.presenter;

/**
 * Created by Lokesh on 22-02-2016.
 */
public interface IRepoPresenter {
    void getRepo();
    void onDestroy();
    void clear();
    boolean isLoading();
    boolean isReachedLimit();
}
