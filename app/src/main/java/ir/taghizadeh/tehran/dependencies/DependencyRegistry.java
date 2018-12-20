package ir.taghizadeh.tehran.dependencies;

import ir.taghizadeh.tehran.MainActivity;
import ir.taghizadeh.tehran.helpers.AuthenticationImpl;
import ir.taghizadeh.tehran.helpers.Authentication;
import ir.taghizadeh.tehran.helpers.Constants;
import ir.taghizadeh.tehran.helpers.Map;
import ir.taghizadeh.tehran.helpers.MapImpl;
import ir.taghizadeh.tehran.helpers.WindowConfig;
import ir.taghizadeh.tehran.helpers.WindowConfigImpl;

public class DependencyRegistry {

    public static  DependencyRegistry register = new DependencyRegistry();

    public void inject(MainActivity activity) {
        Authentication authenticationPresenter = new AuthenticationImpl(activity, Constants.RC_SIGN_IN);
        Map mapPresenter = new MapImpl(activity);
        WindowConfig windowConfigPresenter = new WindowConfigImpl(activity);
        activity.configureWith(authenticationPresenter, mapPresenter, windowConfigPresenter);
    }

}
