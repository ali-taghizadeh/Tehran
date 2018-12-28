package ir.taghizadeh.tehran.activities;

import android.os.Bundle;

import butterknife.ButterKnife;
import ir.taghizadeh.tehran.R;
import ir.taghizadeh.tehran.models.NewPlace;

public class PlaceDetails extends AuthenticationActivity {

    NewPlace mNewPlace;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        ButterKnife.bind(this);
        mNewPlace = (NewPlace) getIntent().getSerializableExtra("newPlace");
    }
}
