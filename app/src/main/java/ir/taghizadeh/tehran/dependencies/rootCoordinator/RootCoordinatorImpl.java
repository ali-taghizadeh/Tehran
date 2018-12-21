package ir.taghizadeh.tehran.dependencies.rootCoordinator;

import android.app.Activity;
import android.content.Intent;

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
}
