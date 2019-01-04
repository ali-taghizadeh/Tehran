package ir.taghizadeh.tehran.models;

import java.io.Serializable;

public class Comments implements Serializable {

    public Comments() {
    }
    private String mUsername;
    private String mUserPhotoUrl;
    private String mComment;

    public Comments(String username, String userPhotoUrl, String comment){
        this.mUsername = username;
        this.mUserPhotoUrl = userPhotoUrl;
        this.mComment = comment;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getUserPhotoUrl() {
        return mUserPhotoUrl;
    }

    public void setUserPhotoUrl(String mUserPhotoUrl) {
        this.mUserPhotoUrl = mUserPhotoUrl;
    }

    public String getComment() {
        return mComment;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }
}
