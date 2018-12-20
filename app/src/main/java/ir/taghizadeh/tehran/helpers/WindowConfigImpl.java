package ir.taghizadeh.tehran.helpers;

import android.app.Activity;
import android.view.WindowManager;

public class WindowConfigImpl implements WindowConfig{
    private Activity activity;

    public WindowConfigImpl(Activity activity) {
        this.activity = activity;
    }
    @Override
    public void hideStatusBar() {
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }
}
