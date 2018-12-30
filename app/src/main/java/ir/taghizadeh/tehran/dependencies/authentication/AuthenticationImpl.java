package ir.taghizadeh.tehran.dependencies.authentication;

import android.app.Activity;
import android.net.Uri;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Arrays;

import ir.taghizadeh.tehran.helpers.Constants;

public class AuthenticationImpl implements Authentication {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser mFirebaseUser;

    private String mUsername;
    private UsernameListener mUsernameListener;

    private Uri mPhotoURL;
    private PhotoURLListener mPhotoURLListener;

    private Activity activity;

    // region CONSTRUCTOR
    public AuthenticationImpl(Activity activity) {
        this.activity = activity;
        mFirebaseAuth = FirebaseAuth.getInstance();
        attachAuthListener();
    }
    // endregion

    //  region USER PHOTO
    @Override
    public void updatePhotoURL(Uri photoURL) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(photoURL)
                .build();
        if (mFirebaseUser != null)
            mFirebaseUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (mPhotoURLListener != null)
                        mPhotoURLListener.onPhotoURLReady(photoURL);
                }
            });
    }

    @Override
    public void setPhotoURLListener(PhotoURLListener photoURLListener) {
        this.mPhotoURLListener = photoURLListener;
    }

    @Override
    public void removePhotoURLListener() {
        mPhotoURLListener = null;
    }
//  endregion

    //  region USERNAME
    @Override
    public void setUsernameListener(UsernameListener mUsernameListener) {
        this.mUsernameListener = mUsernameListener;
    }

    @Override
    public void removeUsernameListener() {
        mUsernameListener = null;
    }
//  endregion

    //  region AUTHENTICATION
    @Override
    public void attachAuthListener() {
        mAuthStateListener = firebaseAuth -> {
            mFirebaseUser = firebaseAuth.getCurrentUser();
            if (mFirebaseUser != null) {
                mUsername = mFirebaseUser.getDisplayName();
                if (mUsernameListener != null)
                    mUsernameListener.onUsernameReady(mUsername);
                mPhotoURL = mFirebaseUser.getPhotoUrl();
                if (mPhotoURLListener != null) {
                    mPhotoURLListener.onPhotoURLReady(mPhotoURL);
                }
            } else {
                activity.startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.EmailBuilder().build()))
                                .build(),
                        Constants.RC_SIGN_IN);
            }
        };
    }

    @Override
    public void addAuthStateListener() {
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void removeAuthStateListener() {
        if (mAuthStateListener != null)
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public void signOut() {
        AuthUI.getInstance().signOut(activity);
    }
    // endregion

}
