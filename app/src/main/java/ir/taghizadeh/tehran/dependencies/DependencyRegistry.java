package ir.taghizadeh.tehran.dependencies;

import ir.taghizadeh.tehran.activities.AuthenticationActivity;
import ir.taghizadeh.tehran.activities.AddNewActivity;
import ir.taghizadeh.tehran.activities.BaseConfigsActivity;
import ir.taghizadeh.tehran.activities.MainActivity;
import ir.taghizadeh.tehran.activities.lists.PlacesAdapter;
import ir.taghizadeh.tehran.dependencies.authentication.Authentication;
import ir.taghizadeh.tehran.dependencies.authentication.AuthenticationImpl;
import ir.taghizadeh.tehran.dependencies.database.Database;
import ir.taghizadeh.tehran.dependencies.database.DatabaseImpl;
import ir.taghizadeh.tehran.dependencies.geoFire.GeoFire;
import ir.taghizadeh.tehran.dependencies.geoFire.GeoFireImpl;
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
    Database databasePresenter;
    RootCoordinator rootCoordinatorPresenter;
    GeoFire geoFirePresenter;
    BaseConfigsActivity mActivity;

    public void inject(BaseConfigsActivity activity) {
        this.mActivity = activity;
        windowConfigPresenter = new WindowConfigImpl(mActivity);
        rootCoordinatorPresenter = new RootCoordinatorImpl(mActivity);
        glidePresenter = new GlideImpl(mActivity);
        activity.configureWith(windowConfigPresenter,rootCoordinatorPresenter, glidePresenter);
    }

    public void inject(AuthenticationActivity activity) {
        authenticationPresenter = new AuthenticationImpl(activity, Constants.RC_SIGN_IN);
        activity.configureWith(authenticationPresenter);
    }

    public void inject(MainActivity activity) {
        mapPresenter = new MapImpl(activity);
        storagePresenter = new StorageImpl(activity);
        databasePresenter = new DatabaseImpl();
        geoFirePresenter = new GeoFireImpl();
        activity.configureWith(storagePresenter, mapPresenter, databasePresenter, geoFirePresenter);
    }

    public void inject(AddNewActivity activity) {
        mapPresenter = new MapImpl(activity);
        storagePresenter = new StorageImpl(activity);
        databasePresenter = new DatabaseImpl();
        geoFirePresenter = new GeoFireImpl();
        activity.configureWith(mapPresenter, storagePresenter, databasePresenter, geoFirePresenter);
    }

    public void inject(PlacesAdapter placesAdapter){
        glidePresenter = new GlideImpl(mActivity);
        placesAdapter.configureWith(glidePresenter);
    }
}
