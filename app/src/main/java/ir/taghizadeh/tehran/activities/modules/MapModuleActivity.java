package ir.taghizadeh.tehran.activities.modules;

import android.annotation.SuppressLint;
import android.os.Bundle;

import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.map.Map;

@SuppressLint("Registered")
public class MapModuleActivity extends DatabaseModuleActivity {

    private Map mMapPresenter;

    // region LIFECYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DependencyRegistry.register.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    // endregion

    // region DEPENDENCY INJECTION
    public void configureWith(Map mapPresenter) {
        this.mMapPresenter = mapPresenter;
    }
    // endregion
}
