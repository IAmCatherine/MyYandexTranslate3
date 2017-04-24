package com.example.user.myyandextranslate.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Direction extends RealmObject{

    @PrimaryKey
    private String direction;

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

}
