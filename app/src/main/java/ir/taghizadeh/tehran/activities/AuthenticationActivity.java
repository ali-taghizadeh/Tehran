package ir.taghizadeh.tehran.activities;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.authentication.Authentication;

@SuppressLint("Registered")
public class AuthenticationActivity extends BaseConfigsActivity {

    private Authentication mAuthentication;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DependencyRegistry.register.inject(this);
    }

    public void configureWith(Authentication authenticationPresenter) {
        this.mAuthentication = authenticationPresenter;
    }

    public void addUsernameListener(){
        mAuthentication.setUsernameListener(username -> mUsername = username);
    }

    public void attachUsername(TextView textView){
        mAuthentication.setUsernameListener(username -> textView.setText(username.toUpperCase()));
    }

    public String getUsername(){
        return mUsername;
    }

    public void setPhoto(ImageView photo, ImageView icon){
        mAuthentication.setPhotoURLListener(uri -> {
            if (uri != null) {
                loadImage(uri.toString(), photo);
                icon.setVisibility(View.GONE);
            } else {
                loadBlank(photo);
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
