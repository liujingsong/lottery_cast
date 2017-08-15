package com.cast.lottery.lotterycast.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cast.lottery.lotterycast.R;

/**
 * Created by kevin on 8/15/17.
 */

public class AboutFragment extends BaseContentFragment {

    public static BaseContentFragment newInstance() {
        return new AboutFragment();
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
        View view = View.inflate(getContext(), R.layout.fragment_about, null);
        return view;
    }
}
