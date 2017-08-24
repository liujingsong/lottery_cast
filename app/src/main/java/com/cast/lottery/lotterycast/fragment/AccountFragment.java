package com.cast.lottery.lotterycast.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cast.lottery.lotterycast.R;
import com.cast.lottery.lotterycast.activity.LoginDeviceManager;

/**
 * Created by Kevin on 2017/8/23.
 */

public class AccountFragment extends BaseContentFragment {
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
        View view = inflater.inflate(R.layout.fragment_account, null);
        setLayoutParams(view);
        setupPhone(view);
        setupEmail(view);
        setupQQ(view);
        setupWeiXin(view);

        setupDeviceManager(view);
        return view;
    }

    private void setupPhone(View view){
        View phone_item = view.findViewById(R.id.phone);
        TextView p_name = (TextView) phone_item.findViewById(R.id.item_name);
        p_name.setText("手机号");
    }

    private void setupEmail(View view){
        View email_item = view.findViewById(R.id.email);
        TextView e_name = (TextView) email_item.findViewById(R.id.item_name);
        e_name.setText("邮箱");
    }

    private void setupQQ(View view){
        View qq_item = view.findViewById(R.id.qq);
        TextView qq_name = (TextView) qq_item.findViewById(R.id.item_name);
        qq_name.setText("QQ");
    }

    private void setupWeiXin(View view){
        View wx_item = view.findViewById(R.id.weixin);
        TextView wx_name = (TextView) wx_item.findViewById(R.id.item_name);
        wx_name.setText("微信");
    }

    private void setupDeviceManager(View view) {
        View device_manager_item = view.findViewById(R.id.login_device_manager);
        TextView dm_name = (TextView) device_manager_item.findViewById(R.id.item_name);
        dm_name.setText("登录设备管理");
        View divider = device_manager_item.findViewById(R.id.divider);
        divider.setVisibility(View.GONE);
        device_manager_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginDeviceManager.class);
                startActivity(intent);
            }
        });
    }

    private void setLayoutParams(View view) {
        DisplayMetrics outMetrics;
        outMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,outMetrics.heightPixels));
    }
}
