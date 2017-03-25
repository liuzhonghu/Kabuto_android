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


    public RealmUser(int userId) {

    }

    public int getUserId() {
        return userId;
    }

    public static final class Builder {
        private int userId;

        public Builder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public RealmUser build() {
            return new RealmUser(userId);
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
