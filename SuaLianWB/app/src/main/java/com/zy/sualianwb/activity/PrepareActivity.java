package com.zy.sualianwb.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zy.sualianwb.Constants;
import com.zy.sualianwb.DownLoadService;
import com.zy.sualianwb.R;
import com.zy.sualianwb.base.BaseActivity;
import com.zy.sualianwb.util.DeviceUtil;
import com.zy.sualianwb.util.L;
import com.zy.sualianwb.util.ShareUitl;

public class PrepareActivity extends BaseActivity {
    private static final String TAG = "PrepareActivity";
    TextView row, column, currpos, start;
    long diff;
    private android.os.Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                start.setClickable(false);
                start.setEnabled(false);
                start.setTextColor(Color.parseColor("#FFFF00"));
                Intent intent = new Intent(PrepareActivity.this, ShowActivity2.class);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        initStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_prepare);
        DeviceUtil.turnLight(this);
        getAppVersionName(this);
        row = (TextView) findViewById(R.id.pre_row);
        column = (TextView) findViewById(R.id.pre_column);
        currpos = (TextView) findViewById(R.id.pre_num);
        start = (TextView) findViewById(R.id.pre_start);
        initStart();
        restorage();

        int currPos = Constants.currPos;
        if (currPos == 0) {
            Toast.makeText(PrepareActivity.this, "请选择一个位置", Toast.LENGTH_SHORT).show();
            finish();
        }

        currpos.setText("" + currPos);
        row.setText("" + ((Constants.currPos - 1) / Constants.Y_NUM + 1) + "行");
        column.setText("" + ((Constants.currPos - 1) % Constants.X_NUM + 1) + "列   第" +
                "程序版本号 " + versioncode +
                "   diff" + Constants.UP_CUT_TIME +
                "");

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrepareActivity.this, DownLoadService.class);
                startService(intent);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        while (!Constants.isReady) {
                            L.i(TAG, "初始化进行中");
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        L.i(TAG, "初始化完毕,跳转到播放页面");
                        mHandler.obtainMessage(0).sendToTarget();
                    }
                }.start();
            }
        });
    }

    private void initStart() {
        start.setClickable(true);
        start.setEnabled(true);
        start.setTextColor(Color.parseColor("#76FF03"));
    }

    private void restorage() {
        String currpos = ShareUitl.getString(Constants.SHARE_PREF, "currpos", this);
        if (currpos != null) {
            Constants.currPos = Integer.parseInt(currpos);
        }
    }

    static int versioncode = 0;

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";

        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }
}
