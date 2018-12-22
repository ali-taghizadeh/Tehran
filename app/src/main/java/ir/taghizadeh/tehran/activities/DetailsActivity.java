package ir.taghizadeh.tehran.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.map.Map;

public class DetailsActivity extends AuthenticationActivity {

    @BindView(R.id.image_details_add_photo)
    ShapedImageView image_details_add_photo;
    @BindView(R.id.edittext_details_title)
    TextInputEditText edittext_details_title;
    @BindView(R.id.edittext_details_description)
    TextInputEditText edittext_details_description;
    private Map mMap;
    private LatLng mLatLng;
    private String mTitle = "Title";
    private String mDescription = "Description";
    private Disposable mTitleDisposable;
    private Disposable mDescriptionDisposable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        DependencyRegistry.register.inject(this);
        mTitleDisposable = textChangeListener(edittext_details_title);
        mDescriptionDisposable = textChangeListener(edittext_details_description);
    }

    public void configureWith(Map map) {
        this.mMap = map;
        setUpUI();
    }

    private void setUpUI() {
        hideStatusBar();
        getLatLng();
        attachMap(getLatLng(), mTitle, mDescription);
    }

    private void attachMap(LatLng latLng, String title, String description) {
        mMap.setOnMapListener(() -> {
            mMap.addMarker(latLng, title, description);
            mMap.startCamera(latLng, 15);
            image_details_add_photo.setVisibility(View.VISIBLE);
        });
    }

    private void updateMap() {
        mMap.clearMap();
        mMap.addMarker(mLatLng, mTitle, mDescription);
    }

    private LatLng getLatLng() {
        Bundle bundle = getIntent().getParcelableExtra("bundle");
        mLatLng = bundle.getParcelable("location");
        return mLatLng;
    }

    private Disposable textChangeListener(TextInputEditText editText) {
        return RxTextView.textChanges(editText)
                .debounce(600, TimeUnit.MILLISECONDS)
                .filter(changes -> {
                    assert changes != null;
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(character -> {
                    if (editText == edittext_details_title)
                        mTitle = character.toString();
                    else
                        mDescription = character.toString();
                    updateMap();
                });
    }
}
