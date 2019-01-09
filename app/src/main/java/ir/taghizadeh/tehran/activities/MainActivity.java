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

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import ir.taghizadeh.tehran.activities.modules.GeoFireModuleActivity;
import ir.taghizadeh.tehran.helpers.Constants;
import ir.taghizadeh.tehran.models.NewPlace;

/**
 * <h1>MainActivity - the only entry point to the application</h1>
 * External dependencies :
 * Firebase Authentication
 * Firebase Auth UI
 * Firebase Realtime database
 * Firebase Storage
 * GeoFire
 * Google maps
 * Rxjava
 * ButterKnife
 * Glide
 * With the help of above libraries, a user can :
 * Register with email/google account.
 * Observe locations on map and refresh them by navigating around.
 * Select a location from a list below the map and see the details.
 * Like, dislike, get direction, read comments and write one.
 * Create a new location.
 * <p>
 * <b>Note:</b> The architecture of this app is somehow different.
 * Each module mentioned above has its own class, its own interface
 * and its own ACTIVITY.
 * Module activities extend each other in this order :
 * GeoFireModuleActivity -> MapModuleActivity -> StorageModuleActivity ->
 * DatabaseModuleActivity -> AuthenticationModuleActivity ->
 * BaseConfigsModuleActivity -> AppCompatActivity.
 * <p>
 * Here MainActivity extends GeoFireModuleActivity.
 * As a result this class has direct access to all the modules down to the AppCompatActivity
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-05
 */

public class MainActivity extends GeoFireModuleActivity {

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

    private List<String> mKeys = new ArrayList<>();
    private List<GeoLocation> mGeoLocations = new ArrayList<>();
    private LatLng mLatLng;

    private CompositeDisposable generalDisposable;

    // These 3 disposables are created separately, and are not added to the generalDisposable due to their different lifecycle.
    private Disposable cameraDisposable;
    private Disposable geoQueryDisposable;
    private Disposable mPutFileDisposable;

    // region HANDLE LIFECYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setOnMapListener((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map), getCenterLocation());
        setUpUI();
    }

    /**
     * When onResume is called, an observable waits for 2 secs and then it listens
     * to any camera movement to execute a query on the new location.
     * As long as the user stays in MainActivity, this listener is alive
     * and checks the camera and when user leaves the activity, listener will get disposed.
     * The first query will be executed without any camera movement.
     * It will be done with a certain LatLng which is declared in {@link Constants}.
     * <b>Note:</b> It could have been done with user live location, but as its name is Tehran
     * I wanted the app to start always from downtown!
     */
    @Override
    protected void onResume() {
        super.onResume();
        Observable.interval(1, TimeUnit.SECONDS)
                .take(2)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        MainActivity.this.getGeneralDisposable().add(disposable);
                        clearMap();
                        updateList(new ArrayList<>());
                    }
                })
                .doOnComplete(() -> {
                    disposeGeneral();
                    setOnGeoQueryReady();
                    setOnCameraMoveListener();
                    getCameraSubject()
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSubscribe(disposable -> cameraDisposable = new CompositeDisposable(disposable))
                            .doOnError(throwable -> Log.e("cameraError : ", throwable.getMessage()))
                            .doOnNext(this::queryLocations)
                            .subscribe();
                })
                .subscribe();
    }

    /**
     * Any disposable will get disposed when onPause is called
     */
    @Override
    protected void onPause() {
        super.onPause();
        disposeGeneral();
        if (cameraDisposable != null && !cameraDisposable.isDisposed()) cameraDisposable.dispose();
        if (geoQueryDisposable != null && !geoQueryDisposable.isDisposed())geoQueryDisposable.dispose();
    }
    // endregion

    // region MANAGE UI

    /**
     * Methods below are created in different ModuleActivities
     * In this app different ways of listening for value changes are used.
     * For instance attachUsername method uses interfaces for triggering value changes.
     * And on the other hand camera movements are triggered by Rx BehaviorSubject.
     * All of these tasks could have been done easily by using EventBus with much less classes,
     * but I want to have more control over these tasks manually.
     */
    private void setUpUI() {
        setFullScreen();
        attachUsername(text_main_username);
        attachUserPhoto(image_main_add_photo, image_main_icon_add_photo);
        initializeList();
    }

    private void initializeList() {
        handleHorizontalList(recyclerView_main);
        PlacesAdapter adapter = new PlacesAdapter(getNewPlacesList(), (newPlace, position) -> {
            if (mKeys != null)
                handlePlaceDetails(newPlace, mKeys.get(position), mGeoLocations.get(position).latitude, mGeoLocations.get(position).longitude);
        });
        recyclerView_main.setAdapter(adapter);
    }
    // endregion

    // region HANDLE ACTIVITY RESULTS

    /**
     * onActivityResult is called when user tries to update avatar
     * or when FirebaseUI triggers authentication updates.
     *
     * @param requestCode This value shows which one of above scenarios is happening
     * @param data        This intent includes the URI to the selected photo from device storage
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN && resultCode == RESULT_CANCELED) {
            finish();
        } else if (requestCode == Constants.RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            updateUserPhoto(data.getData());
        }
    }
    // endregion

    // region UPDATE USER PHOTO

    /**
     * First it tries to upload the photo to the Firebase Storage using StorageModuleActivity.
     * Then it listens for upload callbacks using Rx BehaviorSubject in StorageModuleActivity.
     * When it's done it tries to update users avatar using AuthenticationModuleActivity.
     * Also there is no need to worry about BehaviorSubject which is created in StorageModuleActivity
     * because it's subscribed here and when its task is done it gets disposed.
     *
     * @param selectedImageUri This is the URI of the selected photo received from onActivityResult.
     */
    private void updateUserPhoto(Uri selectedImageUri) {
        putFile(selectedImageUri, Constants.USER_AVATAR);
        getPutFileSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .skip(1)
                .doOnSubscribe(disposable -> {
                    mPutFileDisposable = new CompositeDisposable(disposable);
                    progress_main_image.setVisibility(View.VISIBLE);
                })
                .doOnError(throwable -> Log.e("putError : ", throwable.getMessage()))
                .doOnNext(uri -> {
                    updatePhotoURL(uri);
                    progress_main_image.setVisibility(View.GONE);
                    mPutFileDisposable.dispose();
                })
                .subscribe();
    }
    // endregion

    // region QUERY ON LOCATIONS AND FOR EACH LOCATION QUERY ON DATABASE

    /**
     * Whenever camera updates, this method will be called.
     * First it executes a query on database locations node using GeoFireModuleActivity.
     * it simply sends centerLocation and a predefined distance and GeoFire does its task.
     * Then a Rx BehaviorSubject gets subscribed and it listens for response.
     * Just like cameraDisposable, geoQueryDisposable will stay alive until onPause is called.
     * This is why these 3 disposables are created separately and are not added to the generalDisposable.
     * When response arrives it brings a LinkedHashMap of GeoLocations and their keys.
     * First it clears everything.
     * Then it iterates through the hashMap and for each pair, it adds the location and key to 2
     * separate arrayLists and then it executes another query on database to get the details of that
     * specific location using its key and stores details in a list which is handled in DatabaseModuleActivity.
     * The point here is that when a user creates a new place, app stores the details and the
     * location in 2 different nodes, but by a same key.
     * When iteration is done it tries to refresh the page by calling refreshPage().
     *
     * @param centerLocation This is the location of the center of the map.
     */
    private void queryLocations(LatLng centerLocation) {
        queryLocations(Constants.PLACES_LOCATION, centerLocation, Constants.DEFAULT_DISTANCE);
        getGeoQuerySubject()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> geoQueryDisposable = new CompositeDisposable(disposable))
                .doOnError(throwable -> Log.e("queryLocationError : ", throwable.getMessage()))
                .doOnNext(locationMap -> {
                    disposeGeneral();
                    clearMap();
                    clearNewPlacesList();
                    mKeys.clear();
                    mGeoLocations.clear();
                    if (!locationMap.isEmpty()) {
                        Iterator<Map.Entry<String, GeoLocation>> it = locationMap.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry<String, GeoLocation> pair = it.next();
                            mKeys.add(pair.getKey());
                            mGeoLocations.add(pair.getValue());
                            query(Constants.PLACES, pair.getKey());
                            it.remove();
                        }
                    }
                    refreshPage();
                })
                .subscribe();
    }
    // endregion

    // region WHEN QUERY IS DONE, REFRESH THE PAGE BY ADDING NEW MARKERS AND CURVES

    /**
     * This method is called when queries are executed.
     * First it waits and shows a progressBar for a period of time which is related to the number
     * of locations.
     * Then for each location it draws a curve and adds a marker which is done in MapModuleActivity.
     * While it's doing its task it makes sure that the recyclerView adapter is empty, because we
     * want to show the list of places to the user when all markers and curves are added.
     * When iteration is done, it calls updateList() with the list of places which is populated
     * before in DatabaseModuleActivity.
     */
    private void refreshPage() {
        Observable.interval(200, TimeUnit.MILLISECONDS)
                .take(mGeoLocations.size())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    progress_main.setVisibility(View.VISIBLE);
                    getGeneralDisposable().add(disposable);
                })
                .doOnComplete(() -> progress_main.setVisibility(View.GONE))
                .delay(mGeoLocations.size() * 200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    updateList(new ArrayList<>());
                    getGeneralDisposable().add(disposable);
                })
                .doOnNext(input -> {
                    mLatLng = new LatLng(mGeoLocations.get(input.intValue()).latitude, mGeoLocations.get(input.intValue()).longitude);
                    drawCurve(getCenterLocation(), mLatLng);
                    addMarker(mLatLng, "", "", R.drawable.ic_location);
                })
                .doOnError(throwable -> Log.e("refreshPageError : ", throwable.getMessage()))
                .doOnComplete(() -> {
                    updateList(getNewPlacesList());
                    disposeGeneral();
                })
                .subscribe();
    }
    // endregion

    // region WHEN MARKERS ARE ADDED, UPDATE THE LIST
    private void updateList(List<NewPlace> newPlaces) {
        recyclerView_main.scheduleLayoutAnimation();
        PlacesAdapter adapter = (PlacesAdapter) recyclerView_main.getAdapter();
        assert adapter != null;
        adapter.newPlaces = newPlaces;
        adapter.notifyDataSetChanged();
    }
    // endregion

    // region HANDLE CLICK EVENTS
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
    // endregion

    // region CREATE AND CLEAR DISPOSABLES
    private CompositeDisposable getGeneralDisposable() {
        if (generalDisposable == null || generalDisposable.isDisposed())
            generalDisposable = new CompositeDisposable();
        return generalDisposable;
    }

    private void disposeGeneral() {
        if (generalDisposable != null && !generalDisposable.isDisposed())
            generalDisposable.clear();
        progress_main.setVisibility(View.GONE);
    }
    // endregion

}
