package ir.taghizadeh.tehran.dependencies.windowConfig;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * <h1>RootCoordinatorImpl</h1>
 * <p>
 * Some basic logic which are related to UI are done here and we can have access to these methods with
 * {@link WindowConfig} interface which is injected into BaseConfigsModuleActivity.
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-07
 */

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

    @Override
    public void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
