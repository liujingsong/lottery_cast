package com.cast.lottery.lotterycast;

import android.app.Application;
import android.content.Context;

import cn.bmob.v3.Bmob;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by junbo on 2/11/2016.
 */

public class App extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        Bmob.initialize(this, "d0b51856061702f59f6c148f47e1c5b9");

    }

    public static Context getAppContext() {
        return appContext;
    }
}
