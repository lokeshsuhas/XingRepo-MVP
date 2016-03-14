package xingrepo.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import xingrepo.R;
import xingrepo.model.RepoModel;

/**
 * Created by Lokesh on 22-02-2016.
 */
public class RepoAdapter extends ArrayAdapter<RepoModel> {
    public RepoAdapter(Context ctx) {
        super(ctx, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RepoModel repo = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_repo_item, parent, false);

        TextView name = (TextView) convertView.findViewById(R.id.name_textView);
        TextView desc = (TextView) convertView.findViewById(R.id.desc_textView);
        TextView login = (TextView) convertView.findViewById(R.id.login_textView);
        name.setText(repo.getName());
        desc.setText(TextUtils.isEmpty(repo.getDescription()) ? getContext().getResources().getString(R.string.no_desc) : repo.getDescription());
        login.setText(repo.getOwner().getLogin());
        if (repo.getFork()) {
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.fork_color));
        } else {
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.white));
        }
        return convertView;
    }
}
