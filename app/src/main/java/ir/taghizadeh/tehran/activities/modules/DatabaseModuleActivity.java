package ir.taghizadeh.tehran.activities.modules;

import android.annotation.SuppressLint;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.database.Database;
import ir.taghizadeh.tehran.models.Comments;
import ir.taghizadeh.tehran.models.NewPlace;

@SuppressLint("Registered")
public class DatabaseModuleActivity extends AuthenticationModuleActivity {

    private Database mDatabase;
    private String mPushedKey;
    private List<NewPlace> mNewPlacesList = new ArrayList<>();
    private List<Comments> mCommentsList =  new ArrayList<>();

    // region LIFECYCLE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DependencyRegistry.register.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addPushListener();
        addPlacesDataSnapshotListener();
        addCommentsDataSnapshotListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removePushListener();
        removePlacesDataSnapshotListener();
        removeCommentsDataSnapshotListener();
    }
    // endregion

    // region DEPENDENCY INJECTION
    public void configureWith(Database databasePresenter) {
        this.mDatabase = databasePresenter;
    }
    // endregion

    // region PUSH
    public void pushNewPlace(NewPlace newPlace, String dbLocation){
        mDatabase.pushNewPlace(newPlace, dbLocation);
    }

    public void pushComment(Comments comments, String dbLocation, String key){
        mDatabase.pushComment(comments, dbLocation, key);
    }

    public void pushLike(int like, String dbLocation, String key){
        mDatabase.pushLike(like, dbLocation, key);
    }

    public void pushDislike(int dislike, String dbLocation, String key){
        mDatabase.pushDislike(dislike, dbLocation, key);
    }

    public void addPushListener() {
        mDatabase.setPushListener(key -> mPushedKey = key);
    }

    public void removePushListener() {
        mDatabase.removePushListener();
    }
    // endregion

    // region QUERY
    public void query(String dbLocation, String key){
        mDatabase.query(dbLocation, key);
    }

    public void addPlacesDataSnapshotListener() {
        mDatabase.setPlacesDataSnapshotListener(newPlace -> {
            mNewPlacesList.add(newPlace);
        });
    }

    public void addCommentsDataSnapshotListener() {
        mDatabase.setCommentsDataSnapshotListener(commentsList -> mCommentsList = commentsList);
    }

    public void removePlacesDataSnapshotListener() {
        mDatabase.removePlacesDataSnapshotListener();
    }

    public void removeCommentsDataSnapshotListener() {
        mDatabase.removeCommentsDataSnapshotListener();
    }
    // endregion

    // region GETTERS
    public List<NewPlace> getNewPlacesList() {
        return mNewPlacesList;
    }

    public String getPushedKey() {
        return mPushedKey;
    }

    public List<Comments> getCommentsList() {
        return mCommentsList;
    }

    public void clearNewPlacesList(){
        mNewPlacesList.clear();
    }
    // endregion
}
