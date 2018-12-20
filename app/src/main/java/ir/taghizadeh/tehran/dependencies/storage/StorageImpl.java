package ir.taghizadeh.tehran.dependencies.storage;

import android.app.Activity;
import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StorageImpl implements Storage{

    private FirebaseStorage mFirebaseStorage;
    private Activity activity;
    private FileUploadListener mFileUploadListener;

    public StorageImpl(Activity activity) {
        this.activity = activity;
        mFirebaseStorage = FirebaseStorage.getInstance();
    }

    @Override
    public void putFile(Uri uri, String location) {
        StorageReference mStorageReference = mFirebaseStorage.getReference().child(location);
        StorageReference mUserPhoto = mStorageReference.child(uri.getLastPathSegment());
        mUserPhoto.putFile(uri).addOnSuccessListener(activity, taskSnapshot -> {
            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!urlTask.isSuccessful()) ;
            Uri mDownloadUri = urlTask.getResult();
            if (mFileUploadListener != null)
                mFileUploadListener.onPhotoURLReady(mDownloadUri);
        });
    }

    @Override
    public void setonFileUploadedSuccessfully(FileUploadListener fileUploadedListener) {
        this.mFileUploadListener = fileUploadedListener;
    }
}
