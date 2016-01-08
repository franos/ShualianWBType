package com.zy.sualianwb.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.zy.sualianwb.Constants;

public class DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public static int[] getWindowWidthHeight(Context context) {
        int[] args = new int[2];
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrix = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrix);
        args[0] = outMetrix.widthPixels;
        args[1] = outMetrix.heightPixels;
        return args;
    }

    /**
     * 得到下标列 从0开始
     *
     * @param currPos 当前位置 ，从0开始
     * @return
     */
    public static int getCurrLie(int currPos) {
        int LIE = currPos % Constants.X_NUM;
        return LIE;
    }

    /**
     * 得到当前行 从0开始
     *
     * @param currPos 当前位置 ，从0开始
     * @return
     */
    public static int getCurrHang(int currPos) {
        int HANG = currPos / Constants.Y_NUM;//得到第几行
        return HANG;
    }

    /**
     * 根据行列算出当前位置，真实位置
     *
     * @param hang 从1开始
     * @param lie  从1开始
     * @return
     */
    public static int getCurrPos(int hang, int lie) {
        int currpos = 0;
        currpos = (hang - 1) * Constants.X_NUM + lie;

        return currpos;
    }

}  
