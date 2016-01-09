package com.zy.sualianwb;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.zy.sualianwb.module.ShowTime3;
import com.zy.sualianwb.util.anim.FadeInAnimation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zz on 15/12/23.
 */
public class Constants {


    public static List<ShowTime3> playShowTimeList = new ArrayList<>();

    public static final long STONE_DELAY_TIME = 10000;

    public static final int PHONE_NUM = 289;//个
    public static final int SHOW_ANIM_TIME = 100;//ms
    public static int X_NUM = 17;//总行数
    public static int Y_NUM = 17;//总列数

    public int CURR_X = Constants.currPos % X_NUM;//当前列数
    public int CURR_Y = Constants.currPos / Y_NUM;//当前行数

    public static final int BLIND_CUT_TIME = 1;//百叶窗的切割数
    public static int currPos = 1;//第几个
    public static long UP_CUT_TIME = 0;//时间偏差，加上去
    public static long lastTime = 0;
    public static boolean isReady = false;
    public static boolean isInsign = false;
    public static boolean isAllDownloadOk = true;
    public static boolean isStartDownload = false;
    public static boolean shouldStopNow = false;

//-----------------------------------url-----------------------------------

    public static String HOST_REMOTE = "http://www.modiarts.com/";
    //    public static String HOST = "http://192.168.1.254:55555/";
    public static String HOST_IMAGEDOWNLOAD = "http://101.226.152.105/";
//    public static String HOST = "http://192.168.1.102:8080/";

    //显示图片的时间请求地址
    public static final String SHOWTIME_HOST = HOST_REMOTE + "a/vivo201512/showuser.json.txt";
//    public static final String SHOWTIME_HOST = HOST + "ShualianWebDemo/GETShowTimeServlet";

    //时间矫正
    public static final String CHECK_TIME = "http://www.modiarts.com/a/vivo201512/bjtime.php";
//    public static final String CHECK_TIME="http://192.168.1.102:8080/ShualianWebDemo/TimeServlet";


    //图片url请求地址
    public static final String URLS_HOST = HOST_REMOTE + "a/vivo201512/canvastojpg/data.json";
    //    public static final String URLS_HOST = HOST + "ShualianWebDemo/GETImageServlet";
    //    //请求默认图片(当主图没了就播这几个)
    public static final String DEFAULT_PIC_URL = HOST_IMAGEDOWNLOAD + "a/vivo201512/getpic.php";
    //    public static final String DEFAULT_PIC_URL = HOST + "ShualianWebDemo/GetDefaultImageServlet";
    //图片的前段路径，即除去图片名部分
//    public static final String IMAGE_URL_HOST = HOST_REMOTE + "a/vivo201512/canvastojpg/op/";
    public static final String IMAGE_URL_HOST = HOST_IMAGEDOWNLOAD;
//    public static final String IMAGE_URL_HOST = HOST + "ShualianWebDemo/image/";

    //模板图片的url请求地址
    public static final String TEMPLATE_URL = HOST_REMOTE + "a/vivo201512/canvastojpg/backgrounds.json";

    //showTime给过来的模板图片前段路径,拼上去
    public static final String TEMPLATE_URL_HOST = HOST_IMAGEDOWNLOAD;

//
//    //测试demo
//    public static final String TEST_IMG = HOST + "ShualianWebDemo/pic.jpg";
    //-----------------------------------url-----------------------------------


    public static final String getShowTimeUrl() {
        int ranI = new Random().nextInt(10000000);
        String url = SHOWTIME_HOST + "?" + ranI;
        return url;
    }


    public static final String CACHE_PATH_EXTRA = Environment.getExternalStorageDirectory() + "/EasyShare/image";
    public static final String DATE_FOMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String CACHE_PATH = Environment.getDataDirectory() + "/image_cache";
    public static int DEFAULT_PIC_RES = R.mipmap.ic_launcher;//当图片加载失败时的默认图片

    public static final String DB_NAME = "shualian_images5.db";
    public static final String SHARE_PREF = "shualian_images_pref";
    public static final String SHARE_PREF_LAST_FILE = "shualian_images_last_file_pref";
    public static int[] LIS = new int[X_NUM * Y_NUM];


    public static final int REQUEST_TIME = 5000;//请求服务器的showtie间隔4秒
    public static final long CUT_DELAY_TIME = 1200;//可能存在的1秒钟的延迟时间
    public static final int PIC_DIZENG_DELAY_TIME = 300;//默认的递增图片显示时间
    public static final long DEFAULT_PIC_DELAY = 5000;//默认图片的延时播放时间
    public static long DELAY_TIME_OVERLOAD_FLAG = -1000;//超时标识,代表这张图片已经过时了
    public static long ALLOW_PLAY = -600;//最大允许超过时间
    public static long ANIM_DURATION_OUT = 200;//动画退出的时间
    public static long ANIM_DURATION_IN = 200;//动画进入的时间
    public static final int RANDOM_SEED = 25;
    public static final int OFFSET_TIME_SEED = 50;//randomSeed*offsettimeseed就是每一个方块动画延迟播放时间


    public static int[] initList() {
        int temp = 1;
        for (int i = 0; i < X_NUM * Y_NUM; i++) {
            LIS[i] = temp++;
        }
        return LIS;
    }


    private static FadeInAnimation animationIn;

    public synchronized static void startAnim(final BigImageView bigImageView, final Bitmap cropBitmap) {

        AlphaAnimation inAnimation = getInAnimation();
        inAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bigImageView.setImageBitmap(cropBitmap);
                bigImageView.clearAnimation();
                bigImageView.startAnimation(getOutAnimation());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        bigImageView.startAnimation(inAnimation);


    }


    public static AlphaAnimation getInAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(ANIM_DURATION_IN);
        alphaAnimation.setFillAfter(true);
        return alphaAnimation;
    }

    public static AlphaAnimation getOutAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(ANIM_DURATION_OUT);
        alphaAnimation.setFillAfter(true);
        return alphaAnimation;
    }

    public static int getCurrY() {
        return Constants.currPos / Constants.Y_NUM;
    }

    public static int getCurrX() {
        return Constants.currPos % Constants.X_NUM;
    }


}
