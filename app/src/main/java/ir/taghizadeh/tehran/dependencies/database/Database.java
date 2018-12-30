package ir.taghizadeh.tehran.dependencies.database;

import java.util.List;

import ir.taghizadeh.tehran.models.Comments;
import ir.taghizadeh.tehran.models.NewPlace;

public interface Database {

    // region PUSH
    void pushNewPlace(NewPlace newPlace, String dbLocation);
    void pushComment(Comments comments, String dbLocation, String key);
    void pushLike(int like, String dbLocation, String key);
    void pushDislike(int dislike, String dbLocation, String key);
    void setPushListener(PushListener pushListener);
    interface PushListener {
        void onPushSuccessfully(String key);
    }
    void removePushListener();
    // endregion

    // region QUERY
    void query(String dbLocation, String key);
    void setPlacesDataSnapshotListener(PlacesDataSnapshotListener dataSnapshotListener);
    interface PlacesDataSnapshotListener {
        void onPlacesSnapshotReady(NewPlace newPlace);
    }
    void setCommentsDataSnapshotListener(CommentsDataSnapshotListener commentsDataSnapshotListener);
    interface CommentsDataSnapshotListener {
        void onCommentsSnapshotReady(List<Comments> commentsList);
    }
    void removePlacesDataSnapshotListener();
    void removeCommentsDataSnapshotListener();
    // endregion

}
