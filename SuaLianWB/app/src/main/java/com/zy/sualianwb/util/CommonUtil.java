package com.zy.sualianwb.util;

import android.util.Log;

import com.zy.sualianwb.Constants;
import com.zy.sualianwb.module.ShowTime3;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zz on 15/12/24.
 */
public class CommonUtil {
    static String TAG = "CommonUtil";

//    /**
//     * 用来将另一个集合中的多出位置添加到原有集合中
//     *
//     * @param oldL
//     * @param newL
//     * @return
//     */
//    public static <T> List<T> getMore(List<T> oldL, List<T> newL) {
//        if(newL==null){
//            Log.w(TAG,"newL is null");
//            return null;
//        }
//        if (null == oldL) {
//            Log.w(TAG,"oldL is null");
//            return newL;
//        }
//        int oldSize = oldL.size();
//        int newSize = newL.size();
//
//        Log.i(TAG,"oldSize="+oldSize+"    newSize="+newSize);
//
//        int different = newSize - oldSize;
//
//        Log.i(TAG,"different="+different);
//
//        if (different <= 0) {
//            Log.i(TAG,"different=0 return ");
//            return null;
//        }
//
////        for (int i = oldSize; i < newSize; i++) {
////            oldL.add(newL.get(i));
////
////        }
//
//        newL.removeAll(oldL);
//
//        return newL;
//
//    }

    /**
     * 用来将另一个集合中的多出位置添加到原有集合中
     *
     * @param newL
     * @return
     */
    public static List<ShowTime3> getMore2(List<ShowTime3> newL) {
        if (newL == null) {
            Log.w(TAG, "newL is null");
            return null;
        }
        List<ShowTime3> more = new ArrayList<>();
//        Timestamp currTimestamp = new Timestamp(System.currentTimeMillis() + Constants.UP_CUT_TIME);
//        long currTimestampTime = currTimestamp.getTime();

        for (int i = 0; i < newL.size(); i++) {
            ShowTime3 showTime3 = newL.get(i);
            Timestamp picTimeStamp = showTime3.targetStamp();
            long picTimeStampTime = picTimeStamp.getTime();
            if (picTimeStampTime > Constants.lastTime) {
                more.add(showTime3);
                Constants.lastTime = picTimeStampTime;
            }
        }
        return more;

    }
}
