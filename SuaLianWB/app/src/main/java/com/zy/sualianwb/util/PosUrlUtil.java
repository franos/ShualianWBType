package com.zy.sualianwb.util;

/**
 * Created by zz on 16/1/7.
 */
public class PosUrlUtil {


    private static final String TAG = "PosUrlUtil";

    public static String urlWidthPos(String name) {
//        int currIndex = Constants.currPos;
//
//        String posString = getPosString(currIndex);
//        Log.i(TAG, "currPos" + posString);
//        return name + posString;
        return name;
    }

    private static String getPosString(int i) {
        String posString = "_";
        if (i <= 9) {
            posString += "0";
        }
        posString += String.valueOf(i);
        return posString;
    }
}
