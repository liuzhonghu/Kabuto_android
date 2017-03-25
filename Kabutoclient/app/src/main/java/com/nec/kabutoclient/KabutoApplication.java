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
    private static String maleName;
    private static String femaleName;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        isColdStart = false;

        maleName = getResources().getString(R.string.male);
        femaleName = getResources().getString(R.string.female);
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

    public static void setGenderName(String[] gendeNames) {
        if (gendeNames != null && gendeNames.length >= 2) {
            String newMaleName = gendeNames[0];
            String newFemaleName = gendeNames[1];
            if (!newMaleName.equals(maleName) || !newFemaleName.equals(femaleName)) {
                maleName = newMaleName;
                femaleName = newFemaleName;
//                EventBus.getDefault().post(new GenderTextChangeEvent());
            }
        }
    }

}
