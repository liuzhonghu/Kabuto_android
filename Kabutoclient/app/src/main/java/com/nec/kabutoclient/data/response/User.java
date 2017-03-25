package com.nec.kabutoclient.data.response;

import com.google.gson.annotations.SerializedName;
import com.nec.kabutoclient.data.realm.RealmUser;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("id")
    private int userId;

    public User(int userId) {
    }

    public int getUserId() {
        return userId;
    }

    public static class Builder {
        private int userId;

        public User build() {
            return new User(userId);
        }
    }

    public RealmUser getRealmData() {
        return new RealmUser.Builder().setUserId(userId).build();
    }

    public static User realmValueOf(RealmUser realmUser) {
        return new User(realmUser.getUserId());
    }

    public User clone() {
        return new User(userId);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
