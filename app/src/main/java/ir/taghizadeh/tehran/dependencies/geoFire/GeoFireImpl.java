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

public class GeoFireImpl implements GeoFire {


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private com.firebase.geofire.GeoFire mGeoFire;
    private GeoQuery mGeoQuery;

    private LocationListener mLocationListener;
    private GeoQueryListener mGeoQueryListener;

    private Map<String, GeoLocation> mLocations;


    public GeoFireImpl() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mLocations = new LinkedHashMap<>();
    }

    @Override
    public void pushLocation(String location, String key, LatLng latLng) {
        mDatabaseReference = mFirebaseDatabase.getReference().child(location);
        mGeoFire = new com.firebase.geofire.GeoFire(mDatabaseReference);
        mGeoFire.setLocation(key, new GeoLocation(latLng.latitude, latLng.longitude), (key1, error) -> {
            if (mLocationListener != null)
                mLocationListener.onSetLocationSuccessfully(key1);
        });
    }

    @Override
    public void queryLocations(String location, LatLng latLng, double distance) {
        mDatabaseReference = mFirebaseDatabase.getReference().child(location);
        mGeoFire = new com.firebase.geofire.GeoFire(mDatabaseReference);
        mGeoQuery = mGeoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), distance);
        mLocations.clear();
        mGeoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
                mLocations.put(dataSnapshot.getKey(), location);
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
    public void seLocationListener(LocationListener locationListener) {
        this.mLocationListener = locationListener;
    }
}
