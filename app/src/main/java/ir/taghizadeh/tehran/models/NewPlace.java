package ir.taghizadeh.tehran.models;

import java.io.Serializable;

public class NewPlace implements Serializable {
    public NewPlace() {
    }

    private String mUsername;
    private String mTitle;
    private String mDescription;
    private String mPhotoUrl;
    private String mUserPhotoUrl;
    private int mLikes;
    private int mDislikes;

    public NewPlace(String username, String title, String description, String photoUrl, String userPhotoUrl, int likes, int dislikes) {
        this.mUsername = username;
        this.mTitle = title;
        this.mDescription = description;
        this.mPhotoUrl = photoUrl;
        this.mUserPhotoUrl = userPhotoUrl;
        this.mLikes = likes;
        this.mDislikes = dislikes;

    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
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

    public String getUserPhotoUrl() {
        return mUserPhotoUrl;
    }

    public void setUserPhotoUrl(String mUserPhotoUrl) {
        this.mUserPhotoUrl = mUserPhotoUrl;
    }

    public int getLikes() {
        return mLikes;
    }

    public void setLikes(int mLikes) {
        this.mLikes = mLikes;
    }

    public int getDislikes() {
        return mDislikes;
    }

    public void setmislikes(int mDislikes) {
        this.mDislikes = mDislikes;
    }

}
