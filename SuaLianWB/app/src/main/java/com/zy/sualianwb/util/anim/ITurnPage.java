package com.zy.sualianwb.util.anim;

import android.graphics.Bitmap;
import android.view.SurfaceHolder;

/**
 * Created by apple on 15/12/23.
 */
public interface ITurnPage {

    public abstract void onCreate();

    public abstract void onTurnPageDraw(SurfaceHolder holder,Bitmap[] bitmap,int maxWidth,int maxHeight);

    public abstract void onDestory();
}
