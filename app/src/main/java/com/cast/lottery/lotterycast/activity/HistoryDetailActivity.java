package com.cast.lottery.lotterycast.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LayoutAnimationController;

import com.cast.lottery.lotterycast.R;
import com.cast.lottery.lotterycast.adapter.HistoryDetailAdapter;
import com.cast.lottery.lotterycast.data.LotteryServiceManager;
import com.cast.lottery.lotterycast.models.LotteryHistory;
import com.cast.lottery.lotterycast.utils.LotteryUtils;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import yalantis.com.sidemenu.animation.FlipAnimation;

/**
 * Created by Kevin on 2017/8/4.
 */

public class HistoryDetailActivity extends AppCompatActivity {

    private String lotId;
    private List<LotteryHistory.ListEntity> hList = new ArrayList<>();
    private HistoryDetailAdapter historyDetailAdapter;
    SpinKitView spn_kit;
    public static void start(Context context, String lotId) {
        Bundle b = new Bundle();
        b.putString("id", lotId);
        Intent intent = new Intent(context, HistoryDetailActivity.class);
        intent.putExtra("Arguments", b);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_detail);
        setActionBar();
        spn_kit = (SpinKitView) findViewById(R.id.spin_kit);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyDetailAdapter = new HistoryDetailAdapter(hList, lotId);

        Bundle arguments = getIntent().getBundleExtra("Arguments");
        if (arguments != null) {
            lotId = arguments.getString("id");
            fetchData(lotId,"1");
        }

        recyclerView.setAdapter(historyDetailAdapter);
    }

    public void setActionBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    public void fetchData(final String lotId,final String page) {
        spn_kit.setVisibility(View.VISIBLE);
        LotteryServiceManager.getInstance().getHistory360(new Subscriber<LotteryHistory>() {
            @Override
            public void onCompleted() {
                spn_kit.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                spn_kit.setVisibility(View.GONE);
            }

            @Override
            public void onNext(LotteryHistory h) {
                spn_kit.setVisibility(View.GONE);
                hList.clear();
                hList.addAll(h.getList());
                historyDetailAdapter.notifyDataSetChanged();
            }
        }, lotId, page);
    }
}
