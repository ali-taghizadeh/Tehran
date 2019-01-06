package ir.taghizadeh.tehran.dependencies.glide;

import android.app.Activity;
import android.widget.ImageView;

public class GlideImpl implements Glide {
    private Activity activity;

    // region CONSTRUCTOR
    public GlideImpl(Activity activity) {
        this.activity = activity;
    }
    // endregion

    // region LOAD IMAGE FROM URL
    @Override
    public void loadImage(String url, ImageView imageView) {
        com.bumptech.glide.Glide.with(activity)
                .load(url)
                .into(imageView);
    }
    // endregion

    // region LOAD IMAGE FROM RESOURCES
    @Override
    public void loadFromResources(ImageView imageView, String resId) {
        com.bumptech.glide.Glide.with(activity)
                .load(activity.getResources().getIdentifier(resId, "drawable", activity.getPackageName()))
                .into(imageView);
    }
    // endregion

}
