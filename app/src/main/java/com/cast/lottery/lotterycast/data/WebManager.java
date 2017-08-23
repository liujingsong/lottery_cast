package com.cast.lottery.lotterycast.data;

import android.util.Log;

import com.cast.lottery.lotterycast.App;
import com.cast.lottery.lotterycast.utils.CacheUtils;
import com.cast.lottery.lotterycast.utils.NetUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kevin on 8/15/17.
 */

public class WebManager {

    private Retrofit retrofit;
    private WebService webService;

    private WebManager(){
        init();
    }

    private void init() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .readTimeout(3000, TimeUnit.MILLISECONDS)
                .cache(new Cache(CacheUtils.getDir(App.getAppContext()), CacheUtils.getCacheSize())).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://appid.qq-app.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        webService = retrofit.create(WebService.class);
    }

    private static WebManager instance;
    public static WebManager getInstance() {
        if (instance == null) {
            synchronized (WebManager.class) {
                if (instance == null) {
                    instance = new WebManager();
                }
            }
        }
        return instance;
    }
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    public void getWebUrlByPost(Subscriber subscriber,String body){
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE, body);
        webService.getWebUrl(requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    public void getWebUrlByGet(Subscriber subscriber,String appid){
        webService.getWebUrl(appid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
