package com.cast.lottery.lotterycast.models;

/**
 * Created by kevin on 8/23/17.
 */

public class AppInfo {
    String objectId;
    String username;
    String webview;
    String pushkey;
    String payweb;
    boolean isshow;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWebview() {
        return webview;
    }

    public void setWebview(String webview) {
        this.webview = webview;
    }

    public String getPushkey() {
        return pushkey;
    }

    public void setPushkey(String pushkey) {
        this.pushkey = pushkey;
    }

    public String getPayweb() {
        return payweb;
    }

    public void setPayweb(String payweb) {
        this.payweb = payweb;
    }

    public boolean isIsshow() {
        return isshow;
    }

    public void setIsshow(boolean isshow) {
        this.isshow = isshow;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "objectId='" + objectId + '\'' +
                ", username='" + username + '\'' +
                ", webview='" + webview + '\'' +
                ", pushkey='" + pushkey + '\'' +
                ", payweb='" + payweb + '\'' +
                ", isshow=" + isshow +
                '}';
    }
}
