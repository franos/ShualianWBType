package com.zy.sualianwb.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.update.UmengUpdateAgent;
import com.zy.sualianwb.Constants;
import com.zy.sualianwb.R;
import com.zy.sualianwb.base.BaseActivity;
import com.zy.sualianwb.module.CrtTime;
import com.zy.sualianwb.module.ImagesUrl;
import com.zy.sualianwb.util.DensityUtil;
import com.zy.sualianwb.util.DeviceUtil;
import com.zy.sualianwb.util.DownLoadUtil;
import com.zy.sualianwb.util.DownloadImgUtils;
import com.zy.sualianwb.util.HttpUtil;
import com.zy.sualianwb.util.Insign;
import com.zy.sualianwb.util.L;
import com.zy.sualianwb.util.PickerView;
import com.zy.sualianwb.util.ShareUitl;
import com.zy.sualianwb.util.StorageUtil;
import com.zy.sualianwb.util.TimeUtil;
import com.zy.sualianwb.util.Translate;
import com.zy.sualianwb.util.ViewUtil;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    EditText hang, lie;
    Timer timer, checkVersonTimer;
    boolean show = true;
    PickerView pickerViewHang, pickerViewLie;

    //下载
    private DownLoadUtil util;
    private TextView tips;
    private TextView defTips, oneTimeTV, allTimeTV, hasDownloadTv;
    private int delayTime = 1000;
    private int downloadIndex;

    private Timer mDownLoadTimer;
    private int currSecond = 0;
    private int reTryTime = 1;
    Timestamp stopTimeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);
        DeviceUtil.turnLight(this);
        restoreData();
        oneTimeTV = (TextView) findViewById(R.id.one_time);
        allTimeTV = (TextView) findViewById(R.id.all_time);
        hang = (EditText) findViewById(R.id.main_change_num_y_edt);
        lie = (EditText) findViewById(R.id.main_change_num_x_edt);
        hasDownloadTv = (TextView) findViewById(R.id.main_hasDownload);
        pickerViewHang = (PickerView) findViewById(R.id.main_pickview_hang);
        pickerViewLie = (PickerView) findViewById(R.id.main_pickview_lie);
        pickerViewLie.setFocusable(true);
        pickerViewLie.setFocusableInTouchMode(true);
        pickerViewLie.requestFocus(); // 初始不让EditText得焦点
        pickerViewLie.requestFocusFromTouch();
        initPickView();
        resetHasDownLoad();


        hang.setText("" + Constants.Y_NUM);
        lie.setText("" + Constants.X_NUM);


        checkVersonTimer = new Timer();

        checkVersonTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                UmengUpdateAgent.update(MainActivity.this);
            }
        }, 0, 20000);

        timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    TimeUtil.startRecode();

                    String s = HttpUtil.get(Constants.CHECK_TIME);
                    CrtTime crtTime = new Gson().fromJson(s, CrtTime.class);

                    long stopDelay = TimeUtil.stop() / 2;
                    Timestamp beiJinTimeStamp = new Timestamp(crtTime.getBjtime());

                    Timestamp localTimeStamp = new Timestamp(System.currentTimeMillis());

                    //此时的北京时间
                    long trueBeijinTime = beiJinTimeStamp.getTime() + stopDelay;
                    //此时的本地时间
                    long trueLocalTimeStamp = localTimeStamp.getTime() - stopDelay;
                    //相隔时间
                    Constants.UP_CUT_TIME = trueBeijinTime - trueLocalTimeStamp;
                    Log.e(TAG, "偏差时间" + Constants.UP_CUT_TIME);

                } catch (Exception e) {
                    e.printStackTrace();
                    Constants.UP_CUT_TIME = 0;
                }
            }
        }, 0, 20000000);
        initDownLoad();
        initInsign();

    }

    public void changeModule(View view) {
        if (Constants.isInsign) {
            Constants.isInsign = false;
        } else {
            Constants.isInsign = true;
        }
        toastInsign();
    }

    private void initInsign() {
        List<Integer> insign = Insign.getInsign();
        int currPos = Constants.currPos;
        if (insign.contains(currPos)) {
            //在里面
            Constants.isInsign = true;
        } else {
            Constants.isInsign = false;
        }
        toastInsign();
    }

    private void toastInsign() {
        if (Constants.isInsign) {
            ViewUtil.showSingleToast(this, "当前位置为全量,下载将会下载3000张原图");
        } else {
            ViewUtil.showSingleToast(this, "当前位置为模板部分,下载将会下载7张模板图片");
        }
    }

    private void restoreData() {
        String num = ShareUitl.getString(Constants.SHARE_PREF, "num", this);
        L.i(TAG, "restore num=" + num);
        if (num != null) {
            Constants.X_NUM = Integer.parseInt(num);
            Constants.Y_NUM = Constants.X_NUM;
        }
        String currpos = ShareUitl.getString(Constants.SHARE_PREF, "currpos", this);
        L.i(TAG, "currPos=" + currpos);
        if (null != currpos) {
            Constants.currPos = Integer.parseInt(currpos);
        } else {
            Log.w(TAG, "currPos=null");
            Constants.currPos = 1;
        }
    }

    private void initDownLoad() {
        util = new DownLoadUtil();
        tips = (TextView) findViewById(R.id.download_tips_tv);
        String downLoadState = ShareUitl.getString(Constants.SHARE_PREF, "downLoadState", this);
        Log.w(TAG, "downLoadState" + downLoadState);

        if (null == downLoadState) {
            resetSuccessTips();
        } else {
            setSuccessTips();
        }
        setDownloadUtilListener();
    }

    private void setDownloadUtilListener() {
        util.setOnTemplateGet(new DownLoadUtil.OnTemplateGet() {
            @Override
            public void onTemplateGet(String json) {
                tips("图片链接获取成功，开始下载");
                ImagesUrl imagesUrl = Translate.translateTemplateUrl(json);
                StorageUtil.deleteSave(MainActivity.this, imagesUrl);
                downLoadAllImage(imagesUrl);
                Log.i(TAG, "imagesUrl:" + json);
            }

            @Override
            public void onFail(String msg) {
                Toast.makeText(MainActivity.this, "图片链接获取失败", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onUrlsGetFail:" + msg);
            }
        });


        util.setOnDataGet(new DownLoadUtil.OnDataGet() {
            @Override
            public void onUrlsGet(String json) {
                tips("图片链接获取成功，开始下载");
                ImagesUrl imagesUrl = Translate.translateUrl(json);
                StorageUtil.deleteSave(MainActivity.this, imagesUrl);
                downLoadAllImage(imagesUrl);
                Log.i(TAG, "imagesUrl:" + json);
            }

            @Override
            public void onShowTimeGet(String json) {
            }

            @Override
            public void onUrlsGetFail(String msg) {
                Toast.makeText(MainActivity.this, "图片链接获取失败", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onUrlsGetFail:" + msg);
            }

            @Override
            public void onShowTimeGetFail(String msg) {
                Toast.makeText(MainActivity.this, "图片时间获取失败", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "第" + (1 + MainActivity.this.downloadIndex) + "个图片下载失败", Toast.LENGTH_SHORT).show();

                    break;
                case 2:
                    setSuccessTips();
                    break;
            }

        }
    };

    private void setSuccessTips() {
        Constants.isStartDownload = false;
        if (mDownLoadTimer != null) {
            mDownLoadTimer.cancel();
        }

        if (Constants.isAllDownloadOk) {
            tips.setTextColor(Color.parseColor("#689F38"));
            tips.setText("图片已全部下载完毕!");
        } else {
            tips.setTextColor(Color.parseColor("#EB9682"));
            tips.setText("图片未全部下载完毕，请重新点击下载图片");
        }
//        delayTime = Integer.MAX_VALUE;
//        long stopAllMillis = TimeUtil.stopAll();
//        Date date = new Date(stopAllMillis);
//
//        DateTime dt = new DateTime(date);
//        allTimeTV.setText("" + getSecond(stopAllMillis));

        Log.i(TAG, "write Downloadstate 1");
        ShareUitl.writeString(Constants.SHARE_PREF, "downLoadState", "1", this);
        if (!Constants.isAllDownloadOk && reTryTime < 3) {
            reTryTime++;
            Toast.makeText(MainActivity.this, "图片下载不全，正在尝试重新下载", Toast.LENGTH_SHORT).show();
            toDownload(null);
        }
    }


    private String getSecond(long stopAllMillis) {
        float fl = stopAllMillis / 1000;
        return String.valueOf(fl);
    }

    public void resetSuccessTips() {
        tips.setTextColor(Color.parseColor("#757575"));
        tips.setText("下载计数");
        ShareUitl.writeString(Constants.SHARE_PREF, "downLoadState", null, this);
        Log.i(TAG, "write Downloadstate null");
    }

    private void downLoadAllImage(ImagesUrl imagesUrl) {
        String lastFilePath = ShareUitl.getString(Constants.SHARE_PREF_LAST_FILE, "file_path", this);
        if (null != lastFilePath) {
            File lastFile = new File(lastFilePath);
            if (lastFile.exists()) {
                lastFile.delete();
            } else {
                Log.w(TAG, "lastFile is not exist");
            }
        } else {
            Log.w(TAG, "lastFile is null");
        }
        final List<String> url = imagesUrl.getUrl();
        if (null == url) {
            Log.e(TAG, "数据结构异常");
            return;
        }

        int targetDownloadSize = url.size();
        int hasDownloadSize = StorageUtil.hasDownload();
        if (hasDownloadSize < targetDownloadSize) {

        }


        new Thread() {
            @Override
            public void run() {

                currSecond = 0;
                stopTimeStamp = new Timestamp(System.currentTimeMillis());
                final long cut0 = stopTimeStamp.getTime() - startTimeStamp.getTime();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        allTimeTV.setText("" + cut0 / 1000 + " 秒");
                    }
                });

                for (int i = 0; i < url.size(); i++) {
                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tips.setText("正在下载第" + (finalI + 1) + "张图片/共 " + url.size() + " 张");
                        }
                    });
                    MainActivity.this.downloadIndex = i;
                    String urlImage = url.get(downloadIndex);
                    TimeUtil.startDownLoadRecode();
                    boolean downloadState = DownloadImgUtils.downloadImgByUrl(urlImage, StorageUtil.getDiskCacheDirByPath(MainActivity.this, urlImage), MainActivity.this);
                    Log.i(TAG, "index " + (i + 1) + " downloadState=" + downloadState);
                    stopTimeStamp = new Timestamp(System.currentTimeMillis());

                    resetHasDownLoad();

                    final long cut = stopTimeStamp.getTime() - startTimeStamp.getTime();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            allTimeTV.setText(" " + cut / 1000 + " 秒");
                        }
                    });


//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            long singleMillis = TimeUtil.stopDownLoadRecode();
//                            Log.i("millis",""+singleMillis);

//                            oneTimeTV.setText("" + singleMillis);
//                        }
//                    });

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

    private void tips(String text) {
        tips.setText(text);
    }


    private void initPickView() {
        List<String> pickViewDataHang = new ArrayList<>(Constants.X_NUM);
        List<String> pickViewDataLie = new ArrayList<>(Constants.X_NUM);
        for (int i = 1; i <= Constants.X_NUM; i++) {
            pickViewDataHang.add("" + i);
            pickViewDataLie.add("" + i);
        }
        pickerViewHang.setData(pickViewDataHang);
        pickerViewLie.setData(pickViewDataLie);

        pickerViewLie.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                int lie = Integer.parseInt(text);
                L.i(TAG, "onSelected lie" + lie);
                savePosState();
            }
        });
        pickerViewHang.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                int hang = Integer.parseInt(text);
                L.i(TAG, "onSelected hang" + hang);
                savePosState();
            }
        });
        int currHang = DensityUtil.getCurrHang(Constants.currPos - 1) + 1;

        int currLie = DensityUtil.getCurrLie(Constants.currPos - 1) + 1;
        L.i(TAG, "currHang=" + currHang + " currLie=" + currLie);

        pickerViewHang.setSelected("" + currHang);
        pickerViewLie.setSelected("" + currLie);
    }

    private void savePosState() {
        String currentedSelectedLie = pickerViewLie.getCurrentedSelected();
        String currentedSelectedHang = pickerViewHang.getCurrentedSelected();
        int currhang = Integer.parseInt(currentedSelectedHang);
        int currlie = Integer.parseInt(currentedSelectedLie);
        L.i(TAG, "currhang=" + currhang + "  currlie=" + currlie);
        Constants.currPos = DensityUtil.getCurrPos(currhang, currlie);
        L.i(TAG, "currPos=" + Constants.currPos);
        saveCurrPos(Constants.currPos);
        initInsign();
    }

    /**
     * 更改大小
     *
     * @param view
     */
    public void changePos(View view) {
        Constants.X_NUM = Integer.parseInt(lie.getText().toString());
        Constants.Y_NUM = Constants.X_NUM;
        ShareUitl.writeString(Constants.SHARE_PREF, "num", "" + Constants.X_NUM, this);
        ViewUtil.showSingleToast(this, "修改完毕,当前行列总数:" + Constants.Y_NUM);
        initPickView();
        pickerViewHang.setSelected("" + 1);
        pickerViewLie.setSelected("" + 1);
        savePosState();
    }

    Timestamp startTimeStamp;

    public void toDownload(View view) {

        if (null == startTimeStamp) {
            startTimeStamp = new Timestamp(System.currentTimeMillis());
        }
        boolean isStartDownload = false;
        if (isStartDownload) {
            Toast.makeText(MainActivity.this, "下载已开始，不用重复点击", Toast.LENGTH_SHORT).show();
        } else {
            Constants.isAllDownloadOk = true;
            Constants.isStartDownload = true;
            resetSuccessTips();
            tips.setText("开始下载图片");
            fetchUrl();

        }
    }

    private void fetchUrl() {
        if (Insign.isInsign()) {
            //在里面
            util.getUrls();
        } else {
            //在外面
            util.getTemplateUrl();
        }
    }


    public void toShow(View view) {
        Intent intent = new Intent(this, PrepareActivity.class);
        startActivity(intent);
    }

//    public void choose(View view) {
////
////        ViewUtil.showSingleToast(MainActivity.this, "请选择一个位置");
////        Intent intent = new Intent(MainActivity.this, TestTable.class);
////        startActivity(intent);
//
//
//        ViewUtil.showSingleToast(MainActivity.this, "请选择一个位置");
//        Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
//        startActivity(intent);
////        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
////        dialogBuilder.setTitle("选择位置");
////        dialogBuilder.setMessage("进入正式环境吗?");
////        dialogBuilder.setPositiveButton("是", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////
////            }
////        });
////        dialogBuilder.setNegativeButton("否", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
//
////            }
////        });
////        AlertDialog dialog = dialogBuilder.create();
////        dialog.show();
//
//    }

    public void clear(View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("清除所有缓存");
        dialogBuilder.setMessage("确定清除所有数据（包含图片）吗?");
        dialogBuilder.setPositiveButton("清除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StorageUtil.clearAllData(MainActivity.this);
                dialog.dismiss();
                resetHasDownLoad();
                ViewUtil.showSingleToast(MainActivity.this, "清理完毕");
                resetSuccessTips();
            }
        });
        dialogBuilder.setNegativeButton("不清除", null);
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    private void resetHasDownLoad() {
        final int hasDownload = StorageUtil.hasDownload();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hasDownloadTv.setText("已下载 " + hasDownload);
            }
        });

    }

//    public void clearShowTime(View view) {
//        StorageUtil.clearShowTime(this);
//        ViewUtil.showSingleToast(MainActivity.this, "清理完毕");
//        ShareUitl.writeString("ShowActivity_Perf", "picId", null, this);//清除标识
//    }

//    public void clearUrl(View view) {
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        dialogBuilder.setTitle("清除显示图片的url");
//        dialogBuilder.setMessage("确定清除所有显示图片的url吗? 这可能会导致图片不能正常显示");
//        dialogBuilder.setPositiveButton("清除", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                StorageUtil.clearUrl(MainActivity.this);
//                dialog.dismiss();
//                ViewUtil.showSingleToast(MainActivity.this, "清理完毕");
//            }
//        });
//        dialogBuilder.setNegativeButton("不清除", null);
//        AlertDialog dialog = dialogBuilder.create();
//        dialog.show();
//    }

//    public void clearUrlAndShowTime(View view) {
//        StorageUtil.clearUrlAndShowTime(this);
//        ViewUtil.showSingleToast(MainActivity.this, "清理完毕");
//    }

    /**
     * 存储真实位置
     *
     * @param currPos
     */
    public void saveCurrPos(int currPos) {
        ShareUitl.writeString(Constants.SHARE_PREF, "currpos", "" + currPos, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.isStartDownload = false;
        if (null != timer) {
            timer.cancel();
        }
        if (null != mDownLoadTimer) {
            mDownLoadTimer.cancel();
        }
        currSecond = 0;
        startTimeStamp = null;
    }

}
