package com.example.user.myyandextranslate.netJson;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class JsonYandex {
    final static String TRANSLATE_KEY = "trnsl.1.1.20170327T074258Z.c68ee7e4540295d2.f2bd9c673bd22ffb2d06cde44d0862c40c3d8648";
    final static String TRANSLATE_UI = "ru";
    private static Service translateService;
    public static Service getTranslateService() {

        if (translateService != null) {
            return translateService;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/api/v1.5/tr.json/")
                .client(HttpManager.getDefaultInstance().getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        translateService = retrofit.create(Service.class);

        return translateService;

    }

    public static Observable<TranslateResponce> getTranslate(String direction, String text) {
        return getTranslateService().getTranslate(TRANSLATE_KEY, direction, text);
    }

    public static Observable<LanguagesResponce> getSupportLanguages() {
        return getTranslateService().getSupportLanguages(TRANSLATE_KEY, TRANSLATE_UI);
    }



    private interface Service {

        @Headers("Content-Type: application/json;charset=UTF-8")
        @POST("getLangs")
        Observable<LanguagesResponce> getSupportLanguages(@Query("key") String key, @Query("ui") String ui);

        @Headers("Content-Type: application/json;charset=UTF-8")
        @POST("translate")
        Observable<TranslateResponce> getTranslate(@Query("key") String key, @Query("lang") String lang, @Query("text") String text);

    }

}
