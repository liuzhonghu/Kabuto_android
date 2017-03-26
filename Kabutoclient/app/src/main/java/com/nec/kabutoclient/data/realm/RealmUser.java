package com.nec.kabutoclient.data.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by liuzhonghu on 2017/3/25.
 *
 * @Description
 */

public class RealmUser extends RealmObject implements Cloneable {
    @PrimaryKey
    private int userId;
    private String userName;
    private String avatar;


    public RealmUser(int userId, String userName, String avatar) {
        this.userId = userId;
        this.userName = userName;
        this.avatar = avatar;
    }

    public RealmUser() {
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

    public static final class Builder {
        private int userId;
        private String userName;
        private String avatar;

        public Builder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public int getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public String getAvatar() {
            return avatar;
        }

        public Builder setAvatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public RealmUser build() {
            return new RealmUser(userId, userName, avatar);
        }
    }

    public Object clone() {
        RealmUser data = null;
        try {
            data = (RealmUser) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
        return data;
    }
}
