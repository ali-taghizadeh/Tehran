package ir.taghizadeh.tehran.dependencies;

import ir.taghizadeh.tehran.MainActivity;
import ir.taghizadeh.tehran.dependencies.authentication.AuthenticationImpl;
import ir.taghizadeh.tehran.dependencies.authentication.Authentication;
import ir.taghizadeh.tehran.helpers.Constants;
import ir.taghizadeh.tehran.dependencies.map.Map;
import ir.taghizadeh.tehran.dependencies.map.MapImpl;
import ir.taghizadeh.tehran.dependencies.storage.Storage;
import ir.taghizadeh.tehran.dependencies.storage.StorageImpl;
import ir.taghizadeh.tehran.dependencies.windowConfig.WindowConfig;
import ir.taghizadeh.tehran.dependencies.windowConfig.WindowConfigImpl;

public class DependencyRegistry {

    public static  DependencyRegistry register = new DependencyRegistry();

    public void inject(MainActivity activity) {

        Authentication authenticationPresenter = new AuthenticationImpl(activity, Constants.RC_SIGN_IN);
        Map mapPresenter = new MapImpl(activity);
        WindowConfig windowConfigPresenter = new WindowConfigImpl(activity);
        Storage storagePresenter = new StorageImpl(activity);

        activity.configureWith(authenticationPresenter, storagePresenter,mapPresenter, windowConfigPresenter);
    }
}
