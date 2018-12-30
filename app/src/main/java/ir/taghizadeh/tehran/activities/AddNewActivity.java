package ir.taghizadeh.tehran.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.database.Database;
import ir.taghizadeh.tehran.dependencies.geoFire.GeoFire;
import ir.taghizadeh.tehran.dependencies.map.Map;
import ir.taghizadeh.tehran.dependencies.storage.Storage;
import ir.taghizadeh.tehran.helpers.Constants;
import ir.taghizadeh.tehran.models.NewPlace;

public class AddNewActivity extends AuthenticationActivity {

    @BindView(R.id.image_add_new_add_photo)
    ShapedImageView image_add_new_add_photo;
    @BindView(R.id.image_add_new_icon_add_photo)
    ImageView image_add_new_icon_add_photo;
    @BindView(R.id.edittext_add_new_title)
    TextInputEditText edittext_add_new_title;
    @BindView(R.id.edittext_add_new_description)
    TextInputEditText edittext_add_new_description;
    private Map mMap;
    private Storage mStorage;
    private Database mDatabase;
    private GeoFire mGeoFire;
    private LatLng mLatLng;
    private String mTitle;
    private String mDescription;
    private String mPhotoUri = "";
    private Disposable mTitleDisposable;
    private Disposable mDescriptionDisposable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        ButterKnife.bind(this);
        DependencyRegistry.register.inject(this);
    }

    public void configureWith(Map map, Storage storage, Database database, GeoFire geoFire) {
        this.mMap = map;
        this.mStorage = storage;
        this.mDatabase = database;
        this.mGeoFire = geoFire;
        setUpUI();
    }

    private void setUpUI() {
        hideStatusBar();
        getLatLng();
        attachMap(mLatLng, mTitle, mDescription);
        mTitleDisposable = textChangeListener(edittext_add_new_title);
        mDescriptionDisposable = textChangeListener(edittext_add_new_description);
    }

    private void attachMap(LatLng latLng, String title, String description) {
        mMap.setOnMapListener(() -> {
            mMap.addMarker(latLng, title, description);
            mMap.startCamera(latLng, 15);
        });
    }

    private void updateMap() {
        mMap.clearMap();
        mMap.addMarker(mLatLng, mTitle, mDescription);
    }

    private void getLatLng() {
        Bundle bundle = getIntent().getParcelableExtra("bundle");
        mLatLng = bundle.getParcelable("location");
    }

    private Disposable textChangeListener(TextInputEditText editText) {
        return RxTextView.textChanges(editText)
                .debounce(600, TimeUnit.MILLISECONDS)
                .filter(changes -> {
                    assert changes != null;
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(character -> {
                    if (editText == edittext_add_new_title)
                        mTitle = character.toString();
                    else
                        mDescription = character.toString();
                    updateMap();
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            mStorage.putFile(selectedImageUri, Constants.PLACES);
            mStorage.setonFileUploadedSuccessfully(uri -> {
                mPhotoUri = uri.toString();
                loadImage(mPhotoUri, image_add_new_add_photo);
                image_add_new_icon_add_photo.setVisibility(View.GONE);
            });
        }
    }

    @OnClick(R.id.image_add_new_add_photo)
    void addNewPhoto() {
        handleAddPhoto();
    }

    @OnClick(R.id.button_add_new_save)
    void save() {
        if (mTitle.equals("")) {
            edittext_add_new_title.setError("Pick a title");
        } else if (mDescription.equals("")) {
            edittext_add_new_description.setError("Pick a description");
        } else {
            mDatabase.pushNewPlace(createNewPlace(), Constants.PLACES);
            mDatabase.setPushListener(key -> {
                mGeoFire.pushLocation(Constants.PLACES_LOCATION, key, mLatLng);
                mGeoFire.seLocationListener(key1 -> {
                    dismiss();
                });
            });
        }
    }

    private NewPlace createNewPlace() {
        return new NewPlace(getUsername(), mTitle, mDescription, mPhotoUri, getUserPhoto(), 0, 0);
    }

    @OnClick(R.id.button_add_new_discard)
    void dismiss() {
        mTitleDisposable.dispose();
        mDescriptionDisposable.dispose();
        finish();
    }
}
