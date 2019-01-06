package ir.taghizadeh.tehran.activities.modules;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.glide.Glide;
import ir.taghizadeh.tehran.dependencies.rootCoordinator.RootCoordinator;
import ir.taghizadeh.tehran.dependencies.windowConfig.WindowConfig;
import ir.taghizadeh.tehran.models.NewPlace;

/**
 * <h1>BaseConfigsModuleActivity</h1>
 * In the order of ModuleActivities, this one is the first module and it extends AppCompatActivity.
 * It uses {@link Glide}, {@link RootCoordinator} and {@link WindowConfig} as interfaces and
 * the main job will be done in their related Impl classes.
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-06
 */

@SuppressLint("Registered")
public class BaseConfigsModuleActivity extends AppCompatActivity {

    private Glide mGlide;
    private RootCoordinator mRootCoordinator;
    private WindowConfig mWindowConfig;

    // region LIFECYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DependencyRegistry.register.inject(this);
    }
    // endregion

    // region DEPENDENCY INJECTION
    public void configureWith(WindowConfig windowConfigPresenter, RootCoordinator rootCoordinatorPresenter, Glide glidePresenter) {
        this.mWindowConfig = windowConfigPresenter;
        this.mRootCoordinator = rootCoordinatorPresenter;
        this.mGlide = glidePresenter;
    }
    // endregion

    // region UI
    public void setFullScreen(){
        mWindowConfig.setFullScreen();
    }

    public void handleHorizontalList(RecyclerView recyclerView){
        mWindowConfig.handleHorizontalList(recyclerView);
    }

    public void handleVerticalList(RecyclerView recyclerView){
        mWindowConfig.handleVerticalList(recyclerView);
    }
    // endregion

    // region VALIDATION
    public boolean isInputValid(String input, EditText editText, String error){
        return mWindowConfig.isInputValid(input, editText, error);
    }
    // endregion

    // region LOAD IMAGE FROM URL
    public void loadImage(String url, ImageView imageView){
        mGlide.loadImage(url, imageView);
    }
    // endregion

    // region LOAD IMAGE FROM RESOURCES
    public void loadFromResources(ImageView imageView, String resid){
        mGlide.loadFromResources(imageView, resid);
    }
    // endregion

    // region HANDLE INTENT
    public void handleAddPhoto(){
        mRootCoordinator.handleAddPhoto();
    }

    public void handleAddPlace(LatLng latLng){
        mRootCoordinator.handleAddPlace(latLng);
    }

    public void handlePlaceDetails(NewPlace newPlace, String key, double latitude, double longitude){
        mRootCoordinator.handlePlaceDetails(newPlace, key, latitude, longitude);
    }

    public void handleGetDirection(double latitude, double longitude){
        mRootCoordinator.handleGetDirection(latitude, longitude);
    }
    // endregion

}
