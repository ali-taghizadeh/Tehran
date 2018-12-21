package ir.taghizadeh.tehran.dependencies;

import ir.taghizadeh.tehran.activities.AuthenticationActivity;
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

    Authentication authenticationPresenter;
    Glide glidePresenter;
    Map mapPresenter;
    WindowConfig windowConfigPresenter;
    Storage storagePresenter;
    RootCoordinator rootCoordinatorPresenter;

    public void inject(MainActivity activity) {

        mapPresenter = new MapImpl(activity);
        storagePresenter = new StorageImpl(activity);
        rootCoordinatorPresenter = new RootCoordinatorImpl(activity);

        activity.configureWith(
                storagePresenter,
                mapPresenter,
                rootCoordinatorPresenter);
    }

    public void inject(AuthenticationActivity activity) {

        authenticationPresenter = new AuthenticationImpl(activity, Constants.RC_SIGN_IN);
        glidePresenter = new GlideImpl(activity);
        windowConfigPresenter = new WindowConfigImpl(activity);

        activity.configureWith(authenticationPresenter, glidePresenter, windowConfigPresenter);
    }
}
