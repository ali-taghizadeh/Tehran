package ir.taghizadeh.tehran.dependencies.rootCoordinator;

import com.google.android.gms.maps.model.LatLng;

import ir.taghizadeh.tehran.models.NewPlace;

public interface RootCoordinator {
    void handleAddPhoto();
    void handleAddPlace(LatLng latLng);
    void handlePlaceDetails(NewPlace newPlace, String key, double latitude, double longitude);
    void handleGetDirection(double latitude, double longitude);
}
