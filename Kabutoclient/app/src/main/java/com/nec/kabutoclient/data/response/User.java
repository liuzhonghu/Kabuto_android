package com.nec.kabutoclient.data.response;

import com.google.gson.annotations.SerializedName;
import com.nec.kabutoclient.data.realm.RealmUser;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 5410196420801562875L;
    @SerializedName("id")
    private int userId;
    @SerializedName("userName")
    private String userName;
    @SerializedName("avatar")
    private String avatar;

    public User(int userId, String userName, String avatar) {
        this.userId = userId;
        this.userName = userName;
        this.avatar = avatar;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUserId() {
        return userId;
    }

    public static class Builder {
        private int userId;
        private String userName;
        private String avatar;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public User build() {
            return new User(userId, userName, avatar);
        }
    }

    public RealmUser getRealmData() {
        return new RealmUser.Builder().setUserId(userId).setUserName(userName).setAvatar(avatar).build();
    }

    public static User realmValueOf(RealmUser realmUser) {
        return new User(realmUser.getUserId(), realmUser.getUserName(), realmUser.getAvatar());
    }

    public User clone() {
        return new User(userId, userName, avatar);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
