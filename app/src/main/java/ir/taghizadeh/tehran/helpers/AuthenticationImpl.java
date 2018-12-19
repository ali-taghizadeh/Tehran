package ir.taghizadeh.tehran.helpers;

import android.app.Activity;
import android.net.Uri;

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

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private Activity activity;
    private int rc_sign_in;

    public AuthenticationImpl(Activity activity, int RC_SIGN_IN) {
        this.activity = activity;
        this.rc_sign_in = RC_SIGN_IN;
        mFirebaseAuth = FirebaseAuth.getInstance();
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
                if (mPhotoURLListener != null)
                    mPhotoURLListener.onPhotoURLReady(mPhotoURL);
            } else {
                mUsername = ANONYMOUS;
                activity.startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
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
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null)
            firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
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
