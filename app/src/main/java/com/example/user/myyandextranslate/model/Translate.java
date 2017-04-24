package com.example.user.myyandextranslate.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Translate extends RealmObject {

    @PrimaryKey
    private String text;
    private String translatedText;
    private String direction;
    private Boolean isFavorites;

    public Translate() {

    }

    public Translate(String text, String translatedText, String direction, Boolean isFavorites) {
        this.text = text;
        this.translatedText = translatedText;
        this.direction = direction;
        this.isFavorites = isFavorites;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Boolean getFavorites() {
        return isFavorites;
    }

    public void setFavorites(Boolean favorites) {
        isFavorites = favorites;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Translate translate = (Translate) o;

        if (text != null ? !text.equals(translate.text) : translate.text != null) return false;
        if (translatedText != null ? !translatedText.equals(translate.translatedText) : translate.translatedText != null)
            return false;
        if (direction != null ? !direction.equals(translate.direction) : translate.direction != null)
            return false;
        return isFavorites != null ? isFavorites.equals(translate.isFavorites) : translate.isFavorites == null;

    }

    @Override
    public int hashCode() {
        int result = text != null ? text.hashCode() : 0;
        result = 31 * result + (translatedText != null ? translatedText.hashCode() : 0);
        result = 31 * result + (direction != null ? direction.hashCode() : 0);
        result = 31 * result + (isFavorites != null ? isFavorites.hashCode() : 0);
        return result;
    }
}
