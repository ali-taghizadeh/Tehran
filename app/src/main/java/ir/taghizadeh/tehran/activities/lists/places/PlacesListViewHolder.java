package ir.taghizadeh.tehran.activities.lists.places;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.dependencies.glide.Glide;
import ir.taghizadeh.tehran.models.NewPlace;

class PlacesListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.image_item_place_photo)
    ShapedImageView image_item_place_photo;
    @BindView(R.id.image_item_user_photo)
    ShapedImageView image_item_user_photo;
    @BindView(R.id.text_item_place_title)
    TextView text_item_place_title;
    @BindView(R.id.text_item_place_description)
    TextView text_item_place_description;
    @BindView(R.id.text_item_place_likes)
    TextView text_item_place_likes;
    @BindView(R.id.text_item_place_dislikes)
    TextView text_item_place_dislikes;

    PlacesListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void configureWith(NewPlace newPlace, Glide mGlide) {
        text_item_place_title.setText(newPlace.getTitle().toUpperCase());
        text_item_place_description.setText(newPlace.getDescription());
        text_item_place_likes.setText(String.valueOf(newPlace.getLikes()));
        text_item_place_dislikes.setText(String.valueOf(newPlace.getDislikes()));
        if ((newPlace.getPhotoUrl() != null && !newPlace.getPhotoUrl().equals(""))) mGlide.loadImage(newPlace.getPhotoUrl(), image_item_place_photo);
        else mGlide.loadFromResources(image_item_place_photo, "rect");
        if ((newPlace.getUserPhotoUrl() != null && !newPlace.getUserPhotoUrl().equals(""))) mGlide.loadImage(newPlace.getUserPhotoUrl(), image_item_user_photo);
        else mGlide.loadFromResources(image_item_user_photo, "oval");
    }

}
