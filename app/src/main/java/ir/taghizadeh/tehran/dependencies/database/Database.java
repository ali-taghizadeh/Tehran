package ir.taghizadeh.tehran.dependencies.database;

import ir.taghizadeh.tehran.models.Comments;
import ir.taghizadeh.tehran.models.NewPlace;

public interface Database {
    void pushNewPlace(NewPlace newPlace, String location);
    void getChild(String dbLocation, String key);
    void addComment(Comments comments, String dbLocation, String key);

    void sePushListener(PushListener pushListener);
    interface PushListener {
        void onPushSuccessfully(String key);
    }

    void setDataSnapshotListener(DataSnapshotListener dataSnapshotListener);
    interface DataSnapshotListener {
        void onSnapshotReady(NewPlace newPlace);
    }

}
