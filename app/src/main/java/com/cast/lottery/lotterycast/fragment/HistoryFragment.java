package com.cast.lottery.lotterycast.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cast.lottery.lotterycast.R;
import com.cast.lottery.lotterycast.activity.HistoryDetailActivity;
import com.cast.lottery.lotterycast.models.Lottery;
import com.cast.lottery.lotterycast.utils.LotteryUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 2017/8/3.
 */

public class HistoryFragment extends BaseContentFragment {


    public static BaseContentFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public View getContainerView(View view) {

        return view.findViewById(R.id.container);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ListView category_list = (ListView) rootView.findViewById(R.id.category_list);
        setOnItemONClickListener(category_list);
        List<String> categories = syncCategories();
        category_list.setAdapter(new ArrayAdapter<>(getContext(),R.layout.history_category_item,categories));
        return rootView;
    }

    public void setOnItemONClickListener(ListView category_list){
        category_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryDetailActivity.start(getContext(), LotteryUtils.getId(getData().get(position)));
            }
        });
    }

    @NonNull
    private List<String> syncCategories() {
        List<Lottery.IEntity> data = getData();
        List<String> categories = new ArrayList<>();
        for (Lottery.IEntity entity:
        data) {
            categories.add(entity.getLotName());
        }
        return categories;
    }

    @Override
    public void onRefresh() {

    }
}
