package xingrepo.presenter;

import android.content.ContentResolver;

import java.util.List;

import xingrepo.interactor.IRepoInteractor;
import xingrepo.interactor.RepoInteractorImpl;
import xingrepo.model.RepoModel;
import xingrepo.service.onResultListener;
import xingrepo.view.IRepoView;

/**
 * Created by Lokesh on 22-02-2016.
 */
public class RepoPresenterImpl implements IRepoPresenter, onResultListener<List<RepoModel>> {

    private IRepoView repoView;
    private IRepoInteractor repoInteractor;
    private int pager = 1;
    private boolean isLoading = false;
    private boolean reachedLimit = false;
    public RepoPresenterImpl(IRepoView view,ContentResolver resolver) {
        repoView = view;
        repoInteractor = new RepoInteractorImpl(resolver);
    }

    @Override
    public void getRepo() {
        repoView.showLoading();
        repoInteractor.loadRepo(this, pager);
        isLoading = true;
    }

    @Override
    public void onDestroy() {
        repoView = null;
    }

    @Override
    public void clear() {
        pager = 1;
        reachedLimit = false;
        isLoading = false;
        repoView.resetAdapter();

    }

    @Override
    public boolean isLoading() {
        return this.isLoading;
    }

    @Override
    public boolean isReachedLimit() {
        return this.reachedLimit;
    }

    @Override
    public void onSuccess(List<RepoModel> data) {
        isLoading = false;
        repoView.hideLoading();
        if (data.size() > 0) {
            pager++;
            repoView.loadAdapter(data);
        } else {
            reachedLimit = true;
            repoView.reachedLimit();
        }
    }

    @Override
    public void onError(String error) {
        repoView.hideLoading();
        repoView.showError(error);
    }
}
