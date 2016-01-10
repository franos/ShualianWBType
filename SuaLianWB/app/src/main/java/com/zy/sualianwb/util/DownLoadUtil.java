package com.zy.sualianwb.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zy.sualianwb.Constants;

import java.io.IOException;

/**
 * Created by zz on 15/12/23.
 */
public class DownLoadUtil {
    private String TAG = "DownLoadUtil";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (onDataGet == null) {
                        return;
                    }
                    onDataGet.onUrlsGet((String) msg.obj);
                    break;
                case 1:
                    if (onDataGet == null) {
                        return;
                    }
                    onDataGet.onShowTimeGet((String) msg.obj);
                    break;
                case -1:
                    if (onDataGet == null) {
                        return;
                    }
                    onDataGet.onUrlsGetFail((String) msg.obj);
                    break;
                case -2:
                    if (onDataGet == null) {
                        return;
                    }
                    onDataGet.onShowTimeGetFail((String) msg.obj);
                    break;
                case 3:
                    if (onDefDataGet == null) {
                        return;
                    }
                    onDefDataGet.onUrlsGet((String) msg.obj);
                    break;
                case -3:
                    if (onDefDataGet == null) {
                        return;
                    }
                    onDefDataGet.onUrlsGetFail((String) msg.obj);
                    break;
                case 4:
                    if (onTemplateGet == null) {
                        Log.w(TAG, "onTemplateGet is null");
                        return;
                    }
                    onTemplateGet.onTemplateGet((String) msg.obj);
                    break;
                case -4:
                    if (onTemplateGet == null) {
                        Log.w(TAG, "onTemplateGet is null");
                        return;
                    }
                    onTemplateGet.onFail((String) msg.obj);
                    break;
            }
        }
    };

    public void getDefultUrls() {
        new Thread() {
            @Override
            public void run() {
                try {
                    log("开始获取默认图片");
                    String json = HttpUtil.get(Constants.IMAGE_URL_HOST);
                    log("获取成功" + json);
                    mHandler.obtainMessage(3, json).sendToTarget();
                } catch (IOException e) {
                    log("获取失败" + e);
                    e.printStackTrace();
                    mHandler.obtainMessage(-3, e.getMessage()).sendToTarget();
                }
            }
        }.start();
    }


    public void getUrls() {
        new Thread() {
            @Override
            public void run() {
                try {
                    log("开始获取主要图片url");
                    String json = HttpUtil.get(Constants.URLS_HOST);
                    log("主要图片url获取成功");
                    mHandler.obtainMessage(0, json).sendToTarget();
                } catch (IOException e) {
                    log("主要图片url获取失败" + e);
                    e.printStackTrace();
                    mHandler.obtainMessage(-1, e.getMessage()).sendToTarget();
                }

            }
        }.start();
    }

    public void getShowTime() {
        new Thread() {
            @Override
            public void run() {
                try {
                    log("开始获取展示时间");
                    String json = HttpUtil.get(Constants.getShowTimeUrl());
                    log("展示时间获取成功");
                    mHandler.obtainMessage(1, json).sendToTarget();
                } catch (IOException e) {
                    log("展示时间获取失败" + e);
                    e.printStackTrace();
                    mHandler.obtainMessage(-2, e.getMessage()).sendToTarget();
                }

            }
        }.start();
    }

    public void getTemplateUrl() {
        new Thread() {
            @Override
            public void run() {
                try {
                    log("开始获取展示时间");
                    String json = HttpUtil.get(Constants.TEMPLATE_URL);
                    log("展示时间获取成功");
                    mHandler.obtainMessage(4, json).sendToTarget();
                } catch (IOException e) {
                    log("展示时间获取失败" + e);
                    e.printStackTrace();
                    mHandler.obtainMessage(-4, e.getMessage()).sendToTarget();
                }

            }
        }.start();
    }


    private OnDataGet onDataGet;

    public void setOnDataGet(OnDataGet onDataGet) {
        this.onDataGet = onDataGet;
    }

    public interface OnDataGet {
        void onUrlsGet(String json);

        void onShowTimeGet(String json);

        void onUrlsGetFail(String msg);

        void onShowTimeGetFail(String msg);

    }

    public void setOnTemplateGet(OnTemplateGet onTemplateGet) {
        this.onTemplateGet = onTemplateGet;
    }

    private OnTemplateGet onTemplateGet;

    public interface OnTemplateGet {
        void onTemplateGet(String obj);

        void onFail(String obj);
    }

    private OnDefDataGet onDefDataGet;

    public void setOnDefDataGet(OnDefDataGet onDefDataGet) {
        this.onDefDataGet = onDefDataGet;
    }


    public interface OnDefDataGet {
        void onUrlsGet(String json);

        void onUrlsGetFail(String msg);


    }

    void log(String text) {
        L.i(TAG, text);
    }
}
