package com.cast.lottery.lotterycast;

import android.app.Application;
import android.content.Context;

/**
 * Created by junbo on 2/11/2016.
 */

public class App extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }

    public static Context getAppContext() {
        return appContext;
    }
}
