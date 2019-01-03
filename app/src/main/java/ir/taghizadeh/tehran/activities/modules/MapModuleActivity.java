package ir.taghizadeh.tehran.activities.modules;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.subjects.BehaviorSubject;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.map.Map;
import ir.taghizadeh.tehran.helpers.Constants;

@SuppressLint("Registered")
public class MapModuleActivity extends StorageModuleActivity {

    private Map mMap;
    private BehaviorSubject<LatLng> mCameraSubject;

    // region LIFECYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DependencyRegistry.register.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setOnCameraMoveListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeMapListener();
        removeCameraMoveListener();
    }
    // endregion

    // region DEPENDENCY INJECTION
    public void configureWith(Map mapPresenter) {
        this.mMap = mapPresenter;
    }
    // endregion

    // region MAP
    public void setOnMapListener(SupportMapFragment mapFragment) {
        mMap.setOnMapListener(mapFragment, () -> startCamera(Constants.DOWNTOWN, 17));
    }

    public void removeMapListener() {
        mMap.removeMapListener();
    }
    // endregion

    // region MARKER
    public void addMarker(LatLng position, String title, String snippet, int markerResId) {
        mMap.addMarker(position, title, snippet, markerResId);
    }

    public void clearMap() {
        mMap.clearMap();
    }
    // endregion

    // region CAMERA
    public void startCamera(LatLng position, int zoom) {
        mMap.startCamera(position, zoom);
    }

    public void setOnCameraMoveListener() {
        mMap.setOnCameraMoveListener(() -> {
            mCameraSubject.onNext(getCenterLocation());
        });
    }

    public void removeCameraMoveListener() {
        mMap.removeCameraMoveListener();
    }
    // endregion

    // region GETTERS
    public BehaviorSubject<LatLng> getCameraSubject() {
        mCameraSubject = BehaviorSubject.createDefault(getCenterLocation());
        return mCameraSubject;
    }

    public LatLng getCenterLocation() {
        return mMap.getCenterLocation();
    }
    // endregion

}
