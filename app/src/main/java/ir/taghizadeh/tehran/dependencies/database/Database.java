package ir.taghizadeh.tehran.dependencies.database;

import com.google.android.gms.maps.model.LatLng;

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
}
