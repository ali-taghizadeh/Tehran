package ir.taghizadeh.tehran.dependencies.storage;

import android.app.Activity;
import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

/**
 * <h1>StorageImpl</h1>
 * <p>
 * The main logic about Firebase storage is done here and we can have access to these methods with
 * {@link Storage} interface which is injected into StorageModuleActivity.
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-07
 */

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

    /**
     * First it uploads the file to Firebase storage. Then it waits for the job to be done.
     * As it finishes its task, using our interfaces it sends a pulse that notifies that the
     * task is done. Also it passes the downloadUrl of the file through that listener.
     * @param uri The Uri of the file which we want to upload.
     * @param dbLocation The name of the node in database where we want upload our file.
     */
    @Override
    public void putFile(Uri uri, String dbLocation) {
        StorageReference mUserPhoto = mFirebaseStorage.getReference().child(dbLocation).child(Objects.requireNonNull(uri.getLastPathSegment()));
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
