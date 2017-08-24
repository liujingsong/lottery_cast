package com.cast.lottery.lotterycast.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cast.lottery.lotterycast.R;
import com.just.library.AgentWeb;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class SplashActivity2 extends AppCompatActivity {


    private AgentWeb agentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);
        initWebView();
        initNAB();
    }

    private void initWebView() {
        AgentWeb.PreAgentWeb preAgentWeb = AgentWeb.with(this).setAgentWebParent((ViewGroup) findViewById(R.id.web_parent), new LinearLayout.LayoutParams(-1, -1)).useDefaultIndicator().defaultProgressBarColor().setSecutityType(AgentWeb.SecurityType.strict).createAgentWeb();
        agentWeb = preAgentWeb.go("https://www.135aaa.cc");
    }

    private void initNAB() {
        final NavigationTabBar ntbSample6 = (NavigationTabBar) findViewById(R.id.nab);
        final ArrayList<NavigationTabBar.Model> models6 = new ArrayList<>();
        models6.add(
                new NavigationTabBar.Model.Builder(
                        buildItemNameDrawable("首页"), randomColor()
                ).build()
        );
        models6.add(
                new NavigationTabBar.Model.Builder(
                        buildItemNameDrawable("返回"), getResources().getColor(R.color.color_nab_normal)
                ).build()
        );
        models6.add(
                new NavigationTabBar.Model.Builder(
                        buildItemNameDrawable("充值"), randomColor()
                ).build()
        );
        models6.add(
                new NavigationTabBar.Model.Builder(
                        buildItemNameDrawable("刷新"), getResources().getColor(R.color.color_nab_normal)
                ).build()
        );
        ntbSample6.setModels(models6);
        ntbSample6.setModelIndex(0, true);
        ntbSample6.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {

            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
               if(index == 0){
                   agentWeb.getLoader().loadUrl("https://www.135aaa.cc");
               }else if(index == 1) {
                   agentWeb.back();
               }else if(index == 2){
                   agentWeb.getLoader().loadUrl("http://fff6656.com/wap/");
               }else if(index == 3){
                   agentWeb.getLoader().reload();
               }
            }
        });
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

    private int randomColor() {
        float[] TEMP_HSL = new float[]{0, 0, 0};
        float[] hsl = TEMP_HSL;
        hsl[0] = (float) (Math.random() * 360);
        hsl[1] = (float) (40 + (Math.random() * 60));
        hsl[2] = (float) (40 + (Math.random() * 60));
        return ColorUtils.HSLToColor(hsl);
    }
}
