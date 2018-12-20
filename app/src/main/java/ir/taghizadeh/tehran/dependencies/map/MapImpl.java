package ir.taghizadeh.tehran.dependencies.map;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.helpers.Constants;

public class MapImpl implements OnMapReadyCallback, Map {


    private GoogleMap googleMap;
    private BitmapDescriptor centerMarkerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
    private FragmentActivity fragmentActivity;

    public MapImpl(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
        SupportMapFragment mapFragment = (SupportMapFragment) fragmentActivity.getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(fragmentActivity, R.raw.style_json));
        addMarker(Constants.DOWNTOWN, "Downtown", "We always start from here", centerMarkerIcon);
        startCamera(Constants.DOWNTOWN);
    }

    @Override
    public void addMarker(LatLng position, String title, String snippet, BitmapDescriptor bitmapDescriptor) {
        if (googleMap != null) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(title)
                    .snippet(snippet)
                    .icon(bitmapDescriptor));
            marker.showInfoWindow();
        }
    }

    @Override
    public void startCamera(LatLng position) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(17).build();
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }
}
