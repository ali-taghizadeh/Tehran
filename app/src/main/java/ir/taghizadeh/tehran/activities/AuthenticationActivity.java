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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DependencyRegistry.register.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuthentication.addAuthStateListener();
        addUsernameListener();
        addUserPhotoURLListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuthentication.removeAuthStateListener();
        removeUsernameListener();
        removeUserPhotoURLListener();
    }

    public void configureWith(Authentication authenticationPresenter) {
        this.mAuthentication = authenticationPresenter;
    }

    public void addUsernameListener() {
        mAuthentication.setUsernameListener(username -> {
            mUsername = username;
            if (usernameTextView != null) usernameTextView.setText(username.toUpperCase());
        });
    }

    public void removeUsernameListener() {
        mAuthentication.removeUsernameListener();
    }

    public void addUserPhotoURLListener() {
        mAuthentication.setPhotoURLListener(uri -> {
            mUserPhotoUrl = (uri == null) ? null : uri.toString();
            if (userPhotoImage != null)
                handleImage(mUserPhotoUrl);
        });
    }

    private void handleImage(String mUserPhotoUrl) {
        if (mUserPhotoUrl != null) {
            loadImage(mUserPhotoUrl, userPhotoImage);
            userPhotoIcon.setVisibility(View.GONE);
        } else {
            loadBlank(userPhotoImage);
            userPhotoIcon.setVisibility(View.VISIBLE);
        }
    }

    public void removeUserPhotoURLListener() {
        mAuthentication.removePhotoURLListener();
    }

    public void attachUsername(TextView textView) {
        usernameTextView = textView;
    }

    public void attachUserPhoto(ShapedImageView image_main_add_photo, ImageView image_main_icon_add_photo) {
        userPhotoImage = image_main_add_photo;
        userPhotoIcon = image_main_icon_add_photo;
    }

    public String getUsername() {
        return mUsername;
    }

    public String getUserPhoto() {
        return mUserPhotoUrl;
    }

    public void updatePhotoURL(Uri uri) {
        mAuthentication.updatePhotoURL(uri);
    }

    public void signOut() {
        mAuthentication.signOut();
    }
}
