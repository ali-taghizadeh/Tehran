package ir.taghizadeh.tehran.activities.modules;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;

import io.reactivex.subjects.BehaviorSubject;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.storage.Storage;

/**
 * <h1>StorageModuleActivity</h1>
 * In the order of ModuleActivities, this one is the fourth module which it handles Firebase storage.
 * It uses {@link Storage} as an interface and the main job will be done in StorageImpl class.
 * Unlike AuthenticationModuleActivity which it used interfaces to trigger value changes,
 * this module tries to notify any changes using Rx BehaviorSubject.
 * This Rx subject is created here, but it's not subscribed yet. so it's harmless!
 * It gets subscribed in MainActivity and AddNewActivity and when its task is done, it will get disposed.
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-06
 */

@SuppressLint("Registered")
public class StorageModuleActivity extends DatabaseModuleActivity{

    private Storage mStorage;
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
        this.mStorage = storagePresenter;
    }
    // endregion

    // region PUT FILE
    public void putFile(Uri uri, String location){
        mStorage.putFile(uri, location);
    }

    public void setPutListener() {
        mStorage.setPutListener(uri -> {
            mUri = uri;
            mPutFileSubject.onNext(mUri);
        });
    }

    public void removePutListener(){
        mStorage.removePutListener();
    }
    // endregion

    // region GETTERS
    public BehaviorSubject<Uri> getPutFileSubject() {
        mPutFileSubject = BehaviorSubject.createDefault(mUri);
        return mPutFileSubject;
    }
    // endregion

}
