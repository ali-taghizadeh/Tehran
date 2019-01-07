package ir.taghizadeh.tehran.dependencies.windowConfig;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

public interface WindowConfig {

    // region UI
    void setFullScreen();
    void handleHorizontalList(RecyclerView recyclerView);
    void handleVerticalList(RecyclerView recyclerView);
    void hideKeyboard(View view);
    // endregion

    // region VALIDATION
    boolean isInputValid(String input, EditText editText, String error);
    // endregion

}
