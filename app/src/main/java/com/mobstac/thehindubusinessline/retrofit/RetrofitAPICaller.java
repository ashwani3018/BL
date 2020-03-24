package com.mobstac.thehindubusinessline.retrofit;


import android.content.Context;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by ashwani on 28/09/15.
 */
public final class RetrofitAPICaller {


    //public static String BASE_URL ="http://hinduappserver2.ninestars.in/hinduBL/service/api_v2/"; //Old
    public static String BASE_URL ="https://app.thehindubusinessline.com/hinduBL/service/api_v2/";
    public static String NEWS_DIGEST_URL = "http://thehindubusinessline.com/newsletter/";
    //public static String SEARCH_BY_ARTICLE_ID_URL = "http://hinduappserver1.ninestars.in/hindubl/service/api_v4/mobiles/search.php?guid="; //Old
    public static String SEARCH_BY_ARTICLE_ID_URL = "https://appsearch.thehindubusinessline.com/hindubl/service/api_v4/mobiles/search.php?guid=";
    public static String SEARCH_BY_ARTICLE_TITLE_URL = "https://www.thehindubusinessline.com/app/search.json?term=";
    public static String SEARCH_BY_ARTICLES_ID_URL = "https://www.thehindubusinessline.com/app/getArticleByIds.json?articleIds=";
    //public static String FORCE_UPGRADE = "http://hinduappserver2.ninestars.in/hinduBL/service/api_v1.002/forceUpgrade.php"; //Old
    public static String FORCE_UPGRADE = "https://app.thehindubusinessline.com/hinduBL/service/api_v1.002/forceUpgrade.php";


    private static RetrofitAPICaller sRetrofitApiCaller;

    private static Context sContext;

    private TheHinduApiService mApiEndPointInterface;

    private RetrofitAPICaller() {
        setupRetroAdapter();
    }

    public static RetrofitAPICaller getInstance(Context context) {
        sContext = context;
        if (sRetrofitApiCaller == null) {
            sRetrofitApiCaller = new RetrofitAPICaller();
        }
        return sRetrofitApiCaller;
    }

    private void setupRetroAdapter() {

        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://hinduappserver2.ninestars.in/hinduBL/service/api_v1.002/")
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mApiEndPointInterface = retrofit.create(TheHinduApiService.class);
    }

    public TheHinduApiService getWebserviceAPIs() {
        return mApiEndPointInterface;
    }

}
