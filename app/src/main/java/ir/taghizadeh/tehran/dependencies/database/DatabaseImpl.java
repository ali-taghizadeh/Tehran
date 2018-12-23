package ir.taghizadeh.tehran.dependencies.database;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ir.taghizadeh.tehran.models.NewPlace;

public class DatabaseImpl implements Database{

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private GeoFire mGeoFire;
    private GeoQuery mGeoQuery;
    private PushListener mPushListener;
    private LocationListener mLocationListener;

    public DatabaseImpl() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void pushNewPlace(NewPlace newPlace, String location) {
        mDatabaseReference = mFirebaseDatabase.getReference().child(location);
        mDatabaseReference
                .push()
                .setValue(newPlace, (databaseError, databaseReference) -> {
                    if (mPushListener != null){
                        mPushListener.onPushSuccessfully(databaseReference.getKey());
                    }
                });
    }

    @Override
    public void pushLocation(String location, String key, LatLng latLng) {
        mDatabaseReference = mFirebaseDatabase.getReference().child(location);
        mGeoFire = new GeoFire(mDatabaseReference);
        mGeoFire.setLocation(key, new GeoLocation(latLng.latitude, latLng.longitude), (key1, error) -> {
            if (mLocationListener != null)
                mLocationListener.onSetLocationSuccessfully(key1);
        });
    }

    @Override
    public void queryLocations(String location, LatLng latLng, double distance) {
        mDatabaseReference = mFirebaseDatabase.getReference().child(location);
        mGeoFire = new GeoFire(mDatabaseReference);
        mGeoQuery = mGeoFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), distance);
    }

    @Override
    public void sePushListener(PushListener pushListener) {
        this.mPushListener = pushListener;
    }

    @Override
    public void seLocationListener(LocationListener locationListener) {
        this.mLocationListener = locationListener;
    }
}
