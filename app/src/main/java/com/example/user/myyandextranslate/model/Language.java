package com.example.user.myyandextranslate.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Language extends RealmObject{

    @PrimaryKey
    private String key;
    private String value;

    public Language() {

    }

    public Language(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Language language = (Language) o;

        if (key != null ? !key.equals(language.key) : language.key != null) return false;
        return value != null ? value.equals(language.value) : language.value == null;

    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
