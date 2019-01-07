package ir.taghizadeh.tehran.dependencies.rootCoordinator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import ir.taghizadeh.tehran.activities.AddNewActivity;
import ir.taghizadeh.tehran.activities.PlaceDetailsActivity;
import ir.taghizadeh.tehran.helpers.Constants;
import ir.taghizadeh.tehran.models.NewPlace;

public class RootCoordinatorImpl implements RootCoordinator {

    private Activity activity;

    //region CONSTRUCTOR
    public RootCoordinatorImpl(Activity activity) {
        this.activity = activity;
    }
    // endregion

    // region HANDLE INTENT
    @Override
    public void handleAddPhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        activity.startActivityForResult(Intent.createChooser(intent, "Complete action using"), Constants.RC_PHOTO_PICKER);
    }

    @Override
    public void handleAddPlace(LatLng latLng) {
        Intent intent = new Intent(activity, AddNewActivity.class);
        Bundle args = new Bundle();
        args.putParcelable("location", latLng);
        intent.putExtra("bundle", args);
        activity.startActivity(intent);
    }

    @Override
    public void handlePlaceDetails(NewPlace newPlace, String key, double latitude, double longitude) {
        Intent intent = new Intent(activity, PlaceDetailsActivity.class);
        intent.putExtra("key", key);
        intent.putExtra("newPlace", newPlace);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        activity.startActivity(intent);
    }

    @Override
    public void handleGetDirection(double latitude, double longitude) {
        Uri uri = Uri.parse(String.format("google.navigation:q=%s, %s", latitude, longitude));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapIntent.setPackage("com.google.android.apps.maps");
        activity.startActivity(mapIntent);
    }
    // endregion

}
