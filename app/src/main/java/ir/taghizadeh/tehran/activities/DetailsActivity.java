package ir.taghizadeh.tehran.activities;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.dependencies.map.Map;

public class DetailsActivity extends AuthenticationActivity {

    private Map mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        DependencyRegistry.register.inject(this);
    }

    public void configureWith(Map map) {
        this.mMap = map;
        setUpUI();
    }

    private void setUpUI() {
        hideStatusBar();
        Bundle bundle = getIntent().getParcelableExtra("bundle");
        LatLng location = bundle.getParcelable("location");
        Log.e("location", location.latitude + ", " + location.longitude);
        mMap.setOnMapListener(() -> {
            mMap.addMarker(location);
            mMap.startCamera(location);
        });

    }
}
