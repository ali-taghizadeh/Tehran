package ir.taghizadeh.tehran.dependencies;

import ir.taghizadeh.tehran.activities.modules.AuthenticationModuleActivity;
import ir.taghizadeh.tehran.activities.AddNewActivity;
import ir.taghizadeh.tehran.activities.modules.BaseConfigsModuleActivity;
import ir.taghizadeh.tehran.activities.modules.DatabaseModuleActivity;
import ir.taghizadeh.tehran.activities.MainActivity;
import ir.taghizadeh.tehran.activities.lists.comments.CommentsAdapter;
import ir.taghizadeh.tehran.activities.lists.places.PlacesAdapter;
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

public class DependencyRegistry {

    public static DependencyRegistry register = new DependencyRegistry();

    private Glide glidePresenter;
    private Map mapPresenter;
    private Storage storagePresenter;
    private GeoFire geoFirePresenter;

    public void inject(BaseConfigsModuleActivity activity) {
        BaseConfigsModuleActivity mActivity = activity;
        WindowConfig windowConfigPresenter = new WindowConfigImpl(mActivity);
        RootCoordinator rootCoordinatorPresenter = new RootCoordinatorImpl(mActivity);
        glidePresenter = new GlideImpl(mActivity);
        mActivity.configureWith(windowConfigPresenter, rootCoordinatorPresenter, glidePresenter);
    }

    public void inject(AuthenticationModuleActivity activity) {
        Authentication authenticationPresenter = new AuthenticationImpl(activity);
        activity.configureWith(authenticationPresenter);
    }

    public void inject(DatabaseModuleActivity activity) {
        Database databasePresenter = new DatabaseImpl();
        activity.configureWith(databasePresenter);
    }

    public void inject(MainActivity activity) {
        mapPresenter = new MapImpl(activity);
        storagePresenter = new StorageImpl(activity);
        geoFirePresenter = new GeoFireImpl();
        activity.configureWith(storagePresenter, mapPresenter, geoFirePresenter);
    }

    public void inject(AddNewActivity activity) {
        mapPresenter = new MapImpl(activity);
        storagePresenter = new StorageImpl(activity);
        geoFirePresenter = new GeoFireImpl();
        activity.configureWith(mapPresenter, storagePresenter, geoFirePresenter);
    }

    public void inject(PlacesAdapter placesAdapter){
        placesAdapter.configureWith(glidePresenter);
    }

    public void inject(CommentsAdapter commentsAdapter){
        commentsAdapter.configureWith(glidePresenter);
    }
}
