package com.cast.lottery.lotterycast.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cast.lottery.lotterycast.R;
import com.cast.lottery.lotterycast.models.Lottery;
import com.cast.lottery.lotterycast.models.LotteryDetail;
import com.cast.lottery.lotterycast.utils.LotteryUtils;
import com.cast.lottery.lotterycast.widgets.KJLineItemView;
import com.cast.lottery.lotterycast.widgets.LotteryDetailView;

import java.util.List;

/**
 * Created by kevin on 8/6/17.
 */

public class KJDetailDialog extends Dialog {

    private LotteryDetail ld;
    private KJLineItemView kjLineItemView;
    private LotteryDetailView lotteryDetailView;
    private DisplayMetrics displayMetrics;

    ImageView tName;
    TextView tPhase;
    TextView tTime;
    TextView tPool;
    TextView tSale;

    public KJDetailDialog(Activity context){
        super(context);
        displayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(displayMetrics.widthPixels, (int) (displayMetrics.heightPixels/1.5f));
        View layout = getLayoutInflater().inflate(R.layout.content_lottery_detail, null);
        setContentView(layout,layoutParams);
        kjLineItemView = (KJLineItemView) findViewById(R.id.view_detail_ball);
        lotteryDetailView = (LotteryDetailView) findViewById(R.id.detail_detail);
         tName = (ImageView) findViewById(R.id.text_detail_name);
         tPhase = (TextView) findViewById(R.id.text_detail_phase);
         tTime = (TextView) findViewById(R.id.text_detail_timedraw);
         tPool = (TextView) findViewById(R.id.text_detail_pool);
         tSale = (TextView) findViewById(R.id.text_detail_sale);
    }

    public void setLotteryDetailView(LotteryDetail lotteryDetail){
        this.ld = lotteryDetail;
    }

    private KJDetailDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    private KJDetailDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void bind(){
        kjLineItemView.addViewList(LotteryUtils.getBall(new Lottery.IEntity() {
            @Override
            public String getLotName() {
                return ld.getLotName();
            }

            @Override
            public String getIssue() {
                return ld.getIssue();
            }

            @Override
            public String getBalls() {
                return ld.getCode();
            }

            @Override
            public String getDate() {
                return ld.getDate();
            }
        }));
        kjLineItemView.setWidthHeight(displayMetrics.widthPixels, displayMetrics.heightPixels);
        List lotteryDetailLevel = ld.getLevel();
        if (lotteryDetailLevel != null)
            lotteryDetailLevel.add(0, new LotteryDetail.LevelEntity(getContext().getString(R.string.lottery_item), getContext().getString(R.string.lottery_count), getContext().getString(R.string.lottery_num)));

        lotteryDetailView.setList(lotteryDetailLevel,displayMetrics.widthPixels);

        tName.setImageDrawable(TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .fontSize(46) /* size in px */
                .width(160)  // width in px
                .height(160) // height in px
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(ld.getLotName(), getContext().getResources().getColor(R.color.color_ffca28)));

        tPhase.setText(getContext().getString(R.string.lottery_issue, ld.getIssue()));
        tPool.setText(getContext().getString(R.string.lottery_pool, ld.getMoney()));
        tSale.setText(getContext().getString(R.string.lottery_sale, ld.getSale()));
        tTime.setText(getContext().getString(R.string.lottery_time, ld.getDate()));
    }
}
