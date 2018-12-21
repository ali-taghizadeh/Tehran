package ir.taghizadeh.tehran.dependencies.glide;

import android.widget.ImageView;

public interface Glide {
    void loadImage(String url, ImageView imageView);
    void blankUserPhoto(ImageView imageView);
}
