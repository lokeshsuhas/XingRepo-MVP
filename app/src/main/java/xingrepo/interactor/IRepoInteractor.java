package xingrepo.interactor;

import xingrepo.service.onResultListener;

/**
 * Created by Lokesh on 22-02-2016.
 */
public interface IRepoInteractor {
    void loadRepo(onResultListener listener, int page);
}
