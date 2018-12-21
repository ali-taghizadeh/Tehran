package ir.taghizadeh.tehran.dependencies.map;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

public interface Map {
    void addMarker(LatLng position, String title, String snippet, BitmapDescriptor bitmapDescriptor);
    void startCamera(LatLng position);
    LatLng getCenterLocation();
}
