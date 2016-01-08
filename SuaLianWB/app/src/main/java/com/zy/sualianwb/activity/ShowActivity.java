package com.zy.sualianwb.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zy.sualianwb.Constants;
import com.zy.sualianwb.R;
import com.zy.sualianwb.base.BaseActivity;
import com.zy.sualianwb.module.DefaultUrl;
import com.zy.sualianwb.module.ShowTime3;
import com.zy.sualianwb.util.BitmapUtil;
import com.zy.sualianwb.util.BlindImageView;
import com.zy.sualianwb.util.L;
import com.zy.sualianwb.util.ShareUitl;
import com.zy.sualianwb.util.StorageUtil;

import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class ShowActivity extends BaseActivity {
    private static int currImageId = 0;
    private List<String> urlLis;
    private List<ShowTime3> showTime3;
    private Thread startThread;
    private boolean isDefault = false;
    public BlindImageView imageView;
    public BitmapUtil bitmapUtil;
    private String TAG = "ShowActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_show);
        restoreState();
        try {
            imageView = (BlindImageView) findViewById(R.id.sho_image);

            bitmapUtil = new BitmapUtil(this);
            bitmapUtil.setOnImageBitmapGet(new BitmapUtil.OnImageBitmapGet() {
                @Override
                public void get(final Bitmap bm) {
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            onGet(bm);
                        }
                    });
                }
            });
            L.i(TAG, "播放第一张 id:" + currImageId);
            showNext(0);
        } catch (Exception e) {
            e.printStackTrace();
            L.e(TAG, "出错啦");
        }
    }

    private void onGet(Bitmap bm) {
        L.i("ShowActivity", "show id:" + currImageId);
        imageView.setBitmap(Constants.currPos, bm);
        if (!isDefault) {
            ++currImageId;
        }
        showNext(currImageId);
        isDefault = false;
    }


    private void showDefault() {
        isDefault = true;
        //设置延迟
//        int currY = Constants.currPos % Constants.Y_NUM;//当前列
//        long l = (Constants.DEFAULT_PIC_DELAY + Constants.PIC_DIZENG_DELAY_TIME * (currY == 0 ? 1 : (currY + 1)));
        long l = (Constants.DEFAULT_PIC_DELAY);

        L.i(TAG, "显示默认图片" + l);
        DefaultUrl defaultUrl = StorageUtil.randomDefUrl(this);
        startShowWithDelaying((l == 0 ? 10 : l), defaultUrl.getSrc());
    }

    private int showNext(int i) {
        showTime3 = StorageUtil.getShowTime3(this);

        if (null == showTime3) {
            L.e(TAG, "图片链接未获得, 数据库未存放链接，显示默认图片");
            showDefault();
            return 0;
        }
        L.i(TAG, "图片showtime" + showTime3);
        L.e(TAG, "当前图片指针" + currImageId);
        //先做边界判断
        if (currImageId >= showTime3.size()) {
            L.e(TAG, "当前图片指针" + currImageId + "大于图片个数" + showTime3.size() + ",开始播放默认图片");

            if (!isDefault) {
                currImageId--;
            }
            //展示一张默认图片
            showDefault();
            return 1;
        }
        String url = showTime3.get(i).getSrc();
        L.e(TAG, "开始播放下一张" + currImageId + "   url" + url);
//        long delayTime = getDelayTime(i);
//        L.e(TAG, "延时时间" + delayTime);
//        if (delayTime == Constants.DELAY_TIME_OVERLOAD_FLAG) {
//            L.e(TAG, "该图片已超时");
//            //如果这张图片已经超时了，那代表下标应该加1
//            currImageId++;
//            //显示下一张
//            L.e(TAG, "播放下一张");
//            showNext(currImageId);
//            return 1;
//        }

        currImageId++;
//        L.e(TAG, "开始播放该图片：url" + url + "  delay:" + delayTime);
//        startShowWithDelaying(delayTime, url);
        Timestamp date = showTime3.get(i).targetStamp();
        L.e(TAG, "开始播放该图片：url" + url + "  targetDate :" + date);
        startShowWithDelaying3(date, url);
        return 1;
    }


    private void startShowWithDelaying(long delayTime, final String url) {
        try {
            L.i("ShowActivity", "toWait" + currImageId);
            playHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    L.i("ShowActivity", "Wait end" + currImageId + "  url:" + url);
                    bitmapUtil.getBitmap(imageView.getImageView(), url);
                }
            }, delayTime);
        } catch (Exception e) {
            e.printStackTrace();
            L.e(TAG, "method startShowWithDelaying has some thing wrong");
        }
    }

    private void startShowWithDelaying2(final Date targetDate, final String url) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (shouldDelay(targetDate)) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                DataHolder holder = new DataHolder();
                holder.url = url;
                L.e(TAG, "url  start send=" + url);
                playHandler2.obtainMessage(1, holder).sendToTarget();
            }
        }.start();
    }

    private void startShowWithDelaying3(final Timestamp targetTime, final String url) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                while (shouldDelayTimeStamp(targetTime)) {
                    try {
                        Thread.sleep(50);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                DataHolder holder = new DataHolder();
                holder.url = url;
                L.e(TAG, "url  start send=" + url);
                playHandler2.obtainMessage(1, holder).sendToTarget();
            }
        }.start();
    }


    private boolean shouldDelay(Date targetDate) {
        long currentTimeMillis = DateTime.now().getMillis() + Constants.UP_CUT_TIME;

        long targetDateTime = targetDate.getTime();
        long cut = Math.abs(targetDateTime - currentTimeMillis);
        L.e(TAG, "targetDateTime  " + targetDateTime + " currentTimeMillis " + currentTimeMillis + " cut" + cut);
        if (cut < 50) {
            return false;
        }
        return true;
    }

    private boolean shouldDelayTimeStamp(Timestamp timestamp) {
        Timestamp currTimeStamp = new Timestamp(System.currentTimeMillis() + Constants.UP_CUT_TIME);
        long currTimeStampTimeMillis = currTimeStamp.getTime();
        long targetTimeStameMillis = timestamp.getTime();
        long cut = Math.abs(targetTimeStameMillis - currTimeStampTimeMillis);
        L.e("count-time",""+cut);
        if (cut <= 75) {
            return true;
        }
        return false;
    }


    private static Handler playHandler = new Handler();
    private Handler playHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                final DataHolder holder = (DataHolder) msg.obj;
                final String url = holder.url;
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        ImageView blindImageView = imageView.getImageView();
                        L.e(TAG, "geturl" + url + "  image" + blindImageView);
                        bitmapUtil.getBitmap(blindImageView, url);
                    }
                });

            }
        }
    };

    private long getDelayTime(int i) {
        long delayMillis = showTime3.get(i).getDelayMillis();
        L.i("showActivity", "delayMillis: " + delayMillis);
//        long delayMillis=2000;
        return delayMillis;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        ShareUitl.writeString("ShowActivity_Perf", "picId", "" + currImageId, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreState();
    }

    private void restoreState() {
        String idIndex = ShareUitl.getString("ShowActivity_Perf", "picId", this);
        if (idIndex != null) {
            currImageId = Integer.parseInt(idIndex);
        } else {
            currImageId = 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        restoreState();
    }

    class DataHolder {
        ImageView blindImageView;

        String url;

    }

}
