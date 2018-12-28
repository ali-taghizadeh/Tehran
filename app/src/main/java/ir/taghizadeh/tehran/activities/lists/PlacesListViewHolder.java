package ir.taghizadeh.tehran.activities.lists;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.dependencies.glide.Glide;
import ir.taghizadeh.tehran.models.NewPlace;

class PlacesListViewHolder extends RecyclerView.ViewHolder {

    private ImageView image_item_place_photo;
    private ShapedImageView image_item_user_photo;
    private TextView text_item_place_title;
    private TextView text_item_place_description;
    private TextView text_item_place_likes;
    private TextView text_item_place_dislikes;
    private TextView text_item_place_comments;

    PlacesListViewHolder(View itemView) {
        super(itemView);
        this.image_item_place_photo = itemView.findViewById(R.id.image_item_place_photo);
        this.image_item_user_photo = itemView.findViewById(R.id.image_item_user_photo);
        this.text_item_place_title = itemView.findViewById(R.id.text_item_place_title);
        this.text_item_place_description = itemView.findViewById(R.id.text_item_place_description);
        this.text_item_place_likes = itemView.findViewById(R.id.text_item_place_likes);
        this.text_item_place_dislikes = itemView.findViewById(R.id.text_item_place_dislikes);
        this.text_item_place_comments = itemView.findViewById(R.id.text_item_place_comments);
    }

    void configureWith(NewPlace newPlace, Glide mGlide) {
        text_item_place_title.setText(newPlace.getTitle());
        text_item_place_description.setText(newPlace.getDescription());
        text_item_place_likes.setText(String.valueOf(newPlace.getLikes()));
        text_item_place_dislikes.setText(String.valueOf(newPlace.getDislikes()));
        text_item_place_comments.setText(String.valueOf(newPlace.getComments().size()));
        mGlide.loadImage(newPlace.getUserPhotoUrl(), image_item_user_photo);
        if ((newPlace.getPhotoUrl() != null && !newPlace.getPhotoUrl().equals("")))
            mGlide.loadImage(newPlace.getPhotoUrl(), image_item_place_photo);
    }

}
