package ir.taghizadeh.tehran.dependencies.geoFire;

import android.util.Log;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <h1>GeoFireImpl</h1>
 * <p>
 * The main logic about GeoFire is done here and we can have access to these methods with
 * {@link GeoFire} interface which is injected into GeoFireModuleActivity.
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-07
 */

public class GeoFireImpl implements GeoFire {


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private com.firebase.geofire.GeoFire mGeoFire;

    private LocationListener mLocationListener;
    private GeoQueryListener mGeoQueryListener;

    private Map<String, GeoLocation> mLocations;

    // region CONSTRUCTOR
    public GeoFireImpl() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mLocations = new LinkedHashMap<>();
    }
    // endregion

    // region PUSH LOCATION

    /**
     * It pushes LatLng up into Firebase database and when it's done it passes the key of that node
     * to our listener.
     * @param dbLocation This is the name of the node in database where we want to push data.
     * @param key This is the key to the right object to finally push that LatLng.
     * @param latLng This is the LatLng of a particular place that we want to push.
     */
    @Override
    public void pushLocation(String dbLocation, String key, LatLng latLng) {
        mDatabaseReference = mFirebaseDatabase.getReference().child(dbLocation);
        mGeoFire = new com.firebase.geofire.GeoFire(mDatabaseReference);
        mGeoFire.setLocation(key, new GeoLocation(latLng.latitude, latLng.longitude), (key1, error) -> {
            if (mLocationListener != null)
                mLocationListener.onSetLocationSuccessfully(key1);
        });
    }

    @Override
    public void seLocationListener(LocationListener locationListener) {
        this.mLocationListener = locationListener;
    }

    @Override
    public void removeLocationListener() {
        this.mLocationListener = null;
    }
    // endregion

    // region QUERY LOCATION

    /**
     * First it executes a query at the location that it gets.
     * Then it waits for data to get ready and when it's done, it passes the content
     * of the received snapshot as a LinkedHashMap of keys and GeoLocations to our listener.
     * @param dbLocation This is the name of the node in database where we want to execute a query
     * @param latLng This is a certain LatLng that we want to find nearest locations around it.
     * @param distance This indicates the radius around our LatLng where we want to search for locations.
     */
    @Override
    public void queryLocations(String dbLocation, LatLng latLng, double distance) {
        mDatabaseReference = mFirebaseDatabase.getReference().child(dbLocation);
        mGeoFire = new com.firebase.geofire.GeoFire(mDatabaseReference);
        GeoQuery mGeoQuery = mGeoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), distance);
        mLocations.clear();
        mGeoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
                mLocations.put(Objects.requireNonNull(dataSnapshot.getKey()), location);
            }

            @Override
            public void onDataExited(DataSnapshot dataSnapshot) {
                mLocations.remove(dataSnapshot.getKey());
            }

            @Override
            public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {

            }

            @Override
            public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {
                mGeoQueryListener.OnGeoQueryReady(mLocations);
            }

            @Override
            public void onGeoQueryReady() {
                if (mGeoQueryListener != null)mGeoQueryListener.OnGeoQueryReady(mLocations);
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.e(GeoFireImpl.class.getName(), error.getMessage());
            }
        });
    }

    @Override
    public void setOnGeoQueryReady(GeoQueryListener geoQueryListener) {
        this.mGeoQueryListener = geoQueryListener;
    }

    @Override
    public void removeGeoQueryListener() {
        this.mGeoQueryListener = null;
    }
    // endregion

}
