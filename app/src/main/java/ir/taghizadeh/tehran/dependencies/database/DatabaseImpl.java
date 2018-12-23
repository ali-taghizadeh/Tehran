package ir.taghizadeh.tehran.dependencies.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ir.taghizadeh.tehran.models.NewPlace;

public class DatabaseImpl implements Database{

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private PushListener mPushListener;

    public DatabaseImpl() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void pushNewPlace(NewPlace newPlace, String location) {
        mDatabaseReference = mFirebaseDatabase.getReference().child(location);
        mDatabaseReference
                .push()
                .setValue(newPlace, (databaseError, databaseReference) -> {
                    if (mPushListener != null){
                        mPushListener.onPushSuccessfully(databaseReference.getKey());
                    }
                });
    }

    @Override
    public void sePushListener(PushListener pushListener) {
        this.mPushListener = pushListener;
    }
}
