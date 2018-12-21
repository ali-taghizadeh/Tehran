package ir.taghizadeh.tehran.activities;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.authentication.Authentication;
import ir.taghizadeh.tehran.dependencies.glide.Glide;
import ir.taghizadeh.tehran.dependencies.windowConfig.WindowConfig;

@SuppressLint("Registered")
public class AuthenticationActivity extends AppCompatActivity {

    private Authentication mAuthentication;
    private Glide mGlide;
    private WindowConfig mWindowConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DependencyRegistry.register.inject(this);
    }

    public void configureWith(Authentication authenticationPresenter, Glide glide, WindowConfig windowConfig) {
        this.mAuthentication = authenticationPresenter;
        this.mGlide = glide;
        mWindowConfig = windowConfig;
    }

    public void hideStatusBar(){
        mWindowConfig.hideStatusBar();
    }

    public void setUsername(TextView textView){
        mAuthentication.setUsernameListener(username -> textView.setText(username.toUpperCase()));
    }

    public void setPhoto(ImageView photo, ImageView icon){
        mAuthentication.setPhotoURLListener(uri -> {
            if (uri != null) {
                mGlide.loadImage(uri.toString(), photo);
                icon.setVisibility(View.GONE);
            } else {
                mGlide.blankUserPhoto(photo);
                icon.setVisibility(View.VISIBLE);
            }
        });
    }

    public void updatePhotoURL(Uri uri){
        mAuthentication.updatePhotoURL(uri);
    }


    public void signOut(){
        mAuthentication.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuthentication.addAuthStateListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuthentication.removeAuthStateListener();
    }
}
