package ir.taghizadeh.tehran.models;

public class Comments {

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

    public String getmUsername() {
        return mUsername;
    }

    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getmUserPhotoUrl() {
        return mUserPhotoUrl;
    }

    public void setmUserPhotoUrl(String mUserPhotoUrl) {
        this.mUserPhotoUrl = mUserPhotoUrl;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }
}
