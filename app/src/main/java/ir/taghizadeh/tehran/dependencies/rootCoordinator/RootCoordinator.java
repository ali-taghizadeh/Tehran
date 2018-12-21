package ir.taghizadeh.tehran.dependencies.rootCoordinator;

import com.google.android.gms.maps.model.LatLng;

public interface RootCoordinator {
    void handleAddUserPhoto();
    void handleAddPlace(LatLng latLng);
}
