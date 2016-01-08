package com.zy.sualianwb.util;

/**
 * Created by zz on 15/12/30.
 */
public class TimeUtil {


    static long cutMin;

    public static void startRecode() {
        cutMin = System.currentTimeMillis();
    }

    public static long stop() {
        long currentTimeMillis = System.currentTimeMillis();
        cutMin = (currentTimeMillis - cutMin);
        return cutMin;
    }

    static volatile  long  downloadMillis;

    public static void startDownLoadRecode() {
        downloadMillis = System.currentTimeMillis();
    }

    public static long stopDownLoadRecode() {
        long currentTimeMillis = System.currentTimeMillis();
        return (currentTimeMillis - downloadMillis);

    }


    static long allTimeMillis;

    public static void startAll() {
        allTimeMillis = System.currentTimeMillis();
    }

    public static long stopAll() {
        long currentTimeMillis = System.currentTimeMillis();
        return (currentTimeMillis - allTimeMillis);
    }

}
