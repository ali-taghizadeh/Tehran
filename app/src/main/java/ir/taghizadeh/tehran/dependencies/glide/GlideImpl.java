package ir.taghizadeh.tehran.dependencies.glide;

import android.app.Activity;
import android.widget.ImageView;

public class GlideImpl implements Glide {
    private Activity activity;

    public GlideImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        com.bumptech.glide.Glide.with(activity)
                .load(url)
                .into(imageView);
    }

    @Override
    public void loadFromResources(ImageView imageView, String resId) {
        com.bumptech.glide.Glide.with(activity)
                .load(activity.getResources().getIdentifier(resId, "drawable", activity.getPackageName()))
                .into(imageView);
    }
}
