package com.cast.lottery.lotterycast.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.cast.lottery.lotterycast.App;


/**
 * Created by junbo on 7/10/2016.
 */

public class NetUtil {

    public static boolean isNetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        Toast.makeText(App.getAppContext(),"网络出问题啦~~",Toast.LENGTH_LONG).show();
        return false;
    }

    public static String getBaseUrl(int type) {
        switch (type) {
            case Constants.NEWS_TYPE:
                return Constants.NETEAST_HOST;

        }
        return "";
    }
}
