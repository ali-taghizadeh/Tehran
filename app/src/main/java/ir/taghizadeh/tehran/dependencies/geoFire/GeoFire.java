package ir.taghizadeh.tehran.dependencies.geoFire;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;

import java.util.Map;

public interface GeoFire {

    void pushLocation(String location, String key, LatLng latLng);
    void queryLocations(String location, LatLng latLng, double distance);

    void seLocationListener(LocationListener locationListener);
    interface LocationListener {
        void onSetLocationSuccessfully(String key);
    }
    void setOnGeoQueryReady(GeoQueryListener geoQueryListener);
    interface GeoQueryListener {
        void OnGeoQueryReady(Map<String, GeoLocation> locationMap);
    }
}
