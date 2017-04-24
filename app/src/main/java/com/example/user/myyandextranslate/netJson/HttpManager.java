package com.example.user.myyandextranslate.netJson;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;

public class HttpManager {

    private static HttpManager sInstance;
    private okhttp3.OkHttpClient mClient;

    private HttpManager(final Context context) {
        okhttp3.OkHttpClient.Builder okhttpBuilder = new okhttp3.OkHttpClient.Builder();
        okhttpBuilder
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cache(new Cache(context.getCacheDir(), 10*1024*1024))
                .connectionPool(new okhttp3.ConnectionPool(6, 6, TimeUnit.MINUTES));

        mClient = okhttpBuilder.build();
    }

    public static synchronized void init(Context context) {
        if (sInstance == null) {
            synchronized (HttpManager.class) {
                sInstance = new HttpManager(context);
            }
        }

        if (context == null) {
            throw new IllegalArgumentException("Non-null context required.");
        }
    }

    public static HttpManager getDefaultInstance() {
        return sInstance;
    }

    public okhttp3.OkHttpClient getClient() {
        return mClient;
    }

}
