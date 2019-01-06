package ir.taghizadeh.tehran.dependencies.authentication;

import android.app.Activity;
import android.net.Uri;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Arrays;

import ir.taghizadeh.tehran.helpers.Constants;

/**
 * <h1>AuthenticationImpl</h1>
 * <p>
 * The main logic about Firebase authentication is done here and we can have access to
 * these methods with {@link Authentication} interface which is injected into AuthenticationModuleActivity.
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-06
 */

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

    /**
     * By calling this method Firebase Auth, updates the user profile with a UserProfileChangeRequest
     * instance. When it's done, our listener will notify that the update is done successfully.
     *
     * @param photoURL This is the URL of the uploaded avatar in FireBase storage
     */
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

    /**
     * This method is called in the constructor.
     * First it checks authentication state. if user is valid it passes its username and
     * photoURL to our predefined interfaces to attach them to UI.
     * But if user is invalid, Firebase Auth UI comes in and starts an activity
     * and asks the user to register with email or Google account.
     * By extending AuthenticationModuleActivity this method will be called.
     */
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

    /**
     * This method is called in onResume() within AuthenticationModuleActivity
     */
    @Override
    public void addAuthStateListener() {
        if (mAuthStateListener != null)
            mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    /**
     * This method is called in onPause() within AuthenticationModuleActivity
     */
    @Override
    public void removeAuthStateListener() {
        if (mAuthStateListener != null)
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    /**
     * Auth UI helps to sign out the user with just a line of code.
     */
    @Override
    public void signOut() {
        AuthUI.getInstance().signOut(activity);
    }
    // endregion

}
