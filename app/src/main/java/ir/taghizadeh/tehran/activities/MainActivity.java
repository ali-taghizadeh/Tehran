package ir.taghizadeh.tehran.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.map.Map;
import ir.taghizadeh.tehran.dependencies.storage.Storage;
import ir.taghizadeh.tehran.helpers.Constants;

public class MainActivity extends AuthenticationActivity {

    @BindView(R.id.text_main_username)
    TextView text_main_username;
    @BindView(R.id.image_main_add_photo)
    ShapedImageView image_main_add_photo;
    @BindView(R.id.image_main_icon_add_photo)
    ImageView image_main_icon_add_photo;

    private Storage mStorage;
    private Map mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DependencyRegistry.register.inject(this);
    }

    public void configureWith(Storage storage,
                              Map map) {
        this.mMap = map;
        this.mStorage = storage;
        setUpUI();
    }

    private void setUpUI() {
        hideStatusBar();
        attachUsername(text_main_username);
        setPhoto(image_main_add_photo, image_main_icon_add_photo);
        mMap.setOnMapListener(() -> mMap.startCamera(Constants.DOWNTOWN, 17));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Signed in canceled", Toast.LENGTH_SHORT).show();
            finish();
        } else if (requestCode == Constants.RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            mStorage.putFile(selectedImageUri, Constants.USER_AVATAR);
            mStorage.setonFileUploadedSuccessfully(this::updatePhotoURL);
        }
    }

    @OnClick(R.id.image_main_logout)
    void logOut() {
        signOut();
    }

    @OnClick(R.id.image_main_add_photo)
    void addPhoto() {
        handleAddPhoto();
    }

    @OnClick(R.id.image_main_add_place)
    void addLocation() {
        LatLng latLng = mMap.getCenterLocation();
        handleAddPlace(latLng);
    }
}
