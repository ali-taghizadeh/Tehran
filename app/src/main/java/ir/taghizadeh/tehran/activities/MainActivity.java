package ir.taghizadeh.tehran.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.activities.lists.places.PlacesAdapter;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.database.Database;
import ir.taghizadeh.tehran.dependencies.geoFire.GeoFire;
import ir.taghizadeh.tehran.dependencies.map.Map;
import ir.taghizadeh.tehran.dependencies.storage.Storage;
import ir.taghizadeh.tehran.helpers.Constants;
import ir.taghizadeh.tehran.models.NewPlace;

public class MainActivity extends AuthenticationActivity {

    @BindView(R.id.text_main_username)
    TextView text_main_username;
    @BindView(R.id.image_main_add_photo)
    ShapedImageView image_main_add_photo;
    @BindView(R.id.image_main_icon_add_photo)
    ImageView image_main_icon_add_photo;
    @BindView(R.id.recyclerView_main)
    RecyclerView recyclerView_main;

    private Storage mStorage;
    private Database mDatabase;
    private Map mMap;
    private GeoFire mGeoFire;
    private List<NewPlace> mNewPlacesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DependencyRegistry.register.inject(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void configureWith(Storage storage, Map map, Database database, GeoFire geoFire) {
        this.mMap = map;
        this.mStorage = storage;
        this.mDatabase = database;
        this.mGeoFire = geoFire;
        setUpUI();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUpUI() {
        hideStatusBar();
        attachUsername(text_main_username);
        setPhoto(image_main_add_photo, image_main_icon_add_photo);
        initializeList();
        mMap.setOnMapListener(() -> {
            mMap.startCamera(Constants.DOWNTOWN, 17);
        });
        mMap.setOnCameraMoveListener(() -> queryLocations(Constants.PLACES_LOCATION, mMap.getCenterLocation(), Constants.DEFAULT_DISTANCE));
    }

    private void initializeList() {
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_main.setLayoutManager(manager);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView_main);
        PlacesAdapter adapter = new PlacesAdapter(mNewPlacesList, this::itemTapped);
        recyclerView_main.setAdapter(adapter);
    }

    private void itemTapped(NewPlace newPlace) {
        handlePlaceDetails(newPlace);
    }

    private void updateList(List<NewPlace> newPlaces) {
        if (newPlaces.isEmpty()) recyclerView_main.setVisibility(View.GONE);
        else recyclerView_main.setVisibility(View.VISIBLE);
        this.mNewPlacesList = newPlaces;
        PlacesAdapter adapter = (PlacesAdapter) recyclerView_main.getAdapter();
        assert adapter != null;
        adapter.newPlaces = this.mNewPlacesList;
        adapter.notifyDataSetChanged();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void queryLocations(String dbLocation, LatLng centerLocation, int distance) {
        mGeoFire.queryLocations(dbLocation, centerLocation, distance);
        mGeoFire.setOnGeoQueryReady(locationMap -> {
            mMap.clearMap();
            mNewPlacesList.clear();
            if (!locationMap.isEmpty())
                locationMap.forEach((key, geoLocation) -> {
                    mMap.addMarker(geoLocation);
                    mDatabase.getChild(Constants.PLACES, key);
                });
            else updateList(mNewPlacesList);
            mDatabase.setDataSnapshotListener(newPlace -> {
                mNewPlacesList.add(newPlace);
                updateList(mNewPlacesList);
            });
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
        LatLng latLng = mMap.getCenterLocation();
        handleAddPlace(latLng);
    }
}
