package com.example.user.myyandextranslate.netJson;

import android.content.Context;
import android.content.SharedPreferences;

public class Singleton {

    private static Singleton isInstance;
    private SharedPreferences sharedPreferences;

    private Singleton(final Context context) {
        sharedPreferences = context.getSharedPreferences("TRANSLATE", Context.MODE_PRIVATE);
    }

    public static synchronized void init(Context context) {
        if (isInstance == null) {
            synchronized (HttpManager.class) {
                isInstance = new Singleton(context);
            }
        }

        if (context == null) {
            throw new IllegalArgumentException("Non-null context required.");
        }
    }



}
