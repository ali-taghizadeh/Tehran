package ir.taghizadeh.tehran.dependencies;

import ir.taghizadeh.tehran.MainActivity;
import ir.taghizadeh.tehran.helpers.AuthenticationImpl;
import ir.taghizadeh.tehran.helpers.Authentication;
import ir.taghizadeh.tehran.helpers.Constants;

public class DependencyRegistry {

    public static  DependencyRegistry register = new DependencyRegistry();

    public void inject(MainActivity activity) {
        Authentication authenticationPresenter = new AuthenticationImpl(activity, Constants.RC_SIGN_IN);
        activity.configureWith(authenticationPresenter);
    }

}
