package com.cast.lottery.lotterycast.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cast.lottery.lotterycast.R;

import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by Konstantin on 22.12.2014.
 */
public abstract class BaseContentFragment extends Fragment implements ScreenShotable {
    public static final String CLOSE = "Close";
    public static final String LATEST = "latest";
    public static final String HISTORY = "history";
    public static final String DETAIL = "detail";


    private View containerView;
    private Bitmap bitmap;
    public abstract View getContainerView(View view);

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.containerView = getContainerView(view);
    }


    @Override
    public void takeScreenShot() {
        Bitmap bitmap = Bitmap.createBitmap(containerView.getWidth(),
                containerView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        containerView.draw(canvas);
        BaseContentFragment.this.bitmap = bitmap;
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

