package ir.taghizadeh.tehran.activities.lists;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.dependencies.glide.Glide;
import ir.taghizadeh.tehran.models.NewPlace;

class PlacesListViewHolder extends RecyclerView.ViewHolder {

    private ImageView image_item_place_photo;
    private TextView text_item_place_title;
    private TextView text_item_place_description;
    private TextView text_item_place_author;

    PlacesListViewHolder(View itemView) {
        super(itemView);
        this.image_item_place_photo = itemView.findViewById(R.id.image_item_place_photo);
        this.text_item_place_title = itemView.findViewById(R.id.text_item_place_title);
        this.text_item_place_description = itemView.findViewById(R.id.text_item_place_description);
        this.text_item_place_author = itemView.findViewById(R.id.text_item_place_author);
    }

    void configureWith(NewPlace newPlace, Glide mGlide) {
        text_item_place_title.setText(newPlace.getTitle());
        text_item_place_description.setText(newPlace.getDescription());
        text_item_place_author.setText(newPlace.getUsername().toUpperCase());
        if (newPlace.getPhotoUrl() != null){
            mGlide.loadImage(newPlace.getPhotoUrl(), image_item_place_photo);
        }
    }

}
