package com.nec.kabutoclient.data.db;

import android.content.Context;
import android.text.TextUtils;

import com.nec.kabutoclient.data.response.User;
import com.nec.kabutoclient.data.sp.AppConfig;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmManager {
    private final static int REALM_VERSION = 17;
    private static RealmManager realmManager;
    private static RealmConfiguration config;
    private Migration migration;

    private RealmManager() {
        migration = new Migration();
    }

    private static class SingletonLoader {
        private static final RealmManager INSTANCE = new RealmManager();
    }

    public static RealmManager getInstance() {
        return realmManager = SingletonLoader.INSTANCE;
    }

    public void initRealm(Context context) {
        synchronized (realmManager) {
            if (config == null) {
                User user = AppConfig.getUser();
                if (context != null && user != null) {
                    File newRealmFile = new File(context.getFilesDir(), user.getUserId() + ".realm");
                    if (newRealmFile.exists()) {
                        config = initRealmConfig(user.getUserId());
                    } else {
                        String userToken = AppConfig.getUserToken();
                        if (!TextUtils.isEmpty(userToken)) {
                            File oldRealmFile = new File(context.getFilesDir(), userToken + ".realm");
                            if (oldRealmFile.exists()) {
                                oldRealmFile.renameTo(newRealmFile);
                            }
                            config = initRealmConfig(user.getUserId());
                        }
                    }
                }
            }
            if (config != null) {
                Realm.setDefaultConfiguration(config);
            }
        }
    }

    public void resetRealm() {
        if (config != null) {
            config = null;
        }
    }

    private RealmConfiguration initRealmConfig(int userId) {
        return new RealmConfiguration.Builder()
                .name(userId + ".realm")
                .schemaVersion(REALM_VERSION)
                .modules(Realm.getDefaultModule())
                .migration(migration)
                .deleteRealmIfMigrationNeeded()
                .build();
    }
}
