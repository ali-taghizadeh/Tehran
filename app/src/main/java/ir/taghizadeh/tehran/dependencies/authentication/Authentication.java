package ir.taghizadeh.tehran.dependencies.authentication;

import android.net.Uri;

public interface Authentication {

    //  region USER PHOTO
    void updatePhotoURL(Uri photoURL);
    void setPhotoURLListener(PhotoURLListener photoURLListener);
    interface PhotoURLListener {
        void onPhotoURLReady(Uri uri);
    }
    void removePhotoURLListener();
    //  endregion

    //  region USERNAME
    void setUsernameListener(UsernameListener mUsernameListener);
    interface UsernameListener {
        void onUsernameReady(String username);
    }
    void removeUsernameListener();
    //  endregion

    //  region AUTHENTICATION
    void attachAuthListener();
    void addAuthStateListener();
    void removeAuthStateListener();
    void signOut();
    //  endregion

}
