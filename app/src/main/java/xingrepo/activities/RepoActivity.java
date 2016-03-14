package xingrepo.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import xingrepo.R;
import xingrepo.adapters.LoadingView;
import xingrepo.adapters.RepoAdapter;
import xingrepo.db.RepoDatabaseHelper;
import xingrepo.model.RepoModel;
import xingrepo.presenter.IRepoPresenter;
import xingrepo.presenter.RepoPresenterImpl;
import xingrepo.view.IRepoView;

public class RepoActivity extends AppCompatActivity implements IRepoView {
    private ArrayAdapter<RepoModel> adapter;
    private ListView repo_listview;
    private LoadingView loadingView;
    private IRepoPresenter repoPresenter;
    private ProgressDialog loadingDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo);
        context = this;
        initListView();
        if (repoPresenter == null)
            repoPresenter = new RepoPresenterImpl(this, getContentResolver());

        repoPresenter.getRepo();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        repoPresenter.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_repo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear) {
            repoPresenter.clear();
            context.deleteDatabase(RepoDatabaseHelper.DATABASE_NAME);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadAdapter(List<RepoModel> models) {
        adapter.addAll(models);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String message) {
        hideLoading();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        repo_listview.addFooterView(loadingView);
    }

    @Override
    public void hideLoading() {
        repo_listview.removeFooterView(loadingView);
    }

    @Override
    public void reachedLimit() {
        Toast.makeText(context, R.string.no_more_items, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void resetAdapter() {
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    private void initListView() {
        repo_listview = (ListView) findViewById(R.id.repo_listview);
        repo_listview.setAdapter(adapter = new RepoAdapter(context));
        loadingView = new LoadingView(context);
        repo_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                if (repoPresenter != null && !repoPresenter.isLoading() && !repoPresenter.isReachedLimit() && lastVisibleItem == totalItemCount) {
                    if (repoPresenter != null) {
                        repoPresenter.getRepo();
                    }
                }
            }
        });

        repo_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                RepoModel model = adapter.getItem(position);
                showOptions(model);
                return false;
            }
        });
    }

    private void showOptions(final RepoModel model) {
        final CharSequence[] items = {getString(R.string.choose_repo), getString(R.string.choose_owner),
                getString(R.string.choose_cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.alert_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.choose_repo))) {
                    loadWeb(model.getRepoUrl());
                } else if (items[item].equals(getString(R.string.choose_owner))) {
                    loadWeb(model.getOwner().getUrl());
                } else if (items[item].equals(getString(R.string.choose_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void loadWeb(String url) {
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        List resolveInfo =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo.size() > 0) {
            startActivity(intent);
        } else {
            Toast.makeText(context, R.string.error_browser_not_supported, Toast.LENGTH_SHORT).show();
        }
    }
}
