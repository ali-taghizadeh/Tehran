package ir.taghizadeh.tehran.dependencies.windowConfig;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

public class WindowConfigImpl implements WindowConfig{

    private Activity activity;
    public WindowConfigImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void setFullScreen() {
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
    }

    @Override
    public void handleHorizontalList(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        new PagerSnapHelper().attachToRecyclerView(recyclerView);
    }
}
