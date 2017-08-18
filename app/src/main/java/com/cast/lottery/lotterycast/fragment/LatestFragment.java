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
import com.cast.lottery.lotterycast.MainActivity;
import com.cast.lottery.lotterycast.R;
import com.cast.lottery.lotterycast.activity.HistoryDetailActivity;
import com.cast.lottery.lotterycast.activity.KJDetailDialog;
import com.cast.lottery.lotterycast.data.LotteryServiceManager;
import com.cast.lottery.lotterycast.listener.RecyclerItemClickListener;
import com.cast.lottery.lotterycast.models.Ball;
import com.cast.lottery.lotterycast.models.Lottery;
import com.cast.lottery.lotterycast.models.LotteryDetail;
import com.cast.lottery.lotterycast.utils.BitmapUtils;
import com.cast.lottery.lotterycast.utils.LotteryUtils;
import com.cast.lottery.lotterycast.widgets.KJLineItemView;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.BubbleValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.BubbleChartView;
import rx.Subscriber;

/**
 * Created by Kevin on 2017/8/2.
 */

public class LatestFragment extends BaseContentFragment {
    private LatestLotteryAdapter latestLotteryAdapter;
    private SpinKitView spn_kit;
    private KJDetailDialog kjDetailDialog;

    public LatestFragment() {

    }

    public static BaseContentFragment newInstance() {
        return new LatestFragment();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getData().size() == 0) {
            fetchData();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_latest, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        latestLotteryAdapter = new LatestLotteryAdapter();
        recyclerView.setAdapter(latestLotteryAdapter);
        spn_kit = (SpinKitView) rootView.findViewById(R.id.spin_kit);
        setOnItemClick(recyclerView);
        return rootView;
    }

    public void fetchData() {
        spn_kit.setVisibility(View.VISIBLE);
        LotteryServiceManager.getInstance().getLastData360(new Subscriber<List<Lottery.IEntity>>() {
            @Override
            public void onCompleted() {
                spn_kit.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                spn_kit.setVisibility(View.GONE);
            }

            @Override
            public void onNext(List<Lottery.IEntity> list) {
                Log.d("getLastData360", list.toString());
                getData().clear();
                getData().addAll(list);
                latestLotteryAdapter.notifyDataSetChanged();
                spn_kit.setVisibility(View.GONE);
            }
        });
    }

    private void setOnItemClick(RecyclerView recyclerView) {
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LotteryServiceManager.getInstance().getLotteryDetail(new Subscriber<LotteryDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LotteryDetail lotteryDetail) {
                        if (kjDetailDialog == null) {
                            kjDetailDialog = new KJDetailDialog(getActivity());
                        }
                        kjDetailDialog.setLotteryDetailView(lotteryDetail);
                        kjDetailDialog.bind();
                        kjDetailDialog.setCanceledOnTouchOutside(false);
                        kjDetailDialog.show();

                    }
                }, LotteryUtils.getId(getData().get(position).getLotName()), getData().get(position).getIssue());
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
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
            Lottery.IEntity iEntity = LatestFragment.this.getData().get(pos);

            holder.name.setImageDrawable(buildItemNameDrawable(iEntity.getLotName()));
            holder.phase.setText("第" + iEntity.getIssue() + "期");
            holder.time.setText("开奖日期" + iEntity.getDate());
            DisplayMetrics outMetrics = null;
            outMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            holder.ball.setWidthHeight(outMetrics.widthPixels, outMetrics.heightPixels);
            List<Ball> balls = LotteryUtils.getBall(iEntity);
            holder.ball.addViewList(balls);
            fillChart(holder.chart, balls);
        }

        public void fillChart(BubbleChartView chart, List<Ball> balls) {
            List<BubbleValue> values = new ArrayList<BubbleValue>();
            Ball ball;
            for (int i = 1; i <= balls.size(); ++i) {
                ball = balls.get(i - 1);
                if (!isNumeric(ball.getNum()))
                    break;

                BubbleValue value = new BubbleValue(i, Float.parseFloat(ball.getNum()) , Float.parseFloat(ball.getNum()) * 18);

                value.setLabel(ball.getNum());
                if (ball.isBlue()) {
                    value.setColor(Color.BLUE);
                } else if (ball.isRed()) {
                    value.setColor(Color.RED);
                } else {
                    value.setColor(Color.BLACK);
                }
                value.setShape(ValueShape.CIRCLE);
                values.add(value);
            }
            BubbleChartData data = new BubbleChartData(values);
            data.setHasLabels(true);
            data.setHasLabelsOnlyForSelected(false);
            data.setValueLabelBackgroundAuto(false);
            data.setValueLabelBackgroundColor(Color.TRANSPARENT);
            data.setMinBubbleRadius(20);
            data.setBubbleScale(0.5f);

            Axis axisY = new Axis().setHasLines(true);
            axisY.setName("走势");
            data.setAxisYLeft(axisY);

            chart.setBubbleChartData(data);

        }

        private int getSign() {
            int[] sign = new int[]{-1, 1};
            return sign[Math.round((float) Math.random())];
        }

        public boolean isNumeric(String str) {
            for (int i = 0; i < str.length(); i++) {
                if (!Character.isDigit(str.charAt(i))) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int getItemCount() {
            return getData().size();
        }
    }

    private class LastLotteryHolder extends RecyclerView.ViewHolder {


        private ImageView name;
        private TextView phase;
        private TextView time;
        private KJLineItemView ball;
        private BubbleChartView chart;

        public LastLotteryHolder(View itemView) {
            super(itemView);
            name = (ImageView) itemView.findViewById(R.id.name);
            phase = (TextView) itemView.findViewById(R.id.text_phase);
            time = (TextView) itemView.findViewById(R.id.text_timedraw);
            ball = (KJLineItemView) itemView.findViewById(R.id.view_ball);
            chart = (BubbleChartView) itemView.findViewById(R.id.chart);
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

    @Override
    public void onRefresh() {
        fetchData();
    }
}
