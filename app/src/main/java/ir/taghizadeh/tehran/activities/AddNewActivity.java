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
import ir.taghizadeh.tehran.activities.modules.GeoFireModuleActivity;
import ir.taghizadeh.tehran.helpers.Constants;
import ir.taghizadeh.tehran.models.NewPlace;

/**
 * <h1>AddNewActivity</h1>
 *
 * When user clicks green location button in the center of screen, this
 * activity pops up.
 * To create a new place, writing a title and a brief description is necessary, but
 * adding a photo is an option.
 * Just like MainActivity, this Activity needs all dependencies.
 * As a result, it extends GeoFireModuleActivity.
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-06
 */

public class AddNewActivity extends GeoFireModuleActivity {

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

    private LatLng mLatLng;
    private String mTitle = "";
    private String mDescription = "";
    private String mPhotoUri = "";

    private CompositeDisposable compositeDisposable;

    // region HANDLE LIFECYCLE
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        ButterKnife.bind(this);
        setUpUI();
    }

    /**
     * When onResume is called, 2 disposables related to our 2 textInputs add to a compositeDisposable
     */
    @Override
    protected void onResume() {
        super.onResume();
        seLocationListener();
        getGeneralDisposable().add(updateMarker(edittext_add_new_title));
        getGeneralDisposable().add(updateMarker(edittext_add_new_description));
    }

    /**
     * Any disposable get disposed when this method is called
     */
    @Override
    protected void onPause() {
        super.onPause();
        disposeGeneral();
    }
    // endregion

    // region MANAGE UI
    private void setUpUI() {
        setFullScreen();
        getLatLng();
        attachMap(mLatLng, mTitle, mDescription);
    }

    private void attachMap(LatLng latLng, String title, String description) {
        setOnMapListener((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map), latLng);
        addMarker(latLng, title, description, R.drawable.ic_location);
        startCamera(latLng, 15);
    }

    private void getLatLng() {
        Bundle bundle = getIntent().getParcelableExtra("bundle");
        mLatLng = bundle.getParcelable("location");
    }
    // endregion

    // region TRIGGER ANY CHANGES IN TITLE OR DESCRIPTION

    /**
     * When user tries to write a title or a description, this method helps us to collect
     * characters as user is writing, and when he/she stops writing it emits the text.
     * Then using the emitted string, it redraws the marker.
     * @return Two disposables mentioned above which are added to compositeDisposable in onResume().
     */
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
                    clearMap();
                    addMarker(mLatLng, mTitle, mDescription, R.drawable.ic_location);
                });
    }
    // endregion

    // region HANDLE ACTIVITY RESULTS

    /**
     * The only scenario for calling this method is when user tries to add a photo
     * @param data This intent includes the URI to the selected photo from device storage
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            updatePlacePhoto(data.getData());
        }
    }
    // endregion

    // region UPDATE PLACE PHOTO

    /**
     * This method acts just like updating user avatar in MainActivity.
     * The only difference is that here our BehaviorSubject is added to a compositeDisposable
     * and it gets disposed when onPause is called
     * @param selectedImageUri This is the URI of the selected photo received from onActivityResult.
     */
    private void updatePlacePhoto(Uri selectedImageUri) {
        putFile(selectedImageUri, Constants.PLACES);
        getPutFileSubject()
                .observeOn(AndroidSchedulers.mainThread())
                .skip(1)
                .doOnSubscribe(disposable -> getGeneralDisposable().add(disposable))
                .doOnError(throwable -> Log.e("putError : ", throwable.getMessage()))
                .doOnNext(uri -> {
                    mPhotoUri = uri.toString();
                    loadImage(uri.toString(), image_add_new_add_photo);
                    image_add_new_icon_add_photo.setVisibility(View.GONE);
                })
                .subscribe();
    }
    // endregion

    // region HANDLE CLICK EVENTS
    @OnClick(R.id.image_add_new_add_photo)
    void addNewPhoto() {
        handleAddPhoto();
    }

    /**
     * When user taps save button, first it validates textInputs, if it was ok, then
     * it tries to create a new object of a place and pushes it to Firebase database.
     * Then it waits for 2 secs and after that pushes the location to another node
     * of Firebase database using the exact key of our pushed newPlace object.
     * In this way we have a same key in 2 different nodes about a same place,
     * and as a result, executing queries will become much easier.
     * Then a Rx BehaviorSubject gets subscribed and waits for the task to get finished,
     * and when it receives the pulse, it simply finishes the activity
     * <b>Note:</b> Before finishing the activity, onPause is called and it disposes any disposable.
     */
    @OnClick(R.id.button_add_new_save)
    void save() {
        if (isInputValid(mTitle, edittext_add_new_title, "Pick a title") &&
                isInputValid(mDescription, edittext_add_new_description, "Pick a description")) {
            pushNewPlace(new NewPlace(getUsername(), mTitle, mDescription, mPhotoUri, getUserPhoto(), 0, 0), Constants.PLACES);
            Observable.interval(1, TimeUnit.SECONDS)
                    .take(2)
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(disposable -> {
                        getGeneralDisposable().add(disposable);
                        button_add_new_save.setText("");
                        progress_add_new.setVisibility(View.VISIBLE);
                    })
                    .doOnError(throwable -> Log.e("saveError : ", throwable.getMessage()))
                    .doOnComplete(() -> {
                        pushLocation(Constants.PLACES_LOCATION, getPushedKey(), mLatLng);
                        getLocationKeySubject()
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnSubscribe(disposable -> getGeneralDisposable().add(disposable))
                                .doOnError(throwable -> Log.e("LocationKeyError : ", throwable.getMessage()))
                                .doOnNext(s -> dismiss())
                                .subscribe();
                    })
                    .subscribe();
        }
    }

    @OnClick(R.id.button_add_new_discard)
    void dismiss() {
        finish();
    }
    // endregion

    // region CREATE AND CLEAR DISPOSABLES
    private CompositeDisposable getGeneralDisposable() {
        if (compositeDisposable == null || compositeDisposable.isDisposed())
            compositeDisposable = new CompositeDisposable();
        return compositeDisposable;
    }

    private void disposeGeneral() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed())
            compositeDisposable.clear();
        progress_add_new.setVisibility(View.GONE);
    }
    //endregion

}
