package ir.taghizadeh.tehran.activities.modules;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.subjects.BehaviorSubject;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.map.Map;

/**
 * <h1>MapModuleActivity</h1>
 * In the order of ModuleActivities, this one is the fifth module which it handles GoogleMap.
 * It uses {@link Map} as an interface and the main job will be done in MapImpl class.
 * Just like StorageModuleActivity this module tries to notify any changes using Rx BehaviorSubject.
 * This time this subject is the LatLng of the center of the screen while camera is in idle state
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-06
 */

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
        attachCurve();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeMapListener();
        removeCameraMoveListener();
        removeCurve();
    }
    // endregion

    // region DEPENDENCY INJECTION
    public void configureWith(Map mapPresenter) {
        this.mMap = mapPresenter;
    }
    // endregion

    // region MAP
    public void setOnMapListener(SupportMapFragment mapFragment, LatLng location) {
        mMap.setOnMapListener(mapFragment, () -> startCamera(location, 17));
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

    // region CURVE
    public void attachCurve(){
        mMap.attachCurve();
    }

    public void drawCurve(LatLng from, LatLng to){
        mMap.drawCurve(from, to);
    }

    public void removeCurve(){
        mMap.removeCurve();
    }
    // endregion

    // region CAMERA
    public void startCamera(LatLng position, int zoom) {
        mMap.startCamera(position, zoom);
    }

    public void setOnCameraMoveListener() {
        mMap.setOnCameraMoveListener(() -> mCameraSubject.onNext(getCenterLocation()));
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
