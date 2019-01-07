package ir.taghizadeh.tehran.dependencies.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.makesense.labs.curvefit.Curve;
import com.makesense.labs.curvefit.CurveOptions;
import com.makesense.labs.curvefit.impl.CurveManager;
import com.makesense.labs.curvefit.interfaces.OnCurveDrawnCallback;

import java.util.Arrays;
import java.util.List;

import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.helpers.Constants;

/**
 * <h1>MapImpl</h1>
 * <p>
 * The main logic about Google Maps is done here and we can have access to these methods with
 * {@link Map} interface which is injected into MapModuleActivity.
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-07
 */

public class MapImpl implements OnMapReadyCallback, Map, OnCurveDrawnCallback {


    private GoogleMap googleMap;
    private FragmentActivity fragmentActivity;
    private CurveManager curveManager;

    private MapListener mapListener;
    private CameraListener cameraListener;

    // region CONSTRUCTOR
    public MapImpl(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }
    //endregion

    // region MAP

    /** By implementing {@link OnMapReadyCallback} it waits for googleMap to
     * get ready and when it's done, first it sets a theme on map, then it
     * creates a new CurveManager to draw curves for us and finally sends a
     * signal that map is ready and we can continue our tasks.
     * @param googleMap This is an instance of googleMap when it gets ready to use.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(fragmentActivity, R.raw.style_json));
        attachCurve();
        if (mapListener != null)
            mapListener.onMapReady();
    }

    /**
     * It captures SupportMapFragment and asks to create a new instance of map with that and by doing that
     * it triggers onMapReady and passes that instance of map.
     * @param mapFragment This includes the ID of the fragment in our UI where we want to implement a map.
     */
    @Override
    public void setOnMapListener(SupportMapFragment mapFragment, MapListener mapListener) {
        if (mapFragment != null) mapFragment.getMapAsync(this);
        this.mapListener = mapListener;
    }

    @Override
    public void removeMapListener() {
        this.mapListener = null;
    }
    // endregion

    // region MARKER

    /**
     * Draws a marker with its parameters.
     * @param position The position of the marker.
     * @param title Its title
     * @param snippet A description about this location.
     * @param markerResId The id of our resource for the markers icon.
     */
    @Override
    public void addMarker(LatLng position, String title, String snippet, int markerResId) {
        if (googleMap != null) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(title)
                    .snippet(snippet)
                    .icon(bitmapDescriptorFromVector(markerResId)));
            marker.showInfoWindow();
        }
    }

    /**
     * Clears markers and curves.
     */
    @Override
    public void clearMap() {
        if (googleMap != null)
            googleMap.clear();
    }
    // endregion

    // region CURVE

    /**
     * Creates a new instance of CurveManager and makes it ready to draw curves.
     */
    @Override
    public void attachCurve() {
        if (googleMap != null){
            curveManager = new CurveManager(googleMap);
            curveManager.setOnCurveDrawnCallback(this);
        }
    }

    /**
     * Here we set some options to our curve and then it draws the curve.
     * @param from The starting LatLng
     * @param to The destination LatLng
     */
    @Override
    public void drawCurve(LatLng from, LatLng to) {
        if (googleMap != null) {
            CurveOptions curveOptions = new CurveOptions();
            curveOptions.add(from);
            curveOptions.add(to);
            curveOptions.color(ContextCompat.getColor(fragmentActivity, R.color.colorPrimaryTransparent));
            curveOptions.setAlpha(0.5f);
            curveOptions.width(3);
            List<PatternItem> pattern = Arrays.asList(new Dash(10), new Gap(8));
            curveOptions.pattern(pattern);
            curveOptions.geodesic(false);
            curveManager.drawCurveAsync(curveOptions);
        }
    }

    @Override
    public void onCurveDrawn(Curve curve) {

    }

    @Override
    public void removeCurve() {
        if (curveManager != null) {
            curveManager.unregister();
            curveManager.setOnCurveDrawnCallback(null);
            curveManager = null;
        }
    }
    // endregion

    // region CAMERA

    /**
     * First it animates the camera to a certain LatLng. Then it listens for two events:
     *
     * 1 - When camera starts moving, it simply clears the map to refresh the screen.
     *
     * 2 - When camera stops moving (idle state), it sends a signal and the app executes its
     * queries or it does some tasks with this particular position.
     * @param position This position will be the center of the screen after camera starts moving.
     * @param zoom Indicates the zoom level.
     */
    @Override
    public void startCamera(LatLng position, int zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(position).zoom(zoom).build();
        if (googleMap != null) {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            googleMap.setOnCameraMoveStartedListener(i -> clearMap());
            googleMap.setOnCameraIdleListener(() -> {
                if (cameraListener != null) {
                    cameraListener.onCameraMoved();
                }
            });
        }
    }

    @Override
    public void setOnCameraMoveListener(CameraListener cameraListener) {
        this.cameraListener = cameraListener;
    }

    @Override
    public void removeCameraMoveListener() {
        this.cameraListener = null;
    }
    // endregion

    // region GETTERS
    @Override
    public LatLng getCenterLocation() {
        return ((googleMap == null) ? Constants.DOWNTOWN : googleMap.getCameraPosition().target);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(int resId) {
        Drawable background = ContextCompat.getDrawable(fragmentActivity, resId);
        assert background != null;
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    // endregion

}
