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

public class GeoFireImpl implements GeoFire {


    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private com.firebase.geofire.GeoFire mGeoFire;
    private GeoQuery mGeoQuery;

    private LocationListener mLocationListener;

    private OnDataEnteredListener mOnDataEnteredListener;
    private OnDataExitedListener mOnDataExitedListener;
    private OnDataMovedListener mOnDataMovedListener;
    private OnDataChangedListener mOnDataChangedListener;
    private GeoQueryListener mGeoQueryListener;

    public GeoFireImpl() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
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
        mGeoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location) {
                if (mOnDataEnteredListener != null)mOnDataEnteredListener.onDataEntered(dataSnapshot, location);
            }

            @Override
            public void onDataExited(DataSnapshot dataSnapshot) {
                if (mOnDataExitedListener != null)mOnDataExitedListener.onDataExited(dataSnapshot);
            }

            @Override
            public void onDataMoved(DataSnapshot dataSnapshot, GeoLocation location) {
                if (mOnDataMovedListener != null)mOnDataMovedListener.OnDataMoved(dataSnapshot, location);
            }

            @Override
            public void onDataChanged(DataSnapshot dataSnapshot, GeoLocation location) {
                if (mOnDataChangedListener != null)mOnDataChangedListener.OnDataChanged(dataSnapshot, location);
            }

            @Override
            public void onGeoQueryReady() {
                if (mGeoQueryListener != null)mGeoQueryListener.OnGeoQueryReady();
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.e("onGeoQueryError", error.getMessage());
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

    @Override
    public void setOnDataEnteredListener(OnDataEnteredListener onDataEnteredListener) {
        this.mOnDataEnteredListener = onDataEnteredListener;
    }

    @Override
    public void setOnDataExitedListener(OnDataExitedListener onDataExitedListener) {
        this.mOnDataExitedListener = onDataExitedListener;
    }

    @Override
    public void setOnDataMovedListener(OnDataMovedListener onDataMovedListener) {
        this.mOnDataMovedListener = onDataMovedListener;
    }

    @Override
    public void setOnDataChangedListener(OnDataChangedListener onDataChangedListener) {
        this.mOnDataChangedListener = onDataChangedListener;
    }
}
