package ir.taghizadeh.tehran.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.activities.lists.comments.CommentsAdapter;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.database.Database;
import ir.taghizadeh.tehran.helpers.Constants;
import ir.taghizadeh.tehran.models.Comments;
import ir.taghizadeh.tehran.models.NewPlace;

public class PlaceDetailsActivity extends AuthenticationActivity {

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

    private NewPlace mNewPlace;
    private List<Comments> mCommentsList = new ArrayList<>();
    private String mKey;
    private double mLatitude;
    private double mLongitude;
    private Database mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        ButterKnife.bind(this);
        mNewPlace = (NewPlace) getIntent().getSerializableExtra("newPlace");
        mKey = getIntent().getExtras().getString("key");
        mLatitude = getIntent().getExtras().getDouble("latitude");
        mLongitude = getIntent().getExtras().getDouble("longitude");
        DependencyRegistry.register.inject(this);
    }

    public void configureWith(Database databasePresenter) {
        this.mDatabase = databasePresenter;
        hideStatusBar();
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
        loadImage(mNewPlace.getUserPhotoUrl(), image_place_details_user_photo);
    }

    private void initializeList() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView_place_details.setLayoutManager(manager);
        CommentsAdapter adapter = new CommentsAdapter(mCommentsList);
        recyclerView_place_details.setAdapter(adapter);
    }


    private void attachComments() {
        mDatabase.query(Constants.PLACES_COMMENTS, mKey);
        mDatabase.setCommentsDataSnapshotListener(commentsList -> {
            mCommentsList = commentsList;
            Collections.reverse(mCommentsList);
            updateList(mCommentsList);
        });
    }

    @OnClick(R.id.image_place_details_send)
    void addComment() {
        if (!edittext_place_details_comment.getText().toString().equals("")) {
            Comments comments = new Comments(getUsername(), getUserPhoto(), edittext_place_details_comment.getText().toString());
            mDatabase.pushComment(comments, Constants.PLACES_COMMENTS, mKey);
            mDatabase.setPushListener(key -> {
                edittext_place_details_comment.setText("");
                attachComments();
            });
        } else edittext_place_details_comment.setError("Write your comment first");
    }

    @OnClick(R.id.image_place_details_like)
    void like(){
        int likes = mNewPlace.getLikes() + 1;
        mDatabase.pushLike(likes, Constants.PLACES, mKey);
        text_place_details_likes.setText(String.valueOf(likes));
    }

    @OnClick(R.id.image_place_details_dislike)
    void dislike(){
        int dislikes = mNewPlace.getDislikes() + 1;
        mDatabase.pushDislike(dislikes, Constants.PLACES, mKey);
        text_place_details_dislikes.setText(String.valueOf(dislikes));
    }

    @OnClick(R.id.fab_place_details_direction)
    void getDirection(){
        handleGetDirection(mLatitude, mLongitude);
    }

    private void updateList(List<Comments> comments) {
        if (comments.isEmpty()) {
            recyclerView_place_details.setVisibility(View.GONE);
            image_place_details_empty_list.setVisibility(View.VISIBLE);
            text_place_details_empty_list.setVisibility(View.VISIBLE);
        } else {
            recyclerView_place_details.setVisibility(View.VISIBLE);
            image_place_details_empty_list.setVisibility(View.GONE);
            text_place_details_empty_list.setVisibility(View.GONE);
        }
        this.mCommentsList = comments;
        CommentsAdapter adapter = (CommentsAdapter) recyclerView_place_details.getAdapter();
        assert adapter != null;
        adapter.comments = this.mCommentsList;
        adapter.notifyDataSetChanged();
    }
}
