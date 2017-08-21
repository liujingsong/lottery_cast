package com.cast.lottery.lotterycast.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cast.lottery.lotterycast.R;
import com.cast.lottery.lotterycast.data.NewsServiceManager;
import com.cast.lottery.lotterycast.models.News;
import com.cast.lottery.lotterycast.utils.Constants;
import com.github.ybq.android.spinkit.SpinKitView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by Kevin on 2017/8/21.
 */

public class NewsFragment extends BaseContentFragment {

    private List<News> news = new ArrayList<>();
    private RecyclerView listView;
    private NewsAdapter newsAdapter;
    private SpinKitView skv;

    public static BaseContentFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public View getContainerView(View view) {
        return view.findViewById(R.id.container);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_news, null);
        listView = (RecyclerView) view.findViewById(R.id.list_view);
        listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        skv = (SpinKitView) view.findViewById(R.id.spin_kit);
        newsAdapter = new NewsAdapter();
        listView.setAdapter(newsAdapter);
        return view;
    }

   private class NewsAdapter extends RecyclerView.Adapter<NewsHolder>{

       @Override
       public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           View view = View.inflate(getContext(), R.layout.news_item, null);
           return new NewsHolder(view);
       }

       @Override
       public void onBindViewHolder(NewsHolder holder, int position) {
           News n = NewsFragment.this.news.get(position);
           holder.titleTv.setText(n.getTitle());
           holder.publishTimeTv.setText(n.getPtime());
           Picasso.with(getContext()).load(n.getImgsrc()).resize(200, 200).into(holder.img);
       }

       @Override
       public int getItemCount() {
           Log.d("News","news.size():"+news.size());
           return news.size();
       }


   }


private class NewsHolder extends RecyclerView.ViewHolder{

    protected ImageView img;
    protected TextView titleTv;
    protected TextView publishTimeTv;

    public NewsHolder(View itemView) {
        super(itemView);
        img = (ImageView) itemView.findViewById(R.id.imageView);
        titleTv = (TextView) itemView.findViewById(R.id.title);
        publishTimeTv = (TextView) itemView.findViewById(R.id.publishTime);

    }
}



    @Override
    public void onResume() {
        super.onResume();
        if(news.size() == 0){
            fetchData();
        }

    }

    public void fetchData(){
        skv.setVisibility(View.VISIBLE);
        NewsServiceManager.getInstance()
                .getNews("", Constants.HEADLINE_TYPE, Constants.LOTTERY_ID,0)
                .subscribe(new Subscriber<Map<String, List<News>>>() {
                    @Override
                    public void onCompleted() {
                        skv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        skv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Map<String, List<News>> stringListMap) {
                        news.addAll(stringListMap.get(Constants.LOTTERY_ID));
                        newsAdapter.notifyDataSetChanged();
                        skv.setVisibility(View.GONE);
                    }
                });
    }


}
