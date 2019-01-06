package ir.taghizadeh.tehran.dependencies.windowConfig;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.EditText;

public class WindowConfigImpl implements WindowConfig {

    private Activity activity;

    // region CONSTRUCTOR
    public WindowConfigImpl(Activity activity) {
        this.activity = activity;
    }
    // endregion

    // region UI
    @Override
    public void setFullScreen() {
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void handleHorizontalList(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(manager);
        new PagerSnapHelper().attachToRecyclerView(recyclerView);
    }

    @Override
    public void handleVerticalList(RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(manager);
    }
    // endregion

    // region VALIDATION
    @Override
    public boolean isInputValid(String input, EditText editText, String error) {
        if (input.equals("")) {
            editText.setError(error);
            return false;
        }
        return true;
    }
    // endregion

}
