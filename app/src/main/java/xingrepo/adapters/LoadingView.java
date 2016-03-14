package xingrepo.adapters;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import xingrepo.R;

/**
 * Created by Lokesh on 22-02-2016.
 * View to show the loading UI at the bottom of the listview
 */
public class LoadingView extends LinearLayout {

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.loading_view, this);
    }


}
