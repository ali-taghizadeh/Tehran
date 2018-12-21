package ir.taghizadeh.tehran.dependencies.authentication;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Arrays;

public class AuthenticationImpl implements Authentication {

    private static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private UsernameListener mUsernameListener;
    private Uri mPhotoURL;
    private PhotoURLListener mPhotoURLListener;
    private FirebaseUser mFirebaseUser;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private Activity activity;
    private int rc_sign_in;

    public AuthenticationImpl(Activity activity, int RC_SIGN_IN) {
        this.activity = activity;
        this.rc_sign_in = RC_SIGN_IN;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        attachAuthListener();
    }

    private void attachAuthListener() {
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                mUsername = firebaseUser.getDisplayName();
                mPhotoURL = firebaseUser.getPhotoUrl();
                if (mUsernameListener != null)
                    mUsernameListener.onUsernameReady(mUsername);
                if (mPhotoURLListener != null){
                    try {
                        Log.e("attachAuthListener", mPhotoURL.toString());
                    }catch (Exception e){
                        Log.e("attachAuthListener", e.getMessage());
                    }
                    mPhotoURLListener.onPhotoURLReady(mPhotoURL);}
            } else {
                mUsername = ANONYMOUS;
                activity.startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.EmailBuilder().build()))
                                .build(),
                        rc_sign_in);
            }
        };
    }

    @Override
    public void updatePhotoURL(Uri photoURL) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(photoURL)
                .build();
        if (mFirebaseUser != null)
            mFirebaseUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    try {
                        Log.e("updatePhotoURL", photoURL.toString());
                    }catch (Exception e){
                        Log.e("updatePhotoURL", e.getMessage());
                    }
                    mPhotoURLListener.onPhotoURLReady(photoURL);
                }
            });
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

    @Override
    public void setUsernameListener(UsernameListener mUsernameListener) {
        this.mUsernameListener = mUsernameListener;
    }

    @Override
    public void setPhotoURLListener(PhotoURLListener photoURLListener) {
        this.mPhotoURLListener = photoURLListener;
    }
}
