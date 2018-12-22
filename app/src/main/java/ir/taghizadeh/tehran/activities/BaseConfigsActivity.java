package ir.taghizadeh.tehran.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.glide.Glide;
import ir.taghizadeh.tehran.dependencies.rootCoordinator.RootCoordinator;
import ir.taghizadeh.tehran.dependencies.windowConfig.WindowConfig;

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

    public void hideStatusBar(){
        mWindowConfig.hideStatusBar();
    }

    public void loadImage(String url, ImageView imageView){
        mGlide.loadImage(url, imageView);
    }

    public void loadBlank(ImageView imageView){
        mGlide.loadBlank(imageView);
    }

    public void handleAddUserPhoto(){
        mRootCoordinator.handleAddUserPhoto();
    }

    public void handleAddPlace(LatLng latLng){
        mRootCoordinator.handleAddPlace(latLng);
    }
}
