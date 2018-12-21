package ir.taghizadeh.tehran.dependencies;

import ir.taghizadeh.tehran.activities.MainActivity;
import ir.taghizadeh.tehran.dependencies.authentication.Authentication;
import ir.taghizadeh.tehran.dependencies.authentication.AuthenticationImpl;
import ir.taghizadeh.tehran.dependencies.glide.Glide;
import ir.taghizadeh.tehran.dependencies.glide.GlideImpl;
import ir.taghizadeh.tehran.dependencies.map.Map;
import ir.taghizadeh.tehran.dependencies.map.MapImpl;
import ir.taghizadeh.tehran.dependencies.rootCoordinator.RootCoordinator;
import ir.taghizadeh.tehran.dependencies.rootCoordinator.RootCoordinatorImpl;
import ir.taghizadeh.tehran.dependencies.storage.Storage;
import ir.taghizadeh.tehran.dependencies.storage.StorageImpl;
import ir.taghizadeh.tehran.dependencies.windowConfig.WindowConfig;
import ir.taghizadeh.tehran.dependencies.windowConfig.WindowConfigImpl;
import ir.taghizadeh.tehran.helpers.Constants;

public class DependencyRegistry {

    public static DependencyRegistry register = new DependencyRegistry();

    public void inject(MainActivity activity) {

        Authentication authenticationPresenter = new AuthenticationImpl(activity, Constants.RC_SIGN_IN);
        Map mapPresenter = new MapImpl(activity);
        WindowConfig windowConfigPresenter = new WindowConfigImpl(activity);
        Storage storagePresenter = new StorageImpl(activity);
        Glide glidePresenter = new GlideImpl(activity);
        RootCoordinator rootCoordinatorPresenter = new RootCoordinatorImpl(activity);

        activity.configureWith(authenticationPresenter,
                storagePresenter,
                mapPresenter,
                windowConfigPresenter,
                glidePresenter,
                rootCoordinatorPresenter);
    }
}
