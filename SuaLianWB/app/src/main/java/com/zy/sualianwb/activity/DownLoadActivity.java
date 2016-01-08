package com.zy.sualianwb.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zy.sualianwb.Constants;
import com.zy.sualianwb.R;
import com.zy.sualianwb.base.BaseActivity;
import com.zy.sualianwb.module.DefaultUrl;
import com.zy.sualianwb.module.ImagesUrl;
import com.zy.sualianwb.util.DownLoadUtil;
import com.zy.sualianwb.util.DownloadImgUtils;
import com.zy.sualianwb.util.JsonUtil;
import com.zy.sualianwb.util.L;
import com.zy.sualianwb.util.StorageUtil;
import com.zy.sualianwb.util.Translate;
import com.zy.sualianwb.util.ViewUtil;

import java.util.List;

public class DownLoadActivity extends BaseActivity {
    private static final String TAG = "DownLoadActivity";
    private DownLoadUtil util;
    private TextView tips;
    private TextView defTips;

    private int downloadIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_down_load);
        util = new DownLoadUtil();
        tips = (TextView) findViewById(R.id.download_tips_tv);
        defTips = (TextView) findViewById(R.id.default_tip);


        util.setOnDataGet(new DownLoadUtil.OnDataGet() {
            @Override
            public void onUrlsGet(String json) {
                tips("图片链接获取成功，开始下载");
//                util.getShowTime();
                ImagesUrl imagesUrl = Translate.translateUrl(json);
                StorageUtil.deleteSave(DownLoadActivity.this, imagesUrl);
//                ImagesUrl imagesUrl1 = StorageUtil.getImagesUrl(DownLoadActivity.this);
//                Log.e(TAG,"监查图片url的缓存状况"+imagesUrl1);
                downLoadAllImage(imagesUrl);
                Log.i(TAG, "imagesUrl:" + json);
            }

            @Override
            public void onShowTimeGet(String json) {
//                Toast.makeText(DownLoadActivity.this, "图片显示时间获取成功", Toast.LENGTH_SHORT).show();
//                ShowTime showTime = Translate.translateShowTime(json);
//                StorageUtil.deleteSave(DownLoadActivity.this, showTime);
                Log.i(TAG, "showTime:" + json);
            }

            @Override
            public void onUrlsGetFail(String msg) {
                Toast.makeText(DownLoadActivity.this, "图片链接获取失败", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onUrlsGetFail:" + msg);
            }

            @Override
            public void onShowTimeGetFail(String msg) {
                Toast.makeText(DownLoadActivity.this, "图片时间获取失败", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onShowTimeGetFail:" + msg);
            }
        });
    }

    public Handler mDownloadSusHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
//                    List<String> url = (List<String>) msg.obj;
//                    tips.setText("" + (downloadIndex + 1) + "/" + url.size());
                    break;
                case 0:
                    Toast.makeText(DownLoadActivity.this, "第" + (1 + DownLoadActivity.this.downloadIndex) + "个图片下载失败", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    tips.setText("主要图片下载完毕!");

                    break;
            }

        }
    };

    private void downLoadAllImage(ImagesUrl imagesUrl) {
        final List<String> url = imagesUrl.getUrl();
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < url.size(); i++) {
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tips.setText("正在下载第" + (finalI + 1) + "张图片/共 " + url.size() + " 张");
                        }
                    });


                    DownLoadActivity.this.downloadIndex = i;
                    String urlImage = url.get(downloadIndex);
                    boolean downloadState = DownloadImgUtils.downloadImgByUrl(urlImage, StorageUtil.getDiskCacheDirByPath(DownLoadActivity.this, urlImage), DownLoadActivity.this);
                    if (downloadState) {
                        mDownloadSusHandler.obtainMessage(1, url).sendToTarget();
                    } else {
                        mDownloadSusHandler.obtainMessage(0).sendToTarget();
                    }
                }
                mDownloadSusHandler.obtainMessage(2).sendToTarget();
            }
        }.start();


    }

    void tipsDef(String text) {
        defTips.setText(text);
    }

    private void tips(String text) {
        tips.setText(text);
    }

//    public void getUrls(View view) {
//        util.getUrls();
//    }
//
//    public void getShowTime(View view) {
//        util.getShowTime();
//    }

    public void download(View view) {
        tips.setText("开始下载图片");
        util.getUrls();
        defTips.setText("开始下载默认图片");
        getDefaultPic();
    }


    private void getDefaultPic() {
        final DownLoadUtil util = new DownLoadUtil();
        util.setOnDefDataGet(new DownLoadUtil.OnDefDataGet() {
            @Override
            public void onUrlsGet(String json) {
                tipsDef("默认图片链接获取成功");
                L.i(TAG, json);
                List<DefaultUrl> defaultUrls = new JsonUtil().parseJsonArray(json, DefaultUrl.class);
                tipsDef("类型转换成功");
                L.i(TAG, " 转 " + defaultUrls);
                StorageUtil.deleteSave(DownLoadActivity.this, defaultUrls);
                L.i(TAG, " 保存");
                tipsDef("开始下载");
                if (defaultUrls == null) {
                    util.getDefultUrls();
                    return;
                }
                downloadDef(defaultUrls);
                Constants.isReady = true;
            }

            @Override
            public void onUrlsGetFail(String msg) {
                util.getDefultUrls();
                defTips.setText("获取默认图失败,尝试重新获取中..");
                L.i(TAG, "获取默认图失败,尝试重新获取中..");
            }
        });
        util.getDefultUrls();
    }

    private void downloadDef(final List<DefaultUrl> defaultUrls) {
        new Thread() {
            @Override
            public void run() {
                if (defaultUrls == null) {
                    return;
                }
                for (int i = 0; i < defaultUrls.size(); i++) {
                    final int finalI2 = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            defTips.setText("正在下载第 " + (finalI2 + 1) + " 张默认图片/共 " + defaultUrls.size() + " 张");
                        }
                    });
                    String urlImage = defaultUrls.get(i).getSrc();
                    final boolean downloadState = DownloadImgUtils.downloadImgByUrl(urlImage, StorageUtil.getDiskCacheDirByPath(DownLoadActivity.this, urlImage), DownLoadActivity.this);
                    L.i(TAG, "downloadState=" + downloadState);
                    final int currPic = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (downloadState) {
                            } else {
                                ViewUtil.showSingleToast(DownLoadActivity.this, "第" + (currPic + 1) + "张默认图片下载失败");
                                L.e(TAG, "第" + (currPic + 1) + "张默认图片下载失败");
                            }
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        defTips.setText("默认图片下载完毕!");
                    }
                });
            }
        }.start();
    }

}
