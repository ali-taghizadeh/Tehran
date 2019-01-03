package ir.taghizadeh.tehran.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.activities.lists.places.PlacesAdapter;
import ir.taghizadeh.tehran.activities.modules.MapModuleActivity;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.geoFire.GeoFire;
import ir.taghizadeh.tehran.helpers.Constants;
import ir.taghizadeh.tehran.models.NewPlace;

public class MainActivity extends MapModuleActivity {

    @BindView(R.id.text_main_username)
    TextView text_main_username;
    @BindView(R.id.image_main_add_photo)
    ShapedImageView image_main_add_photo;
    @BindView(R.id.image_main_icon_add_photo)
    ImageView image_main_icon_add_photo;
    @BindView(R.id.recyclerView_main)
    RecyclerView recyclerView_main;
    @BindView(R.id.progress_main)
    ProgressBar progress_main;
    @BindView(R.id.progress_main_image)
    ProgressBar progress_main_image;

    private GeoFire mGeoFire;
    private List<String> mKeys = new ArrayList<>();
    private List<GeoLocation> mGeoLocations = new ArrayList<>();
    private CompositeDisposable compositeDisposable;
    private CompositeDisposable cameraDisposable;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        DependencyRegistry.register.inject(this);
    }

    public void configureWith(GeoFire geoFire) {
        this.mGeoFire = geoFire;
        setUpUI();
    }

    private void setUpUI() {
        setFullScreen();
        attachUsername(text_main_username);
        attachUserPhoto(image_main_add_photo, image_main_icon_add_photo);
        initializeList();
        setOnMapListener(mapFragment);
    }

    private void initializeList() {
        handleHorizontalList(recyclerView_main);
        PlacesAdapter adapter = new PlacesAdapter(getNewPlacesList(), (newPlace, position) -> {
            if (mKeys != null)
                handlePlaceDetails(newPlace, mKeys.get(position), mGeoLocations.get(position).latitude, mGeoLocations.get(position).longitude);
        });
        recyclerView_main.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Signed in canceled", Toast.LENGTH_SHORT).show();
            finish();
        } else if (requestCode == Constants.RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            putFile(selectedImageUri, Constants.USER_AVATAR);
            getPutFileSubject()
                    .observeOn(AndroidSchedulers.mainThread())
                    .skip(1)
                    .doOnSubscribe(disposable -> {
                        getCompositeDisposable().add(disposable);
                        progress_main_image.setVisibility(View.VISIBLE);
                    })
                    .doOnError(throwable -> Log.e("pushError : ", throwable.getMessage()))
                    .doOnNext(uri -> {
                        updatePhotoURL(uri);
                        progress_main_image.setVisibility(View.GONE);
                        dispose();
                    })
                    .subscribe();
        }
    }

    private void queryLocations(LatLng centerLocation) {
        mGeoFire.queryLocations(Constants.PLACES_LOCATION, centerLocation, Constants.DEFAULT_DISTANCE);
        mGeoFire.setOnGeoQueryReady(locationMap -> {
            dispose();
            clearMap();
            clearNewPlacesList();
            mKeys.clear();
            mGeoLocations.clear();
            if (!locationMap.isEmpty()) {
                Iterator<java.util.Map.Entry<String, GeoLocation>> it = locationMap.entrySet().iterator();
                while (it.hasNext()) {
                    java.util.Map.Entry<String, GeoLocation> pair = it.next();
                    mKeys.add(pair.getKey());
                    mGeoLocations.add(pair.getValue());
                    query(Constants.PLACES, pair.getKey());
                    it.remove();
                }
            }
            refreshPage();
        });
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
        handleAddPlace(getCenterLocation());
    }

    private void refreshPage() {
        Observable.interval(200, TimeUnit.MILLISECONDS)
                .take(mGeoLocations.size())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    progress_main.setVisibility(View.VISIBLE);
                    getCompositeDisposable().add(disposable);
                })
                .doOnComplete(() -> progress_main.setVisibility(View.GONE))
                .delay(mGeoLocations.size() * 200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(input -> addMarker(new LatLng(mGeoLocations.get(input.intValue()).latitude, mGeoLocations.get(input.intValue()).longitude)
                        , "", "", R.drawable.ic_location))
                .doOnError(throwable -> Log.e("updatePageError : ", throwable.getMessage()))
                .doOnSubscribe(disposable -> {
                    updateList(new ArrayList<>());
                    getCompositeDisposable().add(disposable);
                })
                .doOnComplete(() -> {
                    updateList(getNewPlacesList());
                    dispose();
                })
                .subscribe();
    }

    private void updateList(List<NewPlace> newPlaces) {
        recyclerView_main.scheduleLayoutAnimation();
        PlacesAdapter adapter = (PlacesAdapter) recyclerView_main.getAdapter();
        assert adapter != null;
        adapter.newPlaces = newPlaces;
        adapter.notifyDataSetChanged();
    }

    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null || compositeDisposable.isDisposed())
            compositeDisposable = new CompositeDisposable();
        return compositeDisposable;
    }

    private void dispose() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed())
            compositeDisposable.clear();
        progress_main.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dispose();
        cameraDisposable.dispose();
    }

    @Override
    protected void onResume(){
        super.onResume();
        getCameraSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> cameraDisposable = new CompositeDisposable(disposable))
                .doOnError(throwable -> Log.e("cameraError : ", throwable.getMessage()))
                .doOnNext(this::queryLocations)
                .subscribe();
    }
}
