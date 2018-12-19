package ir.taghizadeh.tehran.helpers;

public interface Authentication {

    String getUsername();
    void addAuthStateListener();
    void removeAuthStateListener();
    void signOut();
}
