package ir.taghizadeh.tehran.models;

public class NewPlace {
    public NewPlace() {
    }

    private String mUsername;
    private String mTitle;
    private String mDescription;
    private String mPhotoUrl;

    public NewPlace(String username, String title, String description, String photoUrl) {
        this.mUsername = username;
        this.mTitle = title;
        this.mDescription = description;
        this.mPhotoUrl = photoUrl;
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
}
