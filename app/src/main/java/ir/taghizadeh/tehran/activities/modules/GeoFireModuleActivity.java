package ir.taghizadeh.tehran.activities.modules;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.subjects.BehaviorSubject;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.geoFire.GeoFire;

/**
 * <h1>GeoFireModuleActivity</h1>
 * In the order of ModuleActivities, this one is the sixth module (the last one) which it handles GeoFire.
 * It uses {@link GeoFire} as an interface and the main job will be done in GeoFireImpl class.
 * Here we have 2 BehaviorSubjects :
 * 1 - A subject that delivers the key of the pushed location
 * 2 - A subject that delivers a LinkedHashMap of keys and GeoLocations when we execute a query on locations.
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-06
 */

@SuppressLint("Registered")
public class GeoFireModuleActivity extends MapModuleActivity {

    private GeoFire mGeoFire;
    private Map<String, GeoLocation> mLocationMap = new LinkedHashMap<>();
    String mKey = "";
    private BehaviorSubject<String> mLocationKeySubject;
    private BehaviorSubject<Map<String, GeoLocation>> mGeoQuerySubject;

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
        removeGeoQueryListener();
        removeLocationListener();
    }
    // endregion

    // region DEPENDENCY INJECTION
    public void configureWith(GeoFire geoFire) {
        this.mGeoFire = geoFire;
    }
    // endregion

    // region PUSH LOCATION
    public void pushLocation(String location, String key, LatLng latLng){
        mGeoFire.pushLocation(location, key, latLng);
    }

    public void seLocationListener(){
        mGeoFire.seLocationListener(key -> {
            mKey = key;
            mLocationKeySubject.onNext(key);
        });
    }

    public void removeLocationListener(){
        mGeoFire.removeLocationListener();
    }
    // endregion

    // region QUERY LOCATION
    public void queryLocations(String location, LatLng latLng, double distance){
        mGeoFire.queryLocations(location, latLng, distance);
    }

    public void setOnGeoQueryReady(){
        mGeoFire.setOnGeoQueryReady(locationMap -> {
            mLocationMap = locationMap;
            mGeoQuerySubject.onNext(locationMap);
        });
    }

    public void removeGeoQueryListener(){
        mGeoFire.removeGeoQueryListener();
    }
    // endregion

    // region GETTERS
    public BehaviorSubject<Map<String, GeoLocation>> getGeoQuerySubject() {
        mGeoQuerySubject = BehaviorSubject.createDefault(mLocationMap);
        return mGeoQuerySubject;
    }
    public BehaviorSubject<String> getLocationKeySubject() {
        mLocationKeySubject = BehaviorSubject.createDefault(mKey);
        return mLocationKeySubject;
    }
    // endregion
}
