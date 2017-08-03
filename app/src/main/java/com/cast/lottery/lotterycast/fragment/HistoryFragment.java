package com.cast.lottery.lotterycast.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cast.lottery.lotterycast.R;

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
        return rootView;
    }
}
