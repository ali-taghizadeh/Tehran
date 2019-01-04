package ir.taghizadeh.tehran.dependencies.storage;

import android.app.Activity;
import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class StorageImpl implements Storage{

    private FirebaseStorage mFirebaseStorage;
    private Activity activity;
    private PutListener mPutListener;

    // region CONSTRUCTOR
    public StorageImpl(Activity activity) {
        this.activity = activity;
        mFirebaseStorage = FirebaseStorage.getInstance();
    }
    // endregion

    // region PUT FILE
    @Override
    public void putFile(Uri uri, String location) {
        StorageReference mUserPhoto = mFirebaseStorage.getReference().child(location).child(Objects.requireNonNull(uri.getLastPathSegment()));
        mUserPhoto.putFile(uri).addOnSuccessListener(activity, taskSnapshot -> {
            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!urlTask.isSuccessful()) ;
            Uri mDownloadUri = urlTask.getResult();
            if (mPutListener != null)
                mPutListener.onPhotoURLReady(mDownloadUri);
        });
    }

    @Override
    public void setPutListener(PutListener fileUploadedListener) {
        this.mPutListener = fileUploadedListener;
    }

    @Override
    public void removePutListener() {
        mPutListener = null;
    }
    // endregion

}
