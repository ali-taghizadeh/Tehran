package ir.taghizadeh.tehran;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.helpers.Authentication;
import ir.taghizadeh.tehran.helpers.Constants;
import ir.taghizadeh.tehran.helpers.MapHelper;
import ir.taghizadeh.tehran.helpers.StatusBarHelper;

public class MainActivity extends AppCompatActivity {

    private Authentication mAuthentication;
    private Authentication.UsernameListener mUsernameListener;
    StatusBarHelper mStatusBarHelper;
    MapHelper mMapHelper;

    @BindView(R.id.text_main_username)
    TextView text_main_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DependencyRegistry.register.inject(this);
        mMapHelper = new MapHelper(this);
        mStatusBarHelper = new StatusBarHelper(this);
    }

    public void configureWith(Authentication authentication) {
        this.mAuthentication = authentication;
        authentication.setUsernameListener(username -> text_main_username.setText(username));
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN && resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Signed in canceled", Toast.LENGTH_SHORT).show();
                finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuthentication.addAuthStateListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuthentication.removeAuthStateListener();
    }

    @OnClick(R.id.image_main_logout)
    void logOut(){
        mAuthentication.signOut();
    }
}
