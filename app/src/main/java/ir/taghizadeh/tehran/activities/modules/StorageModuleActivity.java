package ir.taghizadeh.tehran.activities.modules;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import io.reactivex.subjects.BehaviorSubject;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.storage.Storage;

@SuppressLint("Registered")
public class StorageModuleActivity extends DatabaseModuleActivity{

    private Storage mStoragePresenter;
    private Uri mUri = Uri.EMPTY;
    private BehaviorSubject<Uri> mPutFileSubject;

    // region LIFECYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DependencyRegistry.register.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPutListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removePutListener();
    }
    // endregion

    // region DEPENDENCY INJECTION
    public void configureWith(Storage storagePresenter) {
        this.mStoragePresenter = storagePresenter;
    }
    // endregion

    // region PUT FILE
    public void putFile(Uri uri, String location){
        mStoragePresenter.putFile(uri, location);
    }

    public void setPutListener() {
        mStoragePresenter.setPutListener(uri -> {
            mUri = uri;
            mPutFileSubject.onNext(mUri);
        });
    }

    public void removePutListener(){
        mStoragePresenter.removePutListener();
    }
    // endregion

    // region GETTERS
    public BehaviorSubject<Uri> getPutFileSubject() {
        mPutFileSubject = BehaviorSubject.createDefault(mUri);
        return mPutFileSubject;
    }
    // endregion

}
