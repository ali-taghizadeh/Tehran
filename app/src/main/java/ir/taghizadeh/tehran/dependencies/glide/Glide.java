package ir.taghizadeh.tehran.dependencies.glide;

import android.widget.ImageView;

public interface Glide {

    // region LOAD IMAGE FROM URL
    void loadImage(String url, ImageView imageView);
    // endregion

    // region LOAD IMAGE FROM RESOURCES
    void loadFromResources(ImageView imageView, String resId);
    // endregion

}
