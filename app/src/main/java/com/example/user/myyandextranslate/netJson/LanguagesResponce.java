package com.example.user.myyandextranslate.netJson;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class LanguagesResponce {

    @SerializedName("dirs")
    @Expose
    private List<String> dirs;

    @SerializedName("langs")
    @Expose
    private Map<String, String> langs;

    public LanguagesResponce(@NonNull Map<String, String> langs, @NonNull List<String> dirs) {
        this.langs = langs;
        this.dirs = dirs;
    }

    public Map<String, String> getLangs() {
        return langs;
    }

    public void setLangs(@NonNull final Map<String, String> langs) {
        this.langs = langs;
    }

    public List<String> getDirs() {
        return dirs;
    }

    public void setDirs(@NonNull final List<String> dirs) {
        this.dirs = dirs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LanguagesResponce languagesResponce = (LanguagesResponce) o;

        if (!dirs.equals(languagesResponce.dirs)) return false;
        return langs.equals(languagesResponce.langs);

    }

    @Override
    public int hashCode() {
        int result = dirs.hashCode();
        result = 31 * result + langs.hashCode();
        return result;
    }
}
