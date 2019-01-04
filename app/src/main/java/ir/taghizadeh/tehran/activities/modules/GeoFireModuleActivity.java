package ir.taghizadeh.tehran.activities.modules;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;

import java.util.Map;

import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.geoFire.GeoFire;

@SuppressLint("Registered")
public class GeoFireModuleActivity extends MapModuleActivity {

    private GeoFire mGeoFire;

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
    public void configureWith(GeoFire geoFire) {
        this.mGeoFire = geoFire;
    }
    // endregion

    // region PUSH LOCATION
    public void pushLocation(String location, String key, LatLng latLng){
        mGeoFire.pushLocation(location, key, latLng);
    }

    public void seLocationListener(){
        mGeoFire.seLocationListener(new GeoFire.LocationListener() {
            @Override
            public void onSetLocationSuccessfully(String key) {

            }
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
        mGeoFire.setOnGeoQueryReady(new GeoFire.GeoQueryListener() {
            @Override
            public void OnGeoQueryReady(Map<String, GeoLocation> locationMap) {

            }
        });
    }

    public void removeGeoQueryListener(){
        mGeoFire.removeGeoQueryListener();
    }
    // endregion

}
