package ir.taghizadeh.tehran.dependencies.database;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

import ir.taghizadeh.tehran.models.NewPlace;

public interface Database {
    void pushNewPlace(NewPlace newPlace, String location);
    void pushLocation(String location, String key, LatLng latLng);
    void queryLocations(String location, LatLng latLng, double distance);

    void sePushListener(PushListener pushListener);
    interface PushListener {
        void onPushSuccessfully(String key);
    }

    void seLocationListener(LocationListener locationListener);
    interface LocationListener {
        void onSetLocationSuccessfully(String key);
    }



    void setOnGeoQueryReady(GeoQueryListener geoQueryListener);
    interface GeoQueryListener {
        void OnGeoQueryReady();
    }
    void setOnDataEnteredListener(OnDataEnteredListener onDataEnteredListener);
    interface OnDataEnteredListener {
        void onDataEntered(DataSnapshot dataSnapshot, GeoLocation location);
    }
    void setOnDataExitedListener(OnDataExitedListener onDataExitedListener);
    interface OnDataExitedListener {
        void onDataExited(DataSnapshot dataSnapshot);
    }
    void setOnDataMovedListener(OnDataMovedListener onDataMovedListener);
    interface OnDataMovedListener {
        void OnDataMoved(DataSnapshot dataSnapshot, GeoLocation location);
    }
    void setOnDataChangedListener(OnDataChangedListener onDataChangedListener);
    interface OnDataChangedListener {
        void OnDataChanged(DataSnapshot dataSnapshot, GeoLocation location);
    }

}
