package ir.taghizadeh.tehran.dependencies.windowConfig;

import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

public interface WindowConfig {

    // region UI
    void setFullScreen();
    void handleHorizontalList(RecyclerView recyclerView);
    void handleVerticalList(RecyclerView recyclerView);
    // endregion

    // region VALIDATION
    boolean isInputValid(String input, EditText editText, String error);
    // endregion

}
