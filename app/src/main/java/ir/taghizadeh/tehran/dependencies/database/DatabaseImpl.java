package ir.taghizadeh.tehran.dependencies.database;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ir.taghizadeh.tehran.helpers.Constants;
import ir.taghizadeh.tehran.models.Comments;
import ir.taghizadeh.tehran.models.NewPlace;

public class DatabaseImpl implements Database {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private PushListener mPushListener;
    private PlacesDataSnapshotListener mPlacesDataSnapshotListener;
    private CommentsDataSnapshotListener mCommentsDataSnapshotListener;
    private NewPlace mNewPlace;
    private List<Comments> mCommentsList;

    // region CONSTRUCTOR
    public DatabaseImpl() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }
    // endregion

    // region PUSH
    @Override
    public void pushNewPlace(NewPlace newPlace, String dbLocation) {
        mFirebaseDatabase.getReference().child(dbLocation).push().setValue(newPlace, (databaseError, databaseReference) -> {
            if (mPushListener != null) mPushListener.onPushSuccessfully(databaseReference.getKey());
        });
    }

    @Override
    public void pushComment(Comments comments, String dbLocation, String key) {
        mFirebaseDatabase.getReference().child(dbLocation).child(key).push().setValue(comments, (databaseError, databaseReference) -> {
            if (mPushListener != null) mPushListener.onPushSuccessfully(databaseReference.getKey());
        });
    }

    @Override
    public void pushLike(int like, String dbLocation, String key) {
        mFirebaseDatabase.getReference().child(dbLocation).child(key).child(Constants.LIKES).setValue(like);
    }

    @Override
    public void pushDislike(int dislike, String dbLocation, String key) {
        mFirebaseDatabase.getReference().child(dbLocation).child(key).child(Constants.DISLIKES).setValue(dislike);
    }

    @Override
    public void setPushListener(PushListener pushListener) {
        this.mPushListener = pushListener;
    }

    @Override
    public void removePushListener() {
        this.mPushListener = null;
    }
    // endregion

    // region QUERY
    @Override
    public void query(String dbLocation, String key) {
        Query query = mDatabaseReference.child(dbLocation).child(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dbLocation.equals(Constants.PLACES)) {
                        mNewPlace = dataSnapshot.getValue(NewPlace.class);
                        if (mPlacesDataSnapshotListener != null)
                            mPlacesDataSnapshotListener.onPlacesSnapshotReady(mNewPlace);
                    } else if (dbLocation.equals(Constants.PLACES_COMMENTS)) {
                        mCommentsList = new ArrayList<>();
                        for (DataSnapshot res : dataSnapshot.getChildren())
                            mCommentsList.add(res.getValue(Comments.class));
                        if (mCommentsDataSnapshotListener != null)
                            mCommentsDataSnapshotListener.onCommentsSnapshotReady(mCommentsList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    @Override
    public void setPlacesDataSnapshotListener(PlacesDataSnapshotListener dataSnapshotListener) {
        mPlacesDataSnapshotListener = dataSnapshotListener;
    }

    @Override
    public void setCommentsDataSnapshotListener(CommentsDataSnapshotListener commentsDataSnapshotListener) {
        this.mCommentsDataSnapshotListener = commentsDataSnapshotListener;
    }

    @Override
    public void removePlacesDataSnapshotListener() {
        this.mPlacesDataSnapshotListener = null;
    }

    @Override
    public void removeCommentsDataSnapshotListener() {
        this.mCommentsDataSnapshotListener = null;
    }
    // endregion

}
