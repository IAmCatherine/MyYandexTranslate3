package com.example.user.myyandextranslate.netJson;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TranslateResponce {

    @SerializedName("code")
    @Expose
    private Integer code;

    @SerializedName("lang")
    @Expose
    private String lang;

    @SerializedName("text")
    @Expose
    private List<String> text = null;


    public TranslateResponce(@NonNull Integer code, String lang, List<String> text) {
        this.code = code;
        this.lang = lang;
        this.text = text;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(@NonNull Integer code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TranslateResponce translateResponce = (TranslateResponce) o;

        if (!code.equals(translateResponce.code)) return false;
        if (lang != null ? !lang.equals(translateResponce.lang) : translateResponce.lang != null) return false;
        return text != null ? text.equals(translateResponce.text) : translateResponce.text == null;

    }

    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + (lang != null ? lang.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
