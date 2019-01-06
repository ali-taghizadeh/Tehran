package ir.taghizadeh.tehran.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.activities.lists.comments.CommentsAdapter;
import ir.taghizadeh.tehran.activities.modules.DatabaseModuleActivity;
import ir.taghizadeh.tehran.helpers.Constants;
import ir.taghizadeh.tehran.models.Comments;
import ir.taghizadeh.tehran.models.NewPlace;

/**
 * <h1>PlaceDetailsActivity</h1>
 *
 * When user taps on an item from list of places, this activity pops up.
 * It could have come here with just a key and use that key to execute a query and
 * populate the page by fresh details. But I just pass the details through an intent
 * and use them statically.
 *
 * In this activity, we need all dependencies except GeoFire,
 * as a result, when we look at the order of moduleActivities, DatabaseModuleActivity would be
 * the right one to extend this class with.
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-06
 */

public class PlaceDetailsActivity extends DatabaseModuleActivity {

    @BindView(R.id.text_place_details_title)
    TextView text_place_details_title;
    @BindView(R.id.text_place_details_description)
    TextView text_place_details_description;
    @BindView(R.id.text_place_details_author)
    TextView text_place_details_author;
    @BindView(R.id.text_place_details_likes)
    TextView text_place_details_likes;
    @BindView(R.id.text_place_details_dislikes)
    TextView text_place_details_dislikes;
    @BindView(R.id.image_place_details_photo)
    ImageView image_place_details_photo;
    @BindView(R.id.image_place_details_like)
    ImageView image_place_details_like;
    @BindView(R.id.image_place_details_dislike)
    ImageView image_place_details_dislike;
    @BindView(R.id.image_place_details_user_photo)
    ShapedImageView image_place_details_user_photo;
    @BindView(R.id.fab_place_details_direction)
    FloatingActionButton fab_place_details_direction;
    @BindView(R.id.recyclerView_place_details)
    RecyclerView recyclerView_place_details;
    @BindView(R.id.edittext_place_details_comment)
    TextInputEditText edittext_place_details_comment;
    @BindView(R.id.image_place_details_empty_list)
    ImageView image_place_details_empty_list;
    @BindView(R.id.text_place_details_empty_list)
    TextView text_place_details_empty_list;
    @BindView(R.id.progress_place_details)
    ProgressBar progress_place_details;

    private NewPlace mNewPlace;
    private String mKey;
    private double mLatitude;
    private double mLongitude;

    private CompositeDisposable compositeDisposable;

    // region HANDLE LIFECYCLE
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        ButterKnife.bind(this);
        setupUI();
    }

    /**
     * Any disposable get disposed when this method is called
     */
    @Override
    protected void onPause() {
        super.onPause();
        disposeGeneral();
    }
    // endregion

    // region MANAGE UI

    /**
     * It opens the bundle here and populates the page.
     * Among these data we have the key of this node in Firebase database which we
     * use it to execute a query to get the right comments about this place.
     * To sum it up, there are 3 nodes in Firebase database :
     * 1 - PLACES : where we store details of a place (title, description, likes, dislikes ...)
     * 2 - PLACES_LOCATION : where we store the location of the place.
     * 3 - PLACES_COMMENTS : where we store the comments about the place
     * Talking about a particular place, it will have a unique key to store data in each node,
     * so it helps executing queries.
     */
    private void setupUI() {
        setFullScreen();
        initializeList();
        mNewPlace = (NewPlace) getIntent().getSerializableExtra("newPlace");
        mKey = Objects.requireNonNull(getIntent().getExtras()).getString("key");
        mLatitude = getIntent().getExtras().getDouble("latitude");
        mLongitude = getIntent().getExtras().getDouble("longitude");
        text_place_details_title.setText(mNewPlace.getTitle().toUpperCase());
        text_place_details_description.setText(mNewPlace.getDescription());
        text_place_details_author.setText(mNewPlace.getUsername().toUpperCase());
        text_place_details_likes.setText(String.valueOf(mNewPlace.getLikes()));
        text_place_details_dislikes.setText(String.valueOf(mNewPlace.getDislikes()));
        if (!mNewPlace.getPhotoUrl().equals("")) loadImage(mNewPlace.getPhotoUrl(), image_place_details_photo);
        if (!mNewPlace.getUserPhotoUrl().equals("")) loadImage(mNewPlace.getUserPhotoUrl(), image_place_details_user_photo);
        attachComments();
    }

    private void initializeList() {
        handleVerticalList(recyclerView_place_details);
        recyclerView_place_details.setAdapter(new CommentsAdapter(new ArrayList<>()));
    }
    // endregion

    // region QUERY ON COMMENTS

    /**
     * When this method is called, it uses the key to execute a query on comments node.
     * it waits for 2 secs and then it updates the list of comments and when its task is
     * done, it disposes all disposables.
     */
    private void attachComments() {
        query(Constants.PLACES_COMMENTS, mKey);
        Observable.interval(1, TimeUnit.SECONDS)
                .take(2)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    getGeneralDisposable().add(disposable);
                    progress_place_details.setVisibility(View.VISIBLE);
                    recyclerView_place_details.setVisibility(View.GONE);
                    image_place_details_empty_list.setVisibility(View.GONE);
                    text_place_details_empty_list.setVisibility(View.GONE);
                    edittext_place_details_comment.setText("");
                })
                .doOnError(throwable -> Log.e("updatePageError : ", throwable.getMessage()))
                .doOnComplete(() -> {
                    progress_place_details.setVisibility(View.GONE);
                    recyclerView_place_details.setVisibility(View.VISIBLE);
                    if (getCommentsList().isEmpty()){
                        image_place_details_empty_list.setVisibility(View.VISIBLE);
                        text_place_details_empty_list.setVisibility(View.VISIBLE);
                    }else {
                        updateList();
                    }
                    disposeGeneral();
                })
                .subscribe();
    }
    // endregion

    // region WHEN QUERY IS DONE, UPDATE COMMENTS LIST
    private void updateList() {
        recyclerView_place_details.scheduleLayoutAnimation();
        CommentsAdapter adapter = (CommentsAdapter) recyclerView_place_details.getAdapter();
        assert adapter != null;
        Collections.reverse(getCommentsList());
        adapter.comments = getCommentsList();
        adapter.notifyDataSetChanged();
    }
    // endregion

    // region HANDLE CLICK EVENTS

    /**
     * When send button is clicked, first it tries to validate the textInput, if it was ok,
     * it creates a new object of comments and pushes it up with the right key to store in database.
     * Then it calls attachComments() which it ends up in executing a query again to get the most fresh data.
     */
    @OnClick(R.id.image_place_details_send)
    void addComment() {
        if (isInputValid(Objects.requireNonNull(edittext_place_details_comment.getText()).toString(), edittext_place_details_comment, "Write your comment first")){
            Comments comments = new Comments(getUsername(), getUserPhoto(), edittext_place_details_comment.getText().toString());
            pushComment(comments, Constants.PLACES_COMMENTS, mKey);
            attachComments();
        }
    }

    /**
     * The point here about like/dislike button is that a user can like/dislike a place
     * multiple times by navigating back and forward which is an issue.
     * There are some solutions to this issue for sure, like making a new node dedicated to likes/dislikes,
     * or binding the user to likes/dislikes to have more control over them.
     *
     */

    @OnClick(R.id.image_place_details_like)
    void like(){
        int likes = mNewPlace.getLikes() + 1;
        pushLike(likes, Constants.PLACES, mKey);
        text_place_details_likes.setText(String.valueOf(likes));
    }

    @OnClick(R.id.image_place_details_dislike)
    void dislike(){
        int dislikes = mNewPlace.getDislikes() + 1;
        pushDislike(dislikes, Constants.PLACES, mKey);
        text_place_details_dislikes.setText(String.valueOf(dislikes));
    }

    @OnClick(R.id.fab_place_details_direction)
    void getDirection(){
        handleGetDirection(mLatitude, mLongitude);
    }
    // endregion

    // region CREATE AND CLEAR DISPOSABLES
    private CompositeDisposable getGeneralDisposable() {
        if (compositeDisposable == null || compositeDisposable.isDisposed())
            compositeDisposable = new CompositeDisposable();
        return compositeDisposable;
    }

    private void disposeGeneral() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed())
            compositeDisposable.clear();
    }
    // endregion
}
