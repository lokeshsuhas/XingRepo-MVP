package xingrepo.view;

import java.util.List;

import xingrepo.model.RepoModel;

/**
 * Created by Lokesh on 22-02-2016.
 */
public interface IRepoView {
    void loadAdapter(List<RepoModel> models);

    void showError(String message);

    void showLoading();

    void hideLoading();

    void reachedLimit();

    void resetAdapter();
}
