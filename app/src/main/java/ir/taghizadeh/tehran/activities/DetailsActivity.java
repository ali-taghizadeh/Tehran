package ir.taghizadeh.tehran.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.map.Map;

public class DetailsActivity extends AuthenticationActivity {

    private Map mMap;

    @BindView(R.id.image_details_add_photo)
    ShapedImageView image_details_add_photo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        DependencyRegistry.register.inject(this);
    }

    public void configureWith(Map map) {
        this.mMap = map;
        setUpUI();
    }

    private void setUpUI() {
        hideStatusBar();
        getLatLng();
        attachMap(getLatLng(), "Title", "Description");
    }

    private void attachMap(LatLng latLng, String title, String description) {
        mMap.setOnMapListener(() -> {
            mMap.clearMap();
            mMap.addMarker(latLng, title, description);
            mMap.startCamera(latLng, 15);
            image_details_add_photo.setVisibility(View.VISIBLE);
        });
    }

    private LatLng getLatLng() {
        Bundle bundle = getIntent().getParcelableExtra("bundle");
        return  bundle.getParcelable("location");
    }
}
