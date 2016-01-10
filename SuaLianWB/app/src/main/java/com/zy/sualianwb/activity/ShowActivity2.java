package com.zy.sualianwb.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.content.Intent;

import com.zy.sualianwb.Constants;
import com.zy.sualianwb.R;
import com.zy.sualianwb.base.BaseActivity;
import com.zy.sualianwb.module.DefaultUrl;
import com.zy.sualianwb.module.ShowTime3;
import com.zy.sualianwb.util.BitmapUtil;
import com.zy.sualianwb.util.BlindImageView;
import com.zy.sualianwb.util.DeviceUtil;
import com.zy.sualianwb.util.Insign;
import com.zy.sualianwb.util.L;
import com.zy.sualianwb.util.StorageUtil;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by zz on 15/12/30.
 */
public class ShowActivity2 extends BaseActivity {
    private BlindImageView showImageView;
    private BitmapUtil bitmapUtil;
    private Thread showThread;
    private boolean isRun = true;
    private String TAG = "ShowActivity2";
    private String currUrl;


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_show);
        DeviceUtil.turnLight(this);
        isRun = true;
        showImageView = (BlindImageView) findViewById(R.id.sho_image);
        bitmapUtil = new BitmapUtil(this);
        bitmapUtil.setOnImageBitmapGet(new BitmapUtil.OnImageBitmapGet() {
            @Override
            public void get(final Bitmap bm) {
                showImageView.post(new Runnable() {
                    @Override
                    public void run() {
                        onGet(bm);
                    }
                });
            }
        });
//        startShowThread();

        startShowThread2();
    }

    private void startShowThread2() {
        showThread = new Thread() {
            @Override
            public void run() {
                super.run();
                while (isRun && !Constants.shouldStopNow) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String targetUrl = null;
                    List<ShowTime3> playShowTimeList = Constants.playShowTimeList;
                    if (playShowTimeList != null) {
                        for (int i = 0; i < playShowTimeList.size(); i++) {
                            ShowTime3 showTime3 = playShowTimeList.get(i);
                            Timestamp targetStamp = showTime3.targetStamp();
                            long diff = getDiff(targetStamp);
                            if (diff <= 0) {
//                                targetUrl = showTime3.getSrc();
                                targetUrl = getSrc(showTime3);

                            } else {
                                break;
                            }
                        }
                        sendToPlayUrl(targetUrl);
                    }
                }
            }
        };
        showThread.start();
    }

    private String getSrc(ShowTime3 showTime3) {
        if (Insign.isInsign()) {
            //在里面，显示全量图
            return showTime3.getSrc();
        } else {
            //在外面，显示模板图
            return showTime3.getType();
        }
    }

    private void sendPlayDefault() {
        playHandler.obtainMessage(2).sendToTarget();


    }

    private void sendToPlay(ShowTime3 showTime3) {
        playHandler.obtainMessage(1, showTime3).sendToTarget();
    }

    private void sendToPlayUrl(String url) {
        playHandler.obtainMessage(3, url).sendToTarget();
    }

    private Handler playHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                ShowTime3 showTime3 = (ShowTime3) msg.obj;
                final String src = showTime3.getSrc();
                L.e("play-start", "准备播放" + src);
                show(src);
            }
            if (msg.what == 2) {
                DefaultUrl defaultUrl = StorageUtil.randomDefUrl(ShowActivity2.this);
                show(defaultUrl.getSrc());
            }

            if (msg.what == 3) {
                String targetUrl = (String) msg.obj;
                if (null == targetUrl) {
                    Log.w(TAG, "targetUrl is null");
                    return;
                }
                if (targetUrl.equals(currUrl)) {
                    Log.w(TAG, "currUrl 和 targetUrl相同");
                    return;
                }
                Log.i(TAG, "show currUrl=" + currUrl);
                currUrl = targetUrl;
                show(currUrl);
            }
        }
    };

    private void show(final String src) {
        showImageView.post(new Runnable() {
            @Override
            public void run() {
                Log.i("play-start", "播放" + src + " 当前时间" + new Timestamp(System.currentTimeMillis() + Constants.UP_CUT_TIME));
                bitmapUtil.getBitmap(showImageView.getImageView(), src);
            }
        });
    }

    private long getDiff(Timestamp timestamp) {
        Timestamp currTimeStamp = new Timestamp(System.currentTimeMillis() + Constants.UP_CUT_TIME);
        long currTimeStampTimeMillis = currTimeStamp.getTime();
        long targetTimeStameMillis = timestamp.getTime();
        long cut = (targetTimeStameMillis - currTimeStampTimeMillis);
        return cut;
    }


    private void onGet(Bitmap bm) {
        showImageView.setBitmap(Constants.currPos, bm);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRun = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    int pressBackCount = 1;


    int pressHomeCount = 1;
    int pressMenuCount = 1;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 监控/拦截/屏蔽返回键
            if (pressBackCount >= 3) {
                finish();
            } else {
                pressBackCount++;
            }

        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (pressMenuCount >= 3) {
                return super.onKeyDown(keyCode, event);
            } else {
                pressMenuCount++;
            }
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            if (pressHomeCount >= 3) {
                return super.onKeyDown(keyCode, event);
            } else {
                pressHomeCount++;
            }

        }
        return true;
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            pressMenuCount = 1;
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            pressBackCount = 1;
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            pressHomeCount = 1;
            return true;
        }

        return super.onKeyLongPress(keyCode, event);
    }


    int clickCount = 1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickCount++;
                if (clickCount > 3) {
                    Intent i = new Intent(ShowActivity2.this, MainActivity.class);
                    startActivity(i);
                    this.finish();
                }
                break;
        }
        return super.onTouchEvent(event);
    }


}
