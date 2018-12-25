package ir.taghizadeh.tehran.dependencies.map;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;

public interface Map {
    void addMarker(LatLng position, String title, String snippet);
    void addMarker(GeoLocation position);
    void startCamera(LatLng position, int zoom);
    void clearMap();
    LatLng getCenterLocation();

    void setOnMapListener(MapListener mapListener);
    interface MapListener {
        void onMapReady();
    }
    void setOnCameraMoveListener(CameraListener cameraListener);
    interface CameraListener {
        void onCameraMoved();
    }
}
