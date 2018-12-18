package ir.taghizadeh.tehran;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import ir.taghizadeh.tehran.helpers.AuthenticationHelper;

public class MainActivity extends AppCompatActivity {


    private static final int RC_SIGN_IN = 1;
    AuthenticationHelper mAuthenticationHelper;
    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuthenticationHelper = new AuthenticationHelper(this, RC_SIGN_IN);
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
