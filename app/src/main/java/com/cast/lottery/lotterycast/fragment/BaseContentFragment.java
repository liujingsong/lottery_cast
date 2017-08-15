package com.cast.lottery.lotterycast.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cast.lottery.lotterycast.MainActivity;
import com.cast.lottery.lotterycast.R;
import com.cast.lottery.lotterycast.models.Lottery;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import yalantis.com.sidemenu.interfaces.ScreenShotable;

/**
 * Created by Konstantin on 22.12.2014.
 */
public abstract class BaseContentFragment extends Fragment implements ScreenShotable {
    public static final String CLOSE = "Close";
    public static final String LATEST = "latest";
    public static final String HISTORY = "history";
    public static final String ABOUT = "about";


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
        Observable.just(new int[]{containerView.getWidth(), containerView.getHeight()}).map(new Func1<int[], Bitmap>() {
            @Override
            public Bitmap call(int[] ints) {
                return Bitmap.createBitmap(ints[0],
                        ints[1], Bitmap.Config.ARGB_8888);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Bitmap>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Bitmap bitmap) {
                Canvas canvas = new Canvas(bitmap);
                containerView.draw(canvas);
                BaseContentFragment.this.bitmap = bitmap;
            }
        });
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected List<Lottery.IEntity> getData() {
        return ((MainActivity)getActivity()).getData();
    }
}

