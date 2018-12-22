package ir.taghizadeh.tehran.dependencies.map;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

import ir.taghizadeh.tehran.dependencies.authentication.Authentication;

public interface Map {
    void addMarker(LatLng position, String title, String snippet, BitmapDescriptor bitmapDescriptor);
    void addMarker(LatLng position);
    void startCamera(LatLng position);
    LatLng getCenterLocation();

    void setOnMapListener(MapListener mapListener);
    interface MapListener {
        void onMapReady();
    }
}
