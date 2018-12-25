package ir.taghizadeh.tehran.dependencies.database;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ir.taghizadeh.tehran.models.NewPlace;

public class DatabaseImpl implements Database {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private PushListener mPushListener;
    private DataSnapshotListener mDataSnapshotListener;
    private NewPlace mNewPlace;

    public DatabaseImpl() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public void pushNewPlace(NewPlace newPlace, String location) {
        mDatabaseReference = mFirebaseDatabase.getReference().child(location);
        mDatabaseReference
                .push()
                .setValue(newPlace, (databaseError, databaseReference) -> {
                    if (mPushListener != null) {
                        mPushListener.onPushSuccessfully(databaseReference.getKey());
                    }
                });
    }

    @Override
    public void getChild(String dbLocation, String key) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child(dbLocation).child(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mNewPlace = dataSnapshot.getValue(NewPlace.class);
                    if (mDataSnapshotListener != null) {
                        mDataSnapshotListener.onSnapshotReady(mNewPlace);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void sePushListener(PushListener pushListener) {
        this.mPushListener = pushListener;
    }

    @Override
    public void setDataSnapshotListener(DataSnapshotListener dataSnapshotListener) {
        mDataSnapshotListener = dataSnapshotListener;
    }

}
