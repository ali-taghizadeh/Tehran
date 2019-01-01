package ir.taghizadeh.tehran.activities;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.authentication.Authentication;

@SuppressLint("Registered")
public class AuthenticationActivity extends BaseConfigsActivity {

    private Authentication mAuthentication;

    private String mUsername;
    private String mUserPhotoUrl = "";

    private TextView usernameTextView;
    private ShapedImageView userPhotoImage;
    private ImageView userPhotoIcon;

    // region LIFECYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DependencyRegistry.register.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addAuthStateListener();
        addUsernameListener();
        addUserPhotoURLListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeAuthStateListener();
        removeUsernameListener();
        removeUserPhotoURLListener();
    }
    // endregion

    // region DEPENDENCY INJECTION
    public void configureWith(Authentication authenticationPresenter) {
        this.mAuthentication = authenticationPresenter;
    }
    // endregion

    //  region USERNAME
    public void addUsernameListener() {
        mAuthentication.setUsernameListener(username -> {
            mUsername = username;
            if (usernameTextView != null) usernameTextView.setText(username.toUpperCase());
        });
    }

    public void attachUsername(TextView textView) {
        usernameTextView = textView;
    }

    public String getUsername() {
        return mUsername;
    }

    public void removeUsernameListener() {
        mAuthentication.removeUsernameListener();
    }
    // endregion

    // region USER PHOTO
    public void addUserPhotoURLListener() {
        mAuthentication.setPhotoURLListener(uri -> {
            mUserPhotoUrl = (uri == null) ? null : uri.toString();
            if (userPhotoImage != null) handleUserPhoto(mUserPhotoUrl);
        });
    }

    public void updatePhotoURL(Uri uri) {
        mAuthentication.updatePhotoURL(uri);
    }

    private void handleUserPhoto(String mUserPhotoUrl) {
        if (mUserPhotoUrl != null) {
            loadImage(mUserPhotoUrl, userPhotoImage);
            userPhotoIcon.setVisibility(View.GONE);
        } else {
            loadFromResources(userPhotoImage, "oval");
            userPhotoIcon.setVisibility(View.VISIBLE);
        }
    }

    public void attachUserPhoto(ShapedImageView image_main_add_photo, ImageView image_main_icon_add_photo) {
        userPhotoImage = image_main_add_photo;
        userPhotoIcon = image_main_icon_add_photo;
    }

    public String getUserPhoto() {
        return mUserPhotoUrl;
    }

    public void removeUserPhotoURLListener() {
        mAuthentication.removePhotoURLListener();
    }
    // endregion

    // region AUTHENTICATION
    public void addAuthStateListener(){
        mAuthentication.addAuthStateListener();
    }

    public void removeAuthStateListener(){
        mAuthentication.removeAuthStateListener();
    }

    public void signOut() {
        mAuthentication.signOut();
    }
    // endregion
}
