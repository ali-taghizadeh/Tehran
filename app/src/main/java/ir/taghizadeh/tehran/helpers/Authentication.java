package ir.taghizadeh.tehran.helpers;

import android.net.Uri;

public interface Authentication {
    void updatePhotoURL(Uri photoURL);
    void addAuthStateListener();
    void removeAuthStateListener();
    void signOut();

    void setUsernameListener(UsernameListener mUsernameListener);
    void setPhotoURLListener(PhotoURLListener photoURLListener);

    interface UsernameListener {
        void onUsernameReady(String username);
    }
    interface PhotoURLListener {
        void onPhotoURLReady(Uri uri);
    }
}
