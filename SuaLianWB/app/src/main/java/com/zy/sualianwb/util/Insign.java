package com.zy.sualianwb.util;

import android.util.Log;

import com.zy.sualianwb.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zz on 16/1/8.
 */
public class Insign {


    private static final String TAG = "Insign";

    public static List<Integer> getInsign() {
        int[] ins = {
                89, 90, 91, 106, 107, 108, 123, 124, 125, 140, 141,
                142, 157, 158, 159, 189, 190, 191, 192, 193, 194, 206,
                207, 208, 209, 210, 211, 223, 224, 225, 226, 227, 228,
                93, 94, 95, 110, 111, 112, 127, 128, 129, 144, 145, 146,
                161, 162, 163, 177, 178, 179, 180, 181, 195, 196, 197
        };
        List<Integer> inLis = toLis(ins);
        return inLis;
    }

    private static List<Integer> toLis(int[] ins) {
        List<Integer> inLis = new ArrayList<>(ins.length);
        for (int i = 0; i < ins.length; i++) {
            int in = ins[i];
            inLis.add(in);
        }
        Log.i(TAG, "" + inLis);
        return inLis;
    }

    /**
     * 是否在里面
     *
     * @return
     */
    public static boolean isInsign() {
        List<Integer> insign = Insign.getInsign();
        int currPos = Constants.currPos;
        if (insign.contains(currPos) && Constants.isInsign) {
            //在里面下载全量图
            return true;
        } else {
            //在外面下载模板图
            return false;
        }
    }
}
