package com.cast.lottery.lotterycast.fragment;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cast.lottery.lotterycast.R;
import com.cast.lottery.lotterycast.data.LotteryServiceManager;
import com.cast.lottery.lotterycast.models.Lottery;
import com.cast.lottery.lotterycast.utils.LotteryUtils;
import com.cast.lottery.lotterycast.widgets.KJLineItemView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by Kevin on 2017/8/2.
 */

public class LatestFragment extends BaseContentFragment {
    private LatestLotteryAdapter latestLotteryAdapter;

    public LatestFragment(){

    }

    public static BaseContentFragment newInstance() {
        return new LatestFragment();
    }
    List<Lottery.IEntity> data = new ArrayList<>();

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_latest, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        latestLotteryAdapter = new LatestLotteryAdapter();
        recyclerView.setAdapter(latestLotteryAdapter);

        return rootView;
    }

    public void fetchData(){
        LotteryServiceManager.getInstance().getLastData360(new Subscriber<List<Lottery.IEntity>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Lottery.IEntity> list) {
                Log.d("getLastData360",list.toString());
                data.clear();
                data.addAll(list);
                latestLotteryAdapter.notifyDataSetChanged();
            }
        });
    }



    private class LatestLotteryAdapter extends RecyclerView.Adapter<LastLotteryHolder> {


        @Override
        public LastLotteryHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.latest_list_item, parent, false);
            return new LastLotteryHolder(view);
        }

        @Override
        public void onBindViewHolder(LastLotteryHolder holder, int pos) {
            Lottery.IEntity iEntity = LatestFragment.this.data.get(pos);

            holder.name.setImageDrawable(buildItemNameDrawable(iEntity.getLotName()));
            holder.phase.setText("第" + iEntity.getIssue() + "期");
            holder.time.setText("开奖日期" + iEntity.getDate());
            DisplayMetrics outMetrics = null;
            outMetrics = new DisplayMetrics();
            ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            holder.ball.setWidthHeight(outMetrics.widthPixels, outMetrics.heightPixels);
            holder.ball.addViewList(LotteryUtils.getBall(iEntity));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }

    private class LastLotteryHolder extends RecyclerView.ViewHolder {


        private ImageView name;
        private  TextView phase;
        private  TextView time;
        private  KJLineItemView ball;

        public LastLotteryHolder(View itemView) {
            super(itemView);
            name = (ImageView) itemView.findViewById(R.id.name);
            phase = (TextView) itemView.findViewById(R.id.text_phase);
            time = (TextView) itemView.findViewById(R.id.text_timedraw);
            ball = (KJLineItemView) itemView.findViewById(R.id.view_ball);
        }
    }



    @Override
    public View getContainerView(View view) {
        return view.findViewById(R.id.container);
    }

    private TextDrawable buildItemNameDrawable(String text) {
        return TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .fontSize(46) /* size in px */
                .width(160)  // width in px
                .height(160) // height in px
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(text, getContext().getResources().getColor(R.color.color_ffca28));
    }
}
