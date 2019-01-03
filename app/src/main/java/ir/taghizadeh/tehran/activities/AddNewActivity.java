package ir.taghizadeh.tehran.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.activities.modules.StorageModuleActivity;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.geoFire.GeoFire;
import ir.taghizadeh.tehran.dependencies.map.Map;
import ir.taghizadeh.tehran.helpers.Constants;
import ir.taghizadeh.tehran.models.NewPlace;

public class AddNewActivity extends StorageModuleActivity {

    @BindView(R.id.image_add_new_add_photo)
    ShapedImageView image_add_new_add_photo;
    @BindView(R.id.image_add_new_icon_add_photo)
    ImageView image_add_new_icon_add_photo;
    @BindView(R.id.edittext_add_new_title)
    TextInputEditText edittext_add_new_title;
    @BindView(R.id.edittext_add_new_description)
    TextInputEditText edittext_add_new_description;
    @BindView(R.id.progress_add_new)
    ProgressBar progress_add_new;
    @BindView(R.id.button_add_new_save)
    Button button_add_new_save;
    private Map mMap;
    private GeoFire mGeoFire;
    private LatLng mLatLng;
    private String mTitle = "";
    private String mDescription = "";
    private String mPhotoUri = "";
    private CompositeDisposable compositeDisposable;
    SupportMapFragment mapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        ButterKnife.bind(this);
        DependencyRegistry.register.inject(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    }

    public void configureWith(Map map, GeoFire geoFire) {
        this.mMap = map;
        this.mGeoFire = geoFire;
        setUpUI();
    }

    private void setUpUI() {
        setFullScreen();
        getLatLng();
        attachMap(mLatLng, mTitle, mDescription);
        getCompositeDisposable().add(updateMarker(edittext_add_new_title));
        getCompositeDisposable().add(updateMarker(edittext_add_new_description));
    }

    private void attachMap(LatLng latLng, String title, String description) {
//        mMap.setOnMapListener(() -> {
//            mMap.addMarker(latLng, title, description, R.drawable.ic_location);
//            mMap.startCamera(latLng, 15);
//        });
    }

    private void getLatLng() {
        Bundle bundle = getIntent().getParcelableExtra("bundle");
        mLatLng = bundle.getParcelable("location");
    }

    private Disposable updateMarker(TextInputEditText editText) {
        return RxTextView.textChanges(editText)
                .debounce(600, TimeUnit.MILLISECONDS)
                .filter(changes -> {
                    assert changes != null;
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e("textChangeError : ", throwable.getMessage()))
                .subscribe(character -> {
                    if (editText == edittext_add_new_title)
                        mTitle = character.toString();
                    else
                        mDescription = character.toString();
                    mMap.clearMap();
                    mMap.addMarker(mLatLng, mTitle, mDescription, R.drawable.ic_location);
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            putFile(selectedImageUri, Constants.PLACES);
            getPutFileSubject()
                    .observeOn(AndroidSchedulers.mainThread())
                    .skip(1)
                    .doOnSubscribe(disposable -> getCompositeDisposable().add(disposable))
                    .doOnError(throwable -> Log.e("pushError : ", throwable.getMessage()))
                    .doOnNext(uri -> {
                        mPhotoUri = uri.toString();
                        loadImage(uri.toString(), image_add_new_add_photo);
                        image_add_new_icon_add_photo.setVisibility(View.GONE);
                    })
                    .subscribe();
        }
    }

    @OnClick(R.id.image_add_new_add_photo)
    void addNewPhoto() {
        handleAddPhoto();
    }

    @OnClick(R.id.button_add_new_save)
    void save() {
        if (isInputValid(mTitle, edittext_add_new_title, "Pick a title") &&
                isInputValid(mDescription, edittext_add_new_description, "Pick a description")) {
            pushNewPlace(createNewPlace(), Constants.PLACES);
            handleSave();
        }
    }

    private void handleSave() {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(2)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    getCompositeDisposable().add(disposable);
                    button_add_new_save.setText("");
                    progress_add_new.setVisibility(View.VISIBLE);
                })
                .doOnError(throwable -> Log.e("updatePageError : ", throwable.getMessage()))
                .doOnComplete(() -> {
                    mGeoFire.pushLocation(Constants.PLACES_LOCATION, getPushedKey(), mLatLng);
                    mGeoFire.seLocationListener(key1 -> {
                        progress_add_new.setVisibility(View.GONE);
                        dismiss();
                        dispose();
                    });
                })
                .subscribe();
    }

    private NewPlace createNewPlace() {
        return new NewPlace(getUsername(), mTitle, mDescription, mPhotoUri, getUserPhoto(), 0, 0);
    }

    @OnClick(R.id.button_add_new_discard)
    void dismiss() {
        finish();
    }

    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null || compositeDisposable.isDisposed())
            compositeDisposable = new CompositeDisposable();
        return compositeDisposable;
    }

    private void dispose() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed())
            compositeDisposable.clear();
        progress_add_new.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
