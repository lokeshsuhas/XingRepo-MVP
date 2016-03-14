package xingrepo.interactor;

import android.content.ContentResolver;

import xingrepo.model.RepoModelList;
import xingrepo.service.RepoApiUtils;
import xingrepo.service.onResultListener;

/**
 * Created by Lokesh on 22-02-2016.
 */
public class RepoInteractorImpl implements IRepoInteractor {

    private ContentResolver contentResolver;

    public RepoInteractorImpl(ContentResolver resolver) {
        this.contentResolver = resolver;
    }

    @Override
    public void loadRepo(onResultListener listener, int pageCount) {
        RepoApiUtils.getRepos(contentResolver, RepoModelList.class, pageCount, listener);
    }
}
