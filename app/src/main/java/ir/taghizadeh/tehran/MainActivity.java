package ir.taghizadeh.tehran;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.taghizadeh.tehran.helpers.AuthenticationHelper;
import ir.taghizadeh.tehran.helpers.MapHelper;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    AuthenticationHelper mAuthenticationHelper;
    MapHelper mMapHelper;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuthenticationHelper = new AuthenticationHelper(this, RC_SIGN_IN);
        mMapHelper = new MapHelper(this);
        mUsername = mAuthenticationHelper.getUsername();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Signed in canceled", Toast.LENGTH_SHORT).show();
                finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuthenticationHelper.addAuthStateListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuthenticationHelper.removeAuthStateListener();
    }
}
