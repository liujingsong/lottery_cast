package com.cast.lottery.lotterycast;

import android.animation.Animator;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cast.lottery.lotterycast.fragment.BaseContentFragment;
import com.cast.lottery.lotterycast.fragment.HistoryFragment;
import com.cast.lottery.lotterycast.fragment.LatestFragment;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import io.codetail.animation.ViewAnimationUtils;
import yalantis.com.sidemenu.interfaces.Resourceble;
import yalantis.com.sidemenu.interfaces.ScreenShotable;
import yalantis.com.sidemenu.model.SlideMenuItem;
import yalantis.com.sidemenu.util.ViewAnimator;


public class MainActivity extends AppCompatActivity implements ViewAnimator.ViewAnimatorListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private List<SlideMenuItem> list = new ArrayList<>();
    private ViewAnimator viewAnimator;
    private LinearLayout linearLayout;
    private SpinKitView skv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseContentFragment defaultContent = LatestFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, defaultContent)
                .commit();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        linearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
            }
        });

        skv = (SpinKitView) findViewById(R.id.spin_kit);

        setActionBar();
        createMenuList();
        viewAnimator = new ViewAnimator<>(this, list, defaultContent, drawerLayout, this);
    }

    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(BaseContentFragment.CLOSE, R.drawable.icn_close);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(BaseContentFragment.LATEST, buildTextMenuItem("最新"));
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(BaseContentFragment.HISTORY, buildTextMenuItem("历史"));
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(BaseContentFragment.DETAIL, buildTextMenuItem("分析"));
        list.add(menuItem3);
    }

    private TextDrawable buildTextMenuItem(String text) {
        return TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .fontSize(48) /* size in px */
                .width(160)  // width in px
                .height(160) // height in px
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRect(text, Color.TRANSPARENT);
    }


    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset > 0.6 && linearLayout.getChildCount() == 0)
                    viewAnimator.showMenuContent();
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ScreenShotable replaceFragment(BaseContentFragment foreground,ScreenShotable background, int topPosition) {

        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Animator animator = ViewAnimationUtils.createCircularReveal(view, 0, topPosition, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

        findViewById(R.id.content_overlay).setBackgroundDrawable(new BitmapDrawable(getResources(), background.getBitmap()));
        animator.start();

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,  foreground).commit();
        return foreground;
    }

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        switch (slideMenuItem.getName()) {
            case BaseContentFragment.CLOSE:
                return screenShotable;
            case BaseContentFragment.LATEST:
                return replaceFragment(LatestFragment.newInstance(),screenShotable, position);
            case BaseContentFragment.HISTORY:
                return replaceFragment(HistoryFragment.newInstance(),screenShotable, position);
            default:
                return screenShotable;
        }
    }

    @Override
    public void disableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(false);

    }

    @Override
    public void enableHomeButton() {
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout.closeDrawers();

    }

    @Override
    public void addViewToContainer(View view) {
        linearLayout.addView(view);
    }
}