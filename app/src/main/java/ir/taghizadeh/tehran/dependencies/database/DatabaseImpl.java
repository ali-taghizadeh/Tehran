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

/**
 * <h1>DatabaseImpl</h1>
 * <p>
 * The main logic about Firebase realtime database is done here and we can have access to
 * these methods with {@link Database} interface which is injected into DatabaseModuleActivity.
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-06
 */

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

    /**
     * Firebase database helps to push an object and when it's done we pass its key
     * to our listener to use it in the future.
     * @param newPlace This is a {@link NewPlace} object which it includes some data like title, description and ... .
     * @param dbLocation This is the name of the node in database where we want to push data.
     */
    @Override
    public void pushNewPlace(NewPlace newPlace, String dbLocation) {
        mFirebaseDatabase.getReference().child(dbLocation).push().setValue(newPlace, (databaseError, databaseReference) -> {
            if (mPushListener != null) mPushListener.onPushSuccessfully(databaseReference.getKey());
        });
    }

    /**
     * Just like what we did in pushing a new place, here we wait for a pulse that push was successfully
     * done and then we get the key again.
     * @param comments This is a {@link Comments} object which includes username, photoURL and the comment.
     * @param dbLocation This is the name of the node in database where we want to push data.
     * @param key This is the key to the right object to finally push the comments.
     */
    @Override
    public void pushComment(Comments comments, String dbLocation, String key) {
        mFirebaseDatabase.getReference().child(dbLocation).child(key).push().setValue(comments, (databaseError, databaseReference) -> {
            if (mPushListener != null) mPushListener.onPushSuccessfully(databaseReference.getKey());
        });
    }

    /**
     * Unlike pushing a comment or a new place, here we don't listen for any response. We
     * just update the UI statically.
     * @param like The number of likes that will be updated.
     * @param dbLocation This is the name of the node in database where we want to push data.
     * @param key This is the key to the right object to finally push the number of likes.
     */
    @Override
    public void pushLike(int like, String dbLocation, String key) {
        mFirebaseDatabase.getReference().child(dbLocation).child(key).child(Constants.LIKES).setValue(like);
    }

    /**
     * This method acts just like pushLike() method.
     * @param dislike The number of dislikes that will be updated.
     * @param dbLocation This is the name of the node in database where we want to push data.
     * @param key This is the key to the right object to finally push the number of dislikes.
     */
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

    /**
     * First it executes a query using the dbLocation and the key.
     * Then it listens for response. By arriving a snapshot first it checks
     * whether it's a query on places or comments and for each scenario it delivers
     * the response to our listeners and they will be used it in the future to populate a list.
     * @param dbLocation This is the name of the node in database where we want to execute a query
     * @param key This is the key to the right object to finally execute that query.
     */
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
