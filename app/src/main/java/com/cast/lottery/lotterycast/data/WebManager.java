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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .readTimeout(3000, TimeUnit.MILLISECONDS)
                .cache(new Cache(CacheUtils.getDir(App.getAppContext()), CacheUtils.getCacheSize()))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Log.d("http-request",request.toString());
                        if (!NetUtil.isNetAvailable()) {
                            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                        }
                        Response response = chain.proceed(request);

                        if (NetUtil.isNetAvailable()) {
                            return response.newBuilder()
                                    .addHeader("Cache-Control", "max-age=0")
                                    .removeHeader("Pragma")
                                    .build();
                        } else {
                            return response.newBuilder()
                                    .addHeader("Cache-Control", "public, only-if-cached, max-stale=60*60*24*365")
                                    .removeHeader("pragma")
                                    .build();
                        }

                    }
                }).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://103.1.43.189/Lottery_server/")
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

    public void getWebUrl(Subscriber subscriber,String appid){
        webService.getWebUrl(appid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
}
