package com.nec.kabutoclient;

import android.app.Application;
import android.content.Context;

/**
 * Created by liuzhonghu on 2017/3/20.
 *
 * @Description app application
 */

public class KabutoApplication extends Application {

    private static Context context;
    public static boolean isColdStart;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        isColdStart = false;

    }

    public static Context getAppContext() {
        return context;
    }

//    public static void setUserData(User userData) {
//        if (userData == null) {
//            UserService.getInstance().clearUser();
//        } else {
//            UserService.getInstance().saveUser(userData);
//        }
//    }

//    public static User getUserData() {
//        return UserService.getInstance().getUser();
//    }

}
