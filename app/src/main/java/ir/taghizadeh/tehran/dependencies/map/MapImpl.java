package ir.taghizadeh.tehran.dependencies.map;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.firebase.geofire.GeoLocation;
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

public class MapImpl implements OnMapReadyCallback, Map {


    private GoogleMap googleMap;
    private FragmentActivity fragmentActivity;

    private MapListener mapListener;
    private CameraListener cameraListener;

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
        if (mapListener != null)
            mapListener.onMapReady();
    }

    @Override
    public void addMarker(LatLng position, String title, String snippet) {
        if (googleMap != null) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(title)
                    .snippet(snippet)
                    .icon(bitmapDescriptorFromVector(fragmentActivity)));
            marker.showInfoWindow();
        }
    }

    @Override
    public void addMarker(GeoLocation position) {
        if (googleMap != null) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(position.latitude, position.longitude))
                    .icon(bitmapDescriptorFromVector(fragmentActivity)));
            ObjectAnimator.ofFloat(marker, "alpha", 0f, 1f).setDuration(200).start();
        }
    }

    @Override
    public void startCamera(LatLng position, int zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(zoom).build();
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            googleMap.setOnCameraMoveStartedListener(i -> {
                if (cameraListener != null) {
                    cameraListener.onCameraMoved();
                }
            });

        }
    }

    @Override
    public void clearMap() {
        if (googleMap != null)
            googleMap.clear();
    }

    @Override
    public LatLng getCenterLocation() {
        return googleMap.getCameraPosition().target;
    }

    @Override
    public void setOnMapListener(MapListener mapListener) {
        this.mapListener = mapListener;
    }

    @Override
    public void setOnCameraMoveListener(CameraListener cameraListener) {
        this.cameraListener = cameraListener;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_location);
        assert background != null;
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
