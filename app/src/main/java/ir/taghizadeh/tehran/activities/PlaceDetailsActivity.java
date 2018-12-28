package ir.taghizadeh.tehran.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import ir.taghizadeh.tehran.R;
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

    private NewPlace mNewPlace;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        ButterKnife.bind(this);
        mNewPlace = (NewPlace) getIntent().getSerializableExtra("newPlace");
        hideStatusBar();
        attachUI();
    }

    private void attachUI() {
        text_place_details_title.setText(mNewPlace.getTitle().toUpperCase());
        text_place_details_description.setText(mNewPlace.getDescription());
        text_place_details_author.setText(mNewPlace.getUsername().toUpperCase());
        text_place_details_likes.setText(String.valueOf(mNewPlace.getLikes()));
        text_place_details_dislikes.setText(String.valueOf(mNewPlace.getDislikes()));
        if (!mNewPlace.getPhotoUrl().equals(""))loadImage(mNewPlace.getPhotoUrl(), image_place_details_photo);
        loadImage(mNewPlace.getUserPhotoUrl(), image_place_details_user_photo);
    }
}
