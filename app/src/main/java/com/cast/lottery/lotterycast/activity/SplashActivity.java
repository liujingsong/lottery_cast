package com.cast.lottery.lotterycast.activity;

import android.app.Activity;
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
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.cast.lottery.lotterycast.R;
import com.cast.lottery.lotterycast.data.WebManager;
import com.tmall.ultraviewpager.UltraViewPager;

import java.util.Map;

import rx.Subscriber;

public class SplashActivity extends Activity {

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
        initWebView();
    }

    private void initWebView() {

        final WebView webview =  (WebView)findViewById(R.id.webview);
        WebManager.getInstance().getWebUrl(new Subscriber<Map>() {
            @Override
            public void onCompleted() {
                Log.d("getWebUrl","onCompleted");
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Map map) {
                Map data = (Map) map.get("data");
                if(data!=null&&(data.get("show_url")).equals("1")) {

                    webview.getSettings().setJavaScriptEnabled(true);
                    webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                    webview.getSettings().setSupportMultipleWindows(true);
                    webview.setWebViewClient(new WebViewClient());
                    webview.setWebChromeClient(new WebChromeClient());
                    webview.loadUrl((String) data.get("url"));
                }
            }
        },"201772609");



        SharedPreferences sharedPreferences = getSharedPreferences("lottery", MODE_PRIVATE);
        boolean firstInit = sharedPreferences.getBoolean("first_init", true);
        if(firstInit){
            UltraViewPager ultraViewPager =  (UltraViewPager) findViewById(R.id.ultra_viewpager);
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
            sharedPreferences.edit().putBoolean("first_init",false).commit();
            ultraViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if(position == 2){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                webview.setVisibility(View.VISIBLE);
                            }
                        }, 2000l);
                    }
                }
            });
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    webview.setVisibility(View.VISIBLE);
                }
            }, 2000l);
        }

    }


    public  class UltraPagerAdapter extends PagerAdapter {

        private final int[] imgRes = {R.drawable.s1,R.drawable.s2,R.drawable.s3};

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
