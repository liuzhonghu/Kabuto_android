package com.nec.kabutoclient.data.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.nec.kabutoclient.KabutoApplication;
import com.nec.kabutoclient.data.ThirdType;
import com.nec.kabutoclient.data.db.RealmManager;
import com.nec.kabutoclient.data.response.User;
import com.nec.kabutoclient.util.UserService;

/**
 * Created by liuzhonghu on 2017/3/25.
 *
 * @Description Configurations.
 */

public class AppConfig {
    private static final String GENERIC_CONFIG_PREFERENCE_NAME = "kabuto_config";
    private static final String SERVER_CONFIG = "server_config";
    private static final String LOGIN_STATE = "login_state";
    private static final String USER_TOKEN = "user_token";

    private static SharedPreferences genericSharedPrefs;

    private AppConfig() {
    }

    public static void init(Context context) {
        genericSharedPrefs = context.getSharedPreferences(GENERIC_CONFIG_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
    }

    public static synchronized void saveLoginState(ThirdType thirdType) {
        SharedPreferences.Editor editor = genericSharedPrefs.edit();
        editor.putInt(LOGIN_STATE, thirdType.code);
        editor.apply();
    }

    public static synchronized ThirdType getLoginState() {
        return ThirdType.codeNumOf(genericSharedPrefs.getInt(LOGIN_STATE, 0));
    }

    public static synchronized void saveUserToken(String userToke) {
        SharedPreferences.Editor editor = genericSharedPrefs.edit();
        editor.putString(USER_TOKEN, TextUtils.isEmpty(userToke) ? "" : userToke);
        editor.apply();
        RealmManager.getInstance().initRealm(KabutoApplication.getAppContext());
    }

    public static synchronized String getUserToken() {
        return genericSharedPrefs.getString(USER_TOKEN, "");
    }

    public static synchronized void saveUser(User user) {
        UserService.getInstance().saveUser(user);
    }

    public static synchronized User getUser() {
        return UserService.getInstance().getUser();
    }

    public static void saveString(String key, String str) {
        genericSharedPrefs.edit().putString(key, str).apply();
    }

    public static String getString(String key) {
        return genericSharedPrefs.getString(key, "");
    }

    public static void saveBoolean(String key, boolean value) {
        genericSharedPrefs.edit().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key) {
        return genericSharedPrefs.getBoolean(key, false);
    }

    public static int getInt(String key) {
        return genericSharedPrefs.getInt(key, -1);
    }

}
