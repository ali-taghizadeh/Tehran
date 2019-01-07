package ir.taghizadeh.tehran.dependencies.geoFire;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;

import java.util.Map;

public interface GeoFire {

    // region PUSH LOCATION
    void pushLocation(String dbLocation, String key, LatLng latLng);
    void seLocationListener(LocationListener locationListener);
    interface LocationListener {
        void onSetLocationSuccessfully(String key);
    }
    void removeLocationListener();
    // endregion

    // region QUERY LOCATION
    void queryLocations(String dbLocation, LatLng latLng, double distance);
    void setOnGeoQueryReady(GeoQueryListener geoQueryListener);
    interface GeoQueryListener {
        void OnGeoQueryReady(Map<String, GeoLocation> locationMap);
    }
    void removeGeoQueryListener();
    // endregion

}
