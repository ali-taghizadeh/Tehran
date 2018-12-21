package ir.taghizadeh.tehran.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ir.taghizadeh.tehran.R;

public class DetailsActivity extends AuthenticationActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        hideStatusBar();
    }
}
