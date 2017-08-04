package com.cast.lottery.lotterycast.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cast.lottery.lotterycast.App;
import com.cast.lottery.lotterycast.R;
import com.cast.lottery.lotterycast.models.HistoryItem;
import com.cast.lottery.lotterycast.models.LotteryHistory;
import com.cast.lottery.lotterycast.utils.LotteryUtils;

import java.util.List;


/**
 * Created by junbo on 7/11/2016.
 */

public class HistoryDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String id;
    private List<LotteryHistory.ListEntity> list;

    public HistoryDetailAdapter(List<LotteryHistory.ListEntity> list, String lotId) {
        this.list = list;
        this.id = lotId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(App.getAppContext(),R.layout.history_detail_item,null);

        return new HistoryFragmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HistoryFragmentViewHolder historyHolder = (HistoryFragmentViewHolder) holder;
        LotteryHistory.ListEntity o = list.get(position);
        HistoryItem item = LotteryUtils.convertHistoryItem(id, o);

        if (item == null) {
            historyHolder.time.setText("开奖日期  " + LotteryUtils.getDate(o.getEndTime()));
            historyHolder.phase.setText("第" + o.getIssue() + "期");
            historyHolder.red.setText(o.getWinNumber());
            historyHolder.blue.setText(o.getBallNumber());
        } else {
            historyHolder.time.setText("开奖日期  " + LotteryUtils.getDate(item.getTime()));
            historyHolder.phase.setText("第" + item.getIssue() + "期");
            historyHolder.red.setText(item.getRed());
            historyHolder.blue.setText(item.getBlue());
        }
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class HistoryFragmentViewHolder extends RecyclerView.ViewHolder {

        TextView time;
        TextView phase;
        TextView red;
        TextView blue;

        public HistoryFragmentViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.text_history_timedraw);
            phase = (TextView) itemView.findViewById(R.id.text_history_phase);
            red = (TextView) itemView.findViewById(R.id.text_history_red);
            blue = (TextView) itemView.findViewById(R.id.text_history_blue);
        }
    }


}
