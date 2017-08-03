package com.cast.lottery.lotterycast.data;



import com.cast.lottery.lotterycast.models.News;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;


/**
 * Created by junbo on 30/9/2016.
 */

public interface NewsService {
    @GET("nc/article/{type}/{id}/{startPage}-20.html")
    @Headers({
            "User-Agent: Retrofit-Sample-App"
    })
    Observable<Map<String, List<News>>> getNewsList(@Header("Cache-Control") String cacheControl,
                                                    @Path("type") String type,
                                                    @Path("id") String id,
                                                    @Path("startPage") int startPage);

}
