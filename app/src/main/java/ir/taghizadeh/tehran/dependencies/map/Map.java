package ir.taghizadeh.tehran.dependencies.map;

import com.google.android.gms.maps.model.LatLng;

public interface Map {
    void addMarker(LatLng position, String title, String snippet);
    void startCamera(LatLng position, int zoom);
    void clearMap();
    LatLng getCenterLocation();

    void setOnMapListener(MapListener mapListener);
    interface MapListener {
        void onMapReady();
    }
}
