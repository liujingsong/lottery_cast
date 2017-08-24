package com.cast.lottery.lotterycast.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cast.lottery.lotterycast.MainActivity;
import com.cast.lottery.lotterycast.R;
import com.cast.lottery.lotterycast.data.WebManager;

import com.cast.lottery.lotterycast.utils.NetUtil;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.tmall.ultraviewpager.UltraViewPager;

import org.json.JSONObject;

import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.GetCallback;
import rx.Subscriber;

public class SplashActivity extends Activity {

    private SpinKitView spkv;
    private ImageButton reTryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ImageView wel = (ImageView) findViewById(R.id.wellcome_img);
        wel.setBackgroundResource(R.drawable.welcome);
        wel.setVisibility(View.VISIBLE);

        spkv = (SpinKitView) findViewById(R.id.spin_kit);
        reTryBtn = (ImageButton) findViewById(R.id.btn_retry);

        netErrorCheckAndInitWebview();

    }

    private void netErrorCheckAndInitWebview() {
        if (!NetUtil.isNetAvailable()) {
            reTryBtn.setVisibility(View.VISIBLE);
            reTryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (NetUtil.isNetAvailable()) {
                        initWebView();
                    }
                    RotateAnimation ra = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    ra.setDuration(2000l);
                    v.startAnimation(ra);

                }
            });
        } else {
            initWebView();
        }
    }


    public class Body {
        String appflag;
        String appname;
    }


//"MmLT7778"
    private void initWebView() {

        Body body = new Body();
        body.appflag = "0";
        body.appname = "PC蛋蛋";
//        body.appname = "百度彩票";
        String json = new Gson().toJson(body);
        Log.d("getWebUrl", json);

        final WebView webview = (WebView) findViewById(R.id.webview);
        WebManager.getInstance().getWebUrl(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                Log.d("getWebUrl", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Map map) {
                Map data = (Map) map.get("appInfo");
                if (data != null && (data.get("state")).equals("2")) {

                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.getSettings().setDomStorageEnabled(true);
//                    webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//                    webview.getSettings().setSupportMultipleWindows(true);
                    webview.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    });
                    webview.setWebChromeClient(new WebChromeClient() {
                        @Override
                        public void onProgressChanged(WebView view, int newProgress) {
                            if (newProgress == 100) {
                                spkv.setVisibility(View.GONE);//加载完网页进度条消失
                            } else {
                                spkv.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                                spkv.setProgress(newProgress);//设置进度值
                            }

                        }
                    });
                    webview.loadUrl((String) data.get("jumplink"));
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }
                    }, 2000l);
                }
            }
        }, json);


        SharedPreferences sharedPreferences = getSharedPreferences("lottery", MODE_PRIVATE);
        boolean firstInit = sharedPreferences.getBoolean("first_init", true);
        if (firstInit) {
            UltraViewPager ultraViewPager = (UltraViewPager) findViewById(R.id.ultra_viewpager);
            ultraViewPager.setVisibility(View.VISIBLE);
            ultraViewPager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
            PagerAdapter adapter = new UltraPagerAdapter();
            ultraViewPager.setAdapter(adapter);

            ultraViewPager.initIndicator();
            ultraViewPager.getIndicator()
                    .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                    .setFocusColor(Color.GREEN)
                    .setNormalColor(Color.WHITE)
                    .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
            ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            ultraViewPager.getIndicator().build();

            ultraViewPager.setInfiniteLoop(false);
            sharedPreferences.edit().putBoolean("first_init", false).commit();
            ultraViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (position == 2) {
                        enterWebView(webview);
                    }
                }
            });
        } else {
            enterWebView(webview);
        }

    }

    private void enterWebView(final WebView webview) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                webview.setVisibility(View.VISIBLE);
            }
        }, 2000l);
    }


    public class UltraPagerAdapter extends PagerAdapter {

        private final int[] imgRes = {R.drawable.s1, R.drawable.s2, R.drawable.s3};

        public UltraPagerAdapter() {
        }

        @Override
        public int getCount() {
            return imgRes.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ImageView imageView = new ImageView(SplashActivity.this);
            imageView.setLayoutParams(layoutParams);
            imageView.setBackgroundResource(imgRes[position]);

            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
