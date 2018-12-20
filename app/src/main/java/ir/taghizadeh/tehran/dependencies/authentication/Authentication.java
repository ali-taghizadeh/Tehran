package ir.taghizadeh.tehran.dependencies.authentication;

import android.net.Uri;

public interface Authentication {
    void updatePhotoURL(Uri photoURL);
    void addAuthStateListener();
    void removeAuthStateListener();
    void signOut();

    void setUsernameListener(UsernameListener mUsernameListener);
        interface UsernameListener {
        void onUsernameReady(String username);
    }
    void setPhotoURLListener(PhotoURLListener photoURLListener);
    interface PhotoURLListener {
        void onPhotoURLReady(Uri uri);
    }
}
