package ir.taghizadeh.tehran.dependencies.database;

import java.util.List;

import ir.taghizadeh.tehran.models.Comments;
import ir.taghizadeh.tehran.models.NewPlace;

public interface Database {
    void pushNewPlace(NewPlace newPlace, String location);
    void getChild(String dbLocation, String key);
    void addComment(Comments comments, String dbLocation, String key);
    void like(int like, String dbLocation, String key);

    void setPushListener(PushListener pushListener);
    interface PushListener {
        void onPushSuccessfully(String key);
    }

    void setPlacesDataSnapshotListener(PlacesDataSnapshotListener dataSnapshotListener);
    interface PlacesDataSnapshotListener {
        void onPlacesSnapshotReady(NewPlace newPlace);
    }

    void setCommentsDataSnapshotListener(CommentsDataSnapshotListener commentsDataSnapshotListener);
    interface CommentsDataSnapshotListener {
        void onCommentsSnapshotReady(List<Comments> commentsList);
    }

}
