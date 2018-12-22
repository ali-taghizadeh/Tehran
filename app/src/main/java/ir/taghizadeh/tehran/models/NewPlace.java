package ir.taghizadeh.tehran.models;

import com.google.android.gms.maps.model.LatLng;

public class NewPlace {

    private String mTitle;
    private String mDescription;
    private String mPhotoUrl;

    public NewPlace(String title, String description, String photoUrl) {
        this.mTitle = title;
        this.mDescription = description;
        this.mPhotoUrl = photoUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }
}
