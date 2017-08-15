package com.cast.lottery.lotterycast.data;


import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by kevin on 8/15/17.
 */

public interface WebService {
    @GET("check_and_get_url.php?type=android")
    Observable<Map> getWebUrl(@Query("appid") String appid);
}
