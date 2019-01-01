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
import java.util.List;
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
import ir.taghizadeh.tehran.helpers.Constants;
import ir.taghizadeh.tehran.models.Comments;
import ir.taghizadeh.tehran.models.NewPlace;

public class PlaceDetailsActivity extends DatabaseActivity {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        ButterKnife.bind(this);
        configureWith();
    }

    private void configureWith() {
        mNewPlace = (NewPlace) getIntent().getSerializableExtra("newPlace");
        mKey = getIntent().getExtras().getString("key");
        mLatitude = getIntent().getExtras().getDouble("latitude");
        mLongitude = getIntent().getExtras().getDouble("longitude");
        setFullScreen();
        attachUI();
        initializeList();
        attachComments();
    }

    private void attachUI() {
        text_place_details_title.setText(mNewPlace.getTitle().toUpperCase());
        text_place_details_description.setText(mNewPlace.getDescription());
        text_place_details_author.setText(mNewPlace.getUsername().toUpperCase());
        text_place_details_likes.setText(String.valueOf(mNewPlace.getLikes()));
        text_place_details_dislikes.setText(String.valueOf(mNewPlace.getDislikes()));
        if (!mNewPlace.getPhotoUrl().equals(""))
            loadImage(mNewPlace.getPhotoUrl(), image_place_details_photo);
        if (!mNewPlace.getUserPhotoUrl().equals(""))
        loadImage(mNewPlace.getUserPhotoUrl(), image_place_details_user_photo);
    }

    private void initializeList() {
        handleVerticalList(recyclerView_place_details);
        CommentsAdapter adapter = new CommentsAdapter(new ArrayList<>());
        recyclerView_place_details.setAdapter(adapter);
    }

    private void attachComments() {
        query(Constants.PLACES_COMMENTS, mKey);
        Observable.interval(1, TimeUnit.SECONDS)
                .take(2)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    getCompositeDisposable().add(disposable);
                    progress_place_details.setVisibility(View.VISIBLE);
                    recyclerView_place_details.setVisibility(View.GONE);
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
                    dispose();
                })
                .subscribe();
    }

    private void updateList() {
        recyclerView_place_details.scheduleLayoutAnimation();
        CommentsAdapter adapter = (CommentsAdapter) recyclerView_place_details.getAdapter();
        assert adapter != null;
        Collections.reverse(getCommentsList());
        adapter.comments = getCommentsList();
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.image_place_details_send)
    void addComment() {
        if (isInputValid(edittext_place_details_comment.getText().toString(), edittext_place_details_comment, "Write your comment first")){
            Comments comments = new Comments(getUsername(), getUserPhoto(), edittext_place_details_comment.getText().toString());
            pushComment(comments, Constants.PLACES_COMMENTS, mKey);
            attachComments();
        }
    }

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

    private CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null || compositeDisposable.isDisposed())
            compositeDisposable = new CompositeDisposable();
        return compositeDisposable;
    }

    private void dispose() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed())
            compositeDisposable.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dispose();
    }
}
