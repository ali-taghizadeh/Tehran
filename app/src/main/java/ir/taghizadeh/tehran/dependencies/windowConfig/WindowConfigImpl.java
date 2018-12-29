package ir.taghizadeh.tehran.dependencies.windowConfig;

import android.app.Activity;
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
}
