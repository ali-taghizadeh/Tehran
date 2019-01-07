package ir.taghizadeh.tehran.dependencies.glide;

import android.app.Activity;
import android.widget.ImageView;

/**
 * <h1>GlideImpl</h1>
 * <p>
 * The main logic about Glide is done here. It's done so simple and there's no
 * catching strategy or any other complexity.
 *
 * @author Ali Taghizadeh Gevari
 * @version 1.0
 * @since 2019-01-07
 */

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
