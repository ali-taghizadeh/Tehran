package ir.taghizadeh.tehran.dependencies.rootCoordinator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import ir.taghizadeh.tehran.activities.AddNewActivity;
import ir.taghizadeh.tehran.helpers.Constants;

public class RootCoordinatorImpl implements RootCoordinator{
    private Activity activity;

    public RootCoordinatorImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void handleAddUserPhoto() {
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
}
