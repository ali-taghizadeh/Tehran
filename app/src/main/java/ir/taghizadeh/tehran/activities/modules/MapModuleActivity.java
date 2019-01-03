package ir.taghizadeh.tehran.activities.modules;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.map.Map;

@SuppressLint("Registered")
public class MapModuleActivity extends DatabaseModuleActivity {

    private Map mMap;

    // region LIFECYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DependencyRegistry.register.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    // endregion

    // region DEPENDENCY INJECTION
    public void configureWith(Map mapPresenter) {
        this.mMap = mapPresenter;
    }
    // endregion

    public void addMarker(LatLng position, String title, String snippet, int markerResId){
        mMap.addMarker(position, title, snippet, markerResId);
    }

    public void startCamera(LatLng position, int zoom){
        mMap.startCamera(position, zoom);
    }

    public void clearMap(){
        mMap.clearMap();
    }

    public LatLng getCenterLocation(){
        return mMap.getCenterLocation();
    }

    public void setOnMapListener(){
        mMap.setOnMapListener(() -> {});
    }

    public void setOnCameraMoveListener(){
        mMap.setOnCameraMoveListener(() -> {});
    }
}
