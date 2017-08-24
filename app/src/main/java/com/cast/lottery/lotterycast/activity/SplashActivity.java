package com.cast.lottery.lotterycast.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cast.lottery.lotterycast.MainActivity;
import com.cast.lottery.lotterycast.R;
import com.cast.lottery.lotterycast.data.WebManager;
import com.cast.lottery.lotterycast.models.AppInfo;
import com.cast.lottery.lotterycast.utils.NetUtil;
import com.github.ybq.android.spinkit.SpinKitView;
import com.just.library.AgentWeb;
import com.tmall.ultraviewpager.UltraViewPager;

import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import rx.Subscriber;

public class SplashActivity extends Activity {

    private SpinKitView spkv;
    private ImageButton reTryBtn;
    private WebView webview;
    private FrameLayout layout;
    private AgentWeb mAgentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        layout = (FrameLayout) View.inflate(this, R.layout.activity_splash, null);
        setContentView(layout);
        ImageView wel = (ImageView) findViewById(R.id.wellcome_img);
        wel.setBackgroundResource(R.drawable.welcome);
        wel.setVisibility(View.VISIBLE);

        spkv = (SpinKitView) findViewById(R.id.spin_kit);
        spkv.setVisibility(View.VISIBLE);
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

    public void home(View view) {
        //点击时打开获取到的首页地址
    }

    public void back(View view) {
        //返回上一页
    }

    public void pay(View view) {
        //点击时打开获取到的充值地址
    }

    public void refresh(View view) {
        //刷新当前agentweb的url地址页面
    }



    public class Body {
        String appid;
    }

    WebViewClient mWebViewClient = new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            spkv.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            spkv.setVisibility(View.GONE);
        }
    };

    WebChromeClient mWebChromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if(newProgress==100){
                spkv.setVisibility(View.GONE);//加载完网页进度条消失
            }
            else{
                spkv.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                spkv.setProgress(newProgress);//设置进度值
            }

        }
    };


//"MmLT7778"
    private void initWebView() {


//        String appid = "2560035";
        String appid = "2017081009";

//        body.appname = "百度彩票";
//        String json = new Gson().toJson(body);
//        Log.d("getWebUrl",json);

        webview = (WebView)findViewById(R.id.webview);
        WebManager.getInstance().getWebUrlByGet(new Subscriber<Map>() {

            @Override
            public void onCompleted() {
                Log.d("getWebUrl", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                enterMockApp();
            }

            @Override

            public void onNext(final Map map) {
                if(map!=null&&map.get("isshowwap").equals("1")) {

                    SharedPreferences sharedPreferences = getSharedPreferences("lottery", MODE_PRIVATE);
                    boolean firstInit = sharedPreferences.getBoolean("first_init", true);
                    if(firstInit){
                        UltraViewPager ultraViewPager =  (UltraViewPager) findViewById(R.id.ultra_viewpager);
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
                        sharedPreferences.edit().putBoolean("first_init",false).commit();
                        ultraViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                if(position == 2){
                                    enterWebView((String) map.get("wapurl"));
                                }
                            }
                        });
                    }else{
                        enterWebView((String) map.get("wapurl"));
                    }
                }else {
                    enterMockApp();
                }
            }
        },appid);
    }

    private void enterMockApp() {
        spkv.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spkv.setVisibility(View.GONE);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
//                SplashActivity.this.finish();
            }},2000l);
    }
    private void enterWebView(String url) {
        mAgentWeb = AgentWeb.with(SplashActivity.this)//传入Activity
                .setAgentWebParent(layout, new FrameLayout.LayoutParams(-1, -1))//传入AgentWeb 的父控件 ，如果父控件为 RelativeLayout ， 那么第二参数需要传入 RelativeLayout.LayoutParams ,第一个参数和第二个参数应该对应。
                .closeProgressBar()// 不使用进度条
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .setSecutityType(AgentWeb.SecurityType.strict)
                .createAgentWeb()
                .ready()
                .go(url);
        findViewById(R.id.buttons).setVisibility(View.VISIBLE);
    }


    public class UltraPagerAdapter extends PagerAdapter {
        private final int[] imgRes = {R.drawable.s1x,R.drawable.s2x,R.drawable.s3x};


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
