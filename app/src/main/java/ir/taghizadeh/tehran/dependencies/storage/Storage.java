package ir.taghizadeh.tehran.dependencies.storage;

import android.net.Uri;

public interface Storage {
    void putFile(Uri uri, String location);
    void setonFileUploadedSuccessfully(FileUploadListener fileUploadedListener);
    interface FileUploadListener {
        void onPhotoURLReady(Uri uri);
    }
}