package ir.taghizadeh.tehran;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;
import ir.taghizadeh.tehran.dependencies.DependencyRegistry;
import ir.taghizadeh.tehran.helpers.Authentication;
import ir.taghizadeh.tehran.helpers.Constants;
import ir.taghizadeh.tehran.helpers.MapHelper;
import ir.taghizadeh.tehran.helpers.StatusBarHelper;

public class MainActivity extends AppCompatActivity {

    private Authentication mAuthentication;
    StatusBarHelper mStatusBarHelper;
    MapHelper mMapHelper;

    @BindView(R.id.text_main_username)
    TextView text_main_username;
    @BindView(R.id.image_main_add_photo)
    ShapedImageView image_main_add_photo;
    @BindView(R.id.image_main_icon_add_photo)
    ImageView image_main_icon_add_photo;


    private FirebaseStorage mFirebaseStorage;
    private StorageReference mChatPhotoStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DependencyRegistry.register.inject(this);
        mMapHelper = new MapHelper(this);
        mStatusBarHelper = new StatusBarHelper(this);
        mFirebaseStorage = FirebaseStorage.getInstance();
        mChatPhotoStorageReference = mFirebaseStorage.getReference().child("user_avatar");
    }

    public void configureWith(Authentication authentication) {
        this.mAuthentication = authentication;
        authentication.setUsernameListener(username -> text_main_username.setText(username));
        authentication.setPhotoURLListener(uri -> {
            if (uri != null){
                Glide.with(MainActivity.this)
                        .load(uri.toString())
                        .into(image_main_add_photo);
                image_main_icon_add_photo.setVisibility(View.GONE);
            }else {
                Glide.with(MainActivity.this)
                        .load(getResources().getIdentifier("oval", "drawable", this.getPackageName()))
                        .into(image_main_add_photo);
                image_main_icon_add_photo.setVisibility(View.VISIBLE);
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN && resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Signed in canceled", Toast.LENGTH_SHORT).show();
                finish();
        }else if (requestCode == Constants.RC_PHOTO_PICKER && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            StorageReference userAvatar = mChatPhotoStorageReference.child(selectedImageUri.getLastPathSegment());
            userAvatar.putFile(selectedImageUri).addOnSuccessListener(this, taskSnapshot -> {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri downloadUrl = urlTask.getResult();
                assert downloadUrl != null;
                mAuthentication.updatePhotoURL(downloadUrl);
            });
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

    @OnClick(R.id.image_main_add_photo)
    void addPhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), Constants.RC_PHOTO_PICKER);
    }
}
