package ir.taghizadeh.tehran.activities;

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

@SuppressLint("Registered")
public class BaseConfigsActivity extends AppCompatActivity {

    private Glide mGlide;
    private RootCoordinator mRootCoordinator;
    private WindowConfig mWindowConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DependencyRegistry.register.inject(this);
    }

    public void configureWith(WindowConfig windowConfigPresenter, RootCoordinator rootCoordinatorPresenter, Glide glidePresenter) {
        this.mWindowConfig = windowConfigPresenter;
        this.mRootCoordinator = rootCoordinatorPresenter;
        this.mGlide = glidePresenter;
    }

    public void setFullScreen(){
        mWindowConfig.setFullScreen();
    }

    public void loadImage(String url, ImageView imageView){
        mGlide.loadImage(url, imageView);
    }

    public void loadFromResources(ImageView imageView, String resid){
        mGlide.loadFromResources(imageView, resid);
    }

    boolean isInputValid(String input, EditText editText, String error){
        return mWindowConfig.isInputValid(input, editText, error);
    }

    public void handleHorizontalList(RecyclerView recyclerView){
        mWindowConfig.handleHorizontalList(recyclerView);
    }

    public void handleVerticalList(RecyclerView recyclerView){
        mWindowConfig.handleVerticalList(recyclerView);
    }

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
}
