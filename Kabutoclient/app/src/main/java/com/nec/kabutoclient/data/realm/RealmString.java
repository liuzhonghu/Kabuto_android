package com.nec.kabutoclient.data.realm;

import io.realm.RealmObject;

public class RealmString extends RealmObject {
    private String value;

    public RealmString(String value) {
        this.value = value;
    }

    public RealmString() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
