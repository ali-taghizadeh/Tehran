package ir.taghizadeh.tehran.helpers;

import android.app.Activity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class AuthenticationImpl implements Authentication {

    private static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private UsernameListener mUsernameListener;

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
                if (mUsernameListener != null)
                    mUsernameListener.onUsernameReady(mUsername);
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
}
