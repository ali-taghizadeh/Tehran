package ir.taghizadeh.tehran.dependencies.map;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public interface Map {

    // region MAP
    void setOnMapListener(SupportMapFragment mapFragment, MapListener mapListener);
    interface MapListener {
        void onMapReady();
    }
    void removeMapListener();
    // endregion

    // region MARKER
    void addMarker(LatLng position, String title, String snippet, int markerResId);
    void clearMap();
    // endregion

    // region CAMERA
    void startCamera(LatLng position, int zoom);
    void setOnCameraMoveListener(CameraListener cameraListener);
    interface CameraListener {
        void onCameraMoved();
    }
    void removeCameraMoveListener();
    // endregion

    // region GETTERS
    LatLng getCenterLocation();
    // endregion

}
