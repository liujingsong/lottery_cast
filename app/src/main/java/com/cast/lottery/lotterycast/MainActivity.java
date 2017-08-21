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
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cast.lottery.lotterycast.fragment.AboutFragment;
import com.cast.lottery.lotterycast.fragment.BaseContentFragment;
import com.cast.lottery.lotterycast.fragment.HistoryFragment;
import com.cast.lottery.lotterycast.fragment.LatestFragment;
import com.cast.lottery.lotterycast.fragment.NewsFragment;
import com.cast.lottery.lotterycast.models.Lottery;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.HashMap;
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
    private ImageButton btnRefresh;
    List<Lottery.IEntity> data = new ArrayList<>();
    private Toolbar toolbar;

    public synchronized List<Lottery.IEntity> getData() {
        return data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        setOnRefreshListener();
        setActionBar();
        createMenuList();

        BaseContentFragment defaultContent = showDefaultFragment();
        viewAnimator = new ViewAnimator<>(this, list, defaultContent, drawerLayout, this);
    }

    private BaseContentFragment showDefaultFragment() {
        BaseContentFragment defaultContent = getFragment(LatestFragment.class.getName());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, defaultContent)
                .commit();
        toolbar.setTitle("最新开奖");
        return defaultContent;
    }


    private void createMenuList() {
        SlideMenuItem menuItem0 = new SlideMenuItem(BaseContentFragment.CLOSE, R.drawable.icn_close);
        list.add(menuItem0);
        SlideMenuItem menuItem = new SlideMenuItem(BaseContentFragment.LATEST, buildTextMenuItem("最新"));
        list.add(menuItem);
        SlideMenuItem menuItem2 = new SlideMenuItem(BaseContentFragment.HISTORY, buildTextMenuItem("历史"));
        list.add(menuItem2);
        SlideMenuItem menuItem3 = new SlideMenuItem(BaseContentFragment.NEWS, buildTextMenuItem("资讯"));
        list.add(menuItem3);
        SlideMenuItem menuItem4 = new SlideMenuItem(BaseContentFragment.ABOUT, buildTextMenuItem("关于"));
        list.add(menuItem4);
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("最新开奖");
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

    private ScreenShotable replaceFragment(BaseContentFragment foreground, ScreenShotable background, int topPosition) {

        View view = findViewById(R.id.content_frame);
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Animator animator = ViewAnimationUtils.createCircularReveal(view, 0, topPosition, 0, finalRadius);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.setDuration(ViewAnimator.CIRCULAR_REVEAL_ANIMATION_DURATION);

        findViewById(R.id.content_overlay).setBackgroundDrawable(new BitmapDrawable(getResources(), background.getBitmap()));
        animator.start();

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, foreground).commit();
        return foreground;
    }

    private HashMap<String, BaseContentFragment> fragments = new HashMap<>();

    @Override
    public ScreenShotable onSwitch(Resourceble slideMenuItem, ScreenShotable screenShotable, int position) {
        switch (slideMenuItem.getName()) {
            case BaseContentFragment.CLOSE:
                return screenShotable;
            case BaseContentFragment.LATEST:
                toolbar.setTitle("最新开奖");
                return replaceFragment(getFragment(LatestFragment.class.getName()), screenShotable, position);
            case BaseContentFragment.HISTORY:
                toolbar.setTitle("历史开奖");
                return replaceFragment(getFragment(HistoryFragment.class.getName()), screenShotable, position);

            case BaseContentFragment.NEWS:
                toolbar.setTitle("资讯");
                return replaceFragment(getFragment(NewsFragment.class.getName()), screenShotable, position);
            case BaseContentFragment.ABOUT:
                toolbar.setTitle("关于");
                return replaceFragment(getFragment(AboutFragment.class.getName()),screenShotable,position);
            default:
                return screenShotable;
        }
    }

    private BaseContentFragment getFragment(String key) {
        BaseContentFragment baseContentFragment = fragments.get(key);
        if (baseContentFragment == null) {
            try {
                baseContentFragment = (BaseContentFragment) Class.forName(key).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fragments.put(key, baseContentFragment);
        }
        return baseContentFragment;
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


    public void setOnRefreshListener() {
        btnRefresh = (ImageButton) findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RotateAnimation ra = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                ra.setDuration(2000l);
                v.startAnimation(ra);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (viewAnimator.getScreenShotable() instanceof LatestFragment) {
            finish();
        } else {
            showDefaultFragment();
        }
    }
}
