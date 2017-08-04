package yalantis.com.sidemenu.interfaces;

import android.graphics.Bitmap;

/**
 * Created by Konstantin on 12.01.2015.
 */
public interface ScreenShotable {

     void takeScreenShot();

     Bitmap getBitmap();

     void onRefresh();
}
