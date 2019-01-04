package ir.taghizadeh.tehran.dependencies;

import ir.taghizadeh.tehran.activities.modules.AuthenticationModuleActivity;
import ir.taghizadeh.tehran.activities.AddNewActivity;
import ir.taghizadeh.tehran.activities.modules.BaseConfigsModuleActivity;
import ir.taghizadeh.tehran.activities.modules.DatabaseModuleActivity;
import ir.taghizadeh.tehran.activities.lists.comments.CommentsAdapter;
import ir.taghizadeh.tehran.activities.lists.places.PlacesAdapter;
import ir.taghizadeh.tehran.activities.modules.GeoFireModuleActivity;
import ir.taghizadeh.tehran.activities.modules.MapModuleActivity;
import ir.taghizadeh.tehran.activities.modules.StorageModuleActivity;
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
    private GeoFire geoFirePresenter;

    public void inject(BaseConfigsModuleActivity activity) {
        WindowConfig windowConfigPresenter = new WindowConfigImpl(activity);
        RootCoordinator rootCoordinatorPresenter = new RootCoordinatorImpl(activity);
        glidePresenter = new GlideImpl(activity);
        activity.configureWith(windowConfigPresenter, rootCoordinatorPresenter, glidePresenter);
    }

    public void inject(AuthenticationModuleActivity activity) {
        Authentication authenticationPresenter = new AuthenticationImpl(activity);
        activity.configureWith(authenticationPresenter);
    }

    public void inject(DatabaseModuleActivity activity) {
        Database databasePresenter = new DatabaseImpl();
        activity.configureWith(databasePresenter);
    }

    public void inject(StorageModuleActivity activity) {
        Storage storagePresenter = new StorageImpl(activity);
        activity.configureWith(storagePresenter);
    }

    public void inject(MapModuleActivity activity) {
        Map mapPresenter = new MapImpl(activity);
        activity.configureWith(mapPresenter);
    }

    public void inject(GeoFireModuleActivity activity) {
        geoFirePresenter = new GeoFireImpl();
        activity.configureWith(geoFirePresenter);
    }

    public void inject(AddNewActivity activity) {
        geoFirePresenter = new GeoFireImpl();
        activity.configureWith(geoFirePresenter);
    }

    public void inject(PlacesAdapter placesAdapter){
        placesAdapter.configureWith(glidePresenter);
    }

    public void inject(CommentsAdapter commentsAdapter){
        commentsAdapter.configureWith(glidePresenter);
    }
}
