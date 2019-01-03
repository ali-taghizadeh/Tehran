package ir.taghizadeh.tehran.dependencies.map;

import com.google.android.gms.maps.model.LatLng;

public interface Map {

    void addMarker(LatLng position, String title, String snippet, int markerResId);
    void startCamera(LatLng position, int zoom);
    void setOnMapListener(MapListener mapListener);
    interface MapListener {
        void onMapReady();
    }
    void setOnCameraMoveListener(CameraListener cameraListener);
    interface CameraListener {
        void onCameraMoved();
    }
    void clearMap();
    LatLng getCenterLocation();
}
