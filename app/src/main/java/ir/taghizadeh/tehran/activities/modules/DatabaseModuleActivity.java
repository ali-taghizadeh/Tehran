package ir.taghizadeh.tehran.activities.modules;

import android.annotation.SuppressLint;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.database.Database;
import ir.taghizadeh.tehran.models.Comments;
import ir.taghizadeh.tehran.models.NewPlace;

/**
 * <h1>DatabaseModuleActivity</h1>
 * In the order of ModuleActivities, this one is the third module which it handles Firebase database.
 * It uses {@link Database} as an interface and the main job will be done in DatabaseImpl class. But
 * there are some simple usages about this class beside being an interface. For instance when
 * we execute a query on places or comments, here it collects their snapshots when each of them get ready,
 * and in the end we can get those lists from the activities that extend this module.
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-06
 */

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
        mDatabase.setPlacesDataSnapshotListener(newPlace -> mNewPlacesList.add(newPlace));
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
