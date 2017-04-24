package com.example.user.myyandextranslate.netJson;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class JsonYandex {

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
        return getTranslateService().getTranslate(Constant.TRANSLATE_KEY, direction, text);
    }

    public static Observable<LanguagesResponce> getSupportLanguages() {
        return getTranslateService().getSupportLanguages(Constant.TRANSLATE_KEY, Constant.TRANSLATE_UI);
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
