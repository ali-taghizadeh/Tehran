package ir.taghizadeh.tehran.helpers;

import android.app.Activity;
import android.view.WindowManager;

public class StatusBarHelper {
    public StatusBarHelper(Activity activity) {
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
    }
}
