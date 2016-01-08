package com.zy.sualianwb.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBase;
import com.zy.sualianwb.Constants;
import com.zy.sualianwb.module.DefaultUrl;
import com.zy.sualianwb.module.ImagesUrl;
import com.zy.sualianwb.module.ShowTime;
import com.zy.sualianwb.module.ShowTime2;
import com.zy.sualianwb.module.ShowTime3;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by zz on 15/12/24.
 */
public class StorageUtil {
    static BlockingDeque<Integer> queue = new LinkedBlockingDeque<>(10);
    static String TAG = "StorageUtil";

    public static String getCachePath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
//            cachePath = context.getExternalCacheDir().getPath();
            cachePath = Constants.CACHE_PATH_EXTRA;
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        File file = new File(cachePath);
        if (!file.exists()) {
            file.mkdir();
        }
        return cachePath;
    }

    /**
     * 获得缓存图片的地址
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = StorageUtil.getCachePath(context);
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获得缓存图片的地址
     *
     * @param context
     * @return
     */
    public static File getDiskCacheDirByPath(Context context, String url) {
        return getDiskCacheDir(context, md5(url));
    }


    /**
     * 利用签名辅助类，将字符串字节数组
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        byte[] digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            digest = md.digest(str.getBytes());
            return bytes2hex02(digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 方式二
     *
     * @param bytes
     * @return
     */
    public static String bytes2hex02(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (byte b : bytes) {
            // 将每个字节与0xFF进行与运算，然后转化为10进制，然后借助于Integer再转化为16进制
            tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() == 1)// 每个字节8为，转为16进制标志，2个16进制位
            {
                tmp = "0" + tmp;
            }
            sb.append(tmp);
        }
        return sb.toString();

    }


    public synchronized static void deleteSave(Context context, ShowTime showTime) {

        DataBase db = LiteOrm.newInstance(context, Constants.DB_NAME);
        db.deleteAll(ShowTime.class);
        db.save(showTime);
        db.close();
    }

    public synchronized static void deleteSave(Context context, ImagesUrl imagesUrl) {
        L.e(TAG, "imageUrl库刷新");
        DataBase db = LiteOrm.newInstance(context, Constants.DB_NAME);
        db.deleteAll(ImagesUrl.class);
        db.save(imagesUrl);
        db.close();
        L.i(TAG, "imageUrl库刷新完毕");
    }


    public static synchronized void saveShowTime3(Context context, List<ShowTime3> showTime3s) {

//        L.e(TAG, "showTime3库刷新前数据");
//        DataBase db = LiteOrm.newInstance(context, Constants.DB_NAME);
//        L.i(TAG, "save show time 3" + showTime3s);

        //判断新来的showTime3s是否需要增加
        List<ShowTime3> showTime3ToSave = CommonUtil.getMore2(showTime3s);
        if (null == showTime3ToSave) {
//            db.close();
            Log.w(TAG, "没什么可添加的");
            return;
        }
        //将要显示的内容添加到显示队列
        Log.i(TAG, "add num  before" + Constants.playShowTimeList.size());
        Constants.playShowTimeList.addAll(showTime3ToSave);
        Log.i(TAG, "add num  after" + Constants.playShowTimeList.size());
//        db.save(showTime3ToSave);
//
//        L.i(TAG, "查看showTime3");
//        ArrayList<ShowTime3> showTime3s1 = db.queryAll(ShowTime3.class);
//        L.i(TAG, "" + showTime3s1);
//        db.close();
//        L.e(TAG, "showTime3库刷新完毕");
    }

    public synchronized static ImagesUrl getImagesUrl(Context context) {
        DataBase db = LiteOrm.newInstance(context, Constants.DB_NAME);
        ArrayList<ImagesUrl> imagesUrls = db.queryAll(ImagesUrl.class);
        if (imagesUrls.isEmpty()) {
            Log.i(TAG, "imagesUrls is empty return null");
            db.close();
            return null;
        }
        ImagesUrl imagesUrl = imagesUrls.get(0);
        db.close();
        return imagesUrl;
    }


    public synchronized static List<ShowTime3> getShowTime3(Context context) {
        DataBase db = LiteOrm.newInstance(context, Constants.DB_NAME);
        ArrayList<ShowTime3> showTimes=new ArrayList<>();
//        showTimes = db.queryAll(ShowTime3.class);
//        if (showTimes.isEmpty()) {
//            Log.i(TAG, "showTimes3 is empty return null");
//            db.close();
//            return null;
//        }
//        db.close();
//        ShowTimeComparable comparable = new ShowTimeComparable();
//        Collections.sort(showTimes, comparable);
//

        return showTimes;
    }


    public synchronized static void deleteSave(Context context, List<DefaultUrl> defaultUrls) {
        L.e(TAG, "刷新默认图片");
        DataBase db = LiteOrm.newInstance(context, Constants.DB_NAME);
        db.deleteAll(DefaultUrl.class);
        db.save(defaultUrls);
        db.close();
        L.e(TAG, "刷新默认图片完毕!");
    }

    public synchronized static DefaultUrl randomDefUrl(Context context) {
        L.e(TAG, "获取默认图片");
        DataBase db = null;
        try {

            db = LiteOrm.newInstance(context, Constants.DB_NAME);
            ArrayList<DefaultUrl> defaultUrls = db.queryAll(DefaultUrl.class);
            int seed = getSeed(defaultUrls.size());
            DefaultUrl defaultUrl = defaultUrls.get(seed);
            L.e(TAG, "获取默认图片 完毕：" + defaultUrl);

            return defaultUrl;
        } catch (Exception e) {
            e.printStackTrace();
            DefaultUrl defaultUrl = new DefaultUrl();
            defaultUrl.setSrc("http://a.36krcnd.com/nil_class/7f31828e-7486-4f6a-bacc-b5fd3887dce7/yestone_HD_1111424136.jpg!heading");
            return defaultUrl;
        } finally {
            if (null != db)
                db.close();
        }

    }

    private static int randomIndex = 0;

    private static int getSeed(int size) {
        Random ran = new Random();
        int i = ran.nextInt((size == 0 ? 3 : size));
        if (i == randomIndex) {
            return getSeed(size);
        }
        randomIndex = i;
        return randomIndex;
    }


    public static void clearAllData(Context context) {
        deleteExtraDirectory();
        clearDB(context);
    }

    private static void clearDB(Context context) {
        DataBase db = LiteOrm.newInstance(context, Constants.DB_NAME);
        db.deleteAll(ImagesUrl.class);
        db.deleteAll(ShowTime.class);
        db.deleteAll(ShowTime2.class);
        db.deleteAll(ShowTime3.class);
        db.close();
    }

    private static void deleteExtraDirectory() {
        File mFile = new File(Constants.CACHE_PATH_EXTRA);
        if (mFile.exists() && mFile.isDirectory()) {
            File[] files = mFile.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
    }

    public static void clearShowTime(Context context) {
        DataBase db = LiteOrm.newInstance(context, Constants.DB_NAME);
        db.deleteAll(ShowTime.class);
        db.deleteAll(ShowTime2.class);
        db.deleteAll(ShowTime3.class);
        db.close();

    }

    public static void clearUrl(Context context) {
        DataBase db = LiteOrm.newInstance(context, Constants.DB_NAME);
        db.deleteAll(ImagesUrl.class);
        db.close();
    }

    public static void clearUrlAndShowTime(Context context) {
        clearShowTime(context);
        clearUrl(context);
    }
}

