package ir.taghizadeh.tehran.helpers;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

public interface Map {
    void addMarker(LatLng position, String title, String snippet, BitmapDescriptor bitmapDescriptor);
    void startCamera(LatLng position);
}
