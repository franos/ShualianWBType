package com.zy.sualianwb;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.zy.sualianwb.module.ImagesUrl;
import com.zy.sualianwb.module.ShowTime3;
import com.zy.sualianwb.util.DeviceUtil;
import com.zy.sualianwb.util.DownLoadUtil;
import com.zy.sualianwb.util.DownloadImgUtils;
import com.zy.sualianwb.util.L;
import com.zy.sualianwb.util.StorageUtil;
import com.zy.sualianwb.util.Translate;
import com.zy.sualianwb.util.ViewUtil;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DownLoadService extends Service {
    public static boolean isStart = false;
    static Timer timer;
    static DownLoadUtil util;
    String TAG = "DownLoadService";
    private int downloadIndex;
    private boolean canStart = false;
    //    private BlockingDeque<Boolean> blockingDeque = new LinkedBlockingDeque<>(10);
    private int timeDiffcheckoutCount = 1;
    private long currCheckoutTime = 0;
    private Thread checkoutTimeThread;

    public DownLoadService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.i(TAG, "downloadService create");
        isStart = true;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.i(TAG, "downloadService init");


        timer = new Timer();
        util = new DownLoadUtil();
//        checkUrlAvaiable();

        util.setOnDataGet(new DownLoadUtil.OnDataGet() {
            @Override
            public void onUrlsGet(String json) {
                try {
                    L.i(TAG, "图片链接获取成功，开始下载");
                    ImagesUrl imagesUrl = Translate.translateUrl(json);
                    StorageUtil.deleteSave(DownLoadService.this, imagesUrl);
//                    downLoadAllImage(imagesUrl);
                    L.i(TAG, "imagesUrl:" + json);
                    ViewUtil.showSingleToast(DownLoadService.this, "图片url获取成功");
//                    mUrlCheckerHandler.obtainMessage(1).sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                    L.e(TAG, "出错啦1");
                }
            }

            @Override
            public void onShowTimeGet(String json) {
                try {
                    L.i("onShowTimeGet", "showTime获取成功" + json);
                    List<ShowTime3> showTime = Translate.translateShowTime3(json);
//                    L.i(TAG, "showTime转换完毕:" + showTime);
                    if (showTime == null) {
                        Log.w(TAG, "showTime from service is null");
                        return;
                    }

                    StorageUtil.saveShowTime3(DownLoadService.this, showTime);
                    Log.i(TAG, "矫正时间");
                    checkoutTime();


                    L.i(TAG, "showTime保存完毕:");
                    if (!Constants.isReady) {
                        Toast.makeText(DownLoadService.this, "初始化完毕", Toast.LENGTH_SHORT).show();
                        Constants.isReady = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    L.e(TAG, "出错啦2");
                }
            }

            @Override
            public void onUrlsGetFail(String msg) {
                L.e(TAG, "获取url列表失败");
//                mUrlCheckerHandler.obtainMessage(0).sendToTarget();
                util.getUrls();

            }

            @Override
            public void onShowTimeGetFail(String msg) {
                L.e(TAG, "获取时间列表失败");


            }
        });
        getShowTimeHandler.obtainMessage(1).sendToTarget();
        return super.onStartCommand(intent, flags, startId);
    }

    private void checkoutTime() {
        if (checkoutTimeThread == null) {
            Log.w(TAG, "checkoutTimeThread 为 null");
            checkoutTimeThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    long diff = 0;
                    try {
                        diff = DeviceUtil.checkoutTime();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    long absDiff = Math.abs(Constants.UP_CUT_TIME - diff);
                    if (absDiff > 1000) {
                        Log.w(TAG, "the diff " + absDiff + "ms is wrong , ignore ");
                        return;
                    }


                    currCheckoutTime += diff;


                    Constants.UP_CUT_TIME = currCheckoutTime / timeDiffcheckoutCount;

                    timeDiffcheckoutCount++;

                    Log.i(TAG, "DIFF=" + Constants.UP_CUT_TIME);
                    checkoutTimeThread = null;
                }
            };
        } else {
            Log.w(TAG, "checkoutTimeThread 不为空");
        }
        checkoutTimeThread.start();


    }

    private Handler getShowTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                try {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //只需要getSHowTime就可以啦
                            L.i(TAG, "开始获取showTime--向服务器发送请求");
                            util.getShowTime();
                        }
                    }, 100, Constants.REQUEST_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                    L.e(TAG, "出错啦3");
                }
                Constants.isReady = true;
            }

        }
    };


//    private void checkUrlAvaiable() {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                L.i(TAG, "检查url是否存在");
//                ImagesUrl imagesUrl = StorageUtil.getImagesUrl(DownLoadService.this);
//                if (imagesUrl == null) {
//                    L.e(TAG, "图片url不存在,重新获取中");
//                    mUrlCheckerHandler.obtainMessage(0).sendToTarget();
//                    Constants.isReady = false;
//                    util.getUrls();
//                } else {
//                    mUrlCheckerHandler.obtainMessage(1).sendToTarget();
//                }
//
//            }
//        }.start();
//
//    }

//    private Handler mUrlCheckerHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 1) {
//                tips("验证完成");
//                blockingDeque.push(true);
//            } else {
//                tips("url不存在，尝试重新获取url");
//            }
//
//        }
//    };

    private void tips(String s) {
        if (!Constants.isReady) {
            ViewUtil.showSingleToast(DownLoadService.this, s);
        }
    }

    /**
     * @param imagesUrl
     * @deprecated
     */
    private void downLoadAllImage(ImagesUrl imagesUrl) {
        final List<String> url = imagesUrl.getUrl();
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < url.size(); i++) {
                    DownLoadService.this.downloadIndex = i;
                    String urlImage = url.get(downloadIndex);
                    boolean downloadState = DownloadImgUtils.downloadImgByUrl(urlImage, StorageUtil.getDiskCacheDirByPath(DownLoadService.this, urlImage), null);
                    if (downloadState) {
                        mDownloadSusHandler.obtainMessage(1, url).sendToTarget();
                    } else {
                        mDownloadSusHandler.obtainMessage(0).sendToTarget();
                    }
                }

            }
        }.start();
    }

    public Handler mDownloadSusHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tips("正在下载第" + DownLoadService.this.downloadIndex + "个图片");
                    L.i(TAG, "正在下载第" + DownLoadService.this.downloadIndex + "个图片");
                    break;
                case 0:
                    L.i(TAG, "第" + DownLoadService.this.downloadIndex + "个图片下载失败");
                    break;
                case 2:
                    break;
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (timer != null) {
                timer.cancel();
            }
            timer = null;
            util = null;
        } catch (Exception e) {
            e.printStackTrace();
            L.e(TAG, "出错啦4");
        }
    }

}
