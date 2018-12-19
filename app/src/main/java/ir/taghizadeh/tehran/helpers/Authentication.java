package ir.taghizadeh.tehran.helpers;

public interface Authentication {
    void setUsernameListener(UsernameListener mUsernameListener);
    void addAuthStateListener();
    void removeAuthStateListener();
    void signOut();

    interface UsernameListener {
        void onUsernameReady(String username);
    }
}
