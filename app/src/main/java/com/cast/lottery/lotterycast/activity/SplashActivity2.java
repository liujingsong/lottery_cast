package com.cast.lottery.lotterycast.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cast.lottery.lotterycast.MainActivity;
import com.cast.lottery.lotterycast.R;
import com.cast.lottery.lotterycast.data.WebManager;
import com.cast.lottery.lotterycast.utils.NetUtil;
import com.github.ybq.android.spinkit.SpinKitView;
import com.just.library.AgentWeb;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.bmob.v3.listener.GetCallback;
import devlight.io.library.ntb.NavigationTabBar;

public class SplashActivity2 extends AppCompatActivity {


    private AgentWeb agentWeb;
    private SpinKitView spkv;
    private ImageView wel;
    private NavigationTabBar ntbSample6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash2);
        ntbSample6 = (NavigationTabBar) findViewById(R.id.nab);
        spkv = (SpinKitView) findViewById(R.id.spin_kit);
        wel = (ImageView) findViewById(R.id.wellcome_img);
        wel.setVisibility(View.VISIBLE);
        fetchData();
        initNAB();
    }
    String homeUrl;
    String paywebUrl;
    Boolean isshow;
    private void fetchData() {
        spkv.setVisibility(View.VISIBLE);
        WebManager.getInstance().getAppInfo(this, "_User", "dq5aDDDH", new GetCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                try {
                    homeUrl = (String) jsonObject.get("webview");
                    paywebUrl = (String) jsonObject.get("payweb");
                    isshow = (Boolean) jsonObject.get("isshow");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(!TextUtils.isEmpty(homeUrl)&&isshow){
                    initWebView();
                }else{
                    enterMockApp();
                }

            }

            @Override
            public void onFailure(int i, String s) {
                Snackbar.make(SplashActivity2.this.getWindow().getDecorView().findViewById(android.R.id.content),"网络错误,请重新加载",Snackbar.LENGTH_INDEFINITE).setAction("重新加载",new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        netErrorCheckAndInitWebview();
                    }
                }).show();
            }
        });
    }


    private void enterMockApp() {
        spkv.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spkv.setVisibility(View.GONE);
                Intent intent = new Intent(SplashActivity2.this, MainActivity.class);
                startActivity(intent);
                SplashActivity2.this.finish();
            }},1200l);
    }

    private void netErrorCheckAndInitWebview() {
        if (NetUtil.isNetAvailable()) {
            fetchData();
        }
    }

    private void initWebView() {
        AgentWeb.PreAgentWeb preAgentWeb = AgentWeb.with(this).setAgentWebParent((ViewGroup) findViewById(R.id.web_parent), new LinearLayout.LayoutParams(-1, -1)).useDefaultIndicator().defaultProgressBarColor().setSecutityType(AgentWeb.SecurityType.strict).createAgentWeb();
        agentWeb = preAgentWeb.go(homeUrl);
        ntbSample6.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spkv.setVisibility(View.GONE);
            }
        },2000l);
    }



    private void initNAB() {

        final ArrayList<NavigationTabBar.Model> models6 = new ArrayList<>();
        models6.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.tab1), getResources().getColor(R.color.red2)
                ).title("首页").build()
        );
        models6.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.tab2), getResources().getColor(R.color.red2)
                ).title("返回").build()
        );
        models6.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.tab3), getResources().getColor(R.color.red2)
                ).title("充值").build()
        );
        models6.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.tab4), getResources().getColor(R.color.red2)
                ).title("刷新").build()
        );
        ntbSample6.setModels(models6);
        ntbSample6.setModelIndex(0, true);
        ntbSample6.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {
                if(index == 0){
                    agentWeb.getLoader().loadUrl(homeUrl);
                    model.setColor(getResources().getColor(R.color.red2));
                }else if(index == 1) {
                    agentWeb.back();
                }else if(index == 2){
                    agentWeb.getLoader().loadUrl(paywebUrl);
                    model.setColor(getResources().getColor(R.color.red2));
                }else if(index == 3){
                    agentWeb.getLoader().reload();
                }
            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {

            }
        });
        ntbSample6.setAnimationDuration(10);
    }

    private TextDrawable buildItemNameDrawable(String text) {
        return TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .fontSize(56) /* size in px */
                .width(160)  // width in px
                .height(160) // height in px
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRect(text, Color.TRANSPARENT);
    }

    @Override
    public void onBackPressed() {
        if(agentWeb!=null&&!agentWeb.back()){
            super.onBackPressed();
        }
    }
}
