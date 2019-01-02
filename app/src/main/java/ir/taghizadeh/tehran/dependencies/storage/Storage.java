package ir.taghizadeh.tehran.dependencies.storage;

import android.net.Uri;

public interface Storage {
    // region PUT FILE
    void putFile(Uri uri, String location);
    void setPutListener(PutListener fileUploadedListener);
    interface PutListener {
        void onPhotoURLReady(Uri uri);
    }
    void removePutListener();
    // endregion
}
