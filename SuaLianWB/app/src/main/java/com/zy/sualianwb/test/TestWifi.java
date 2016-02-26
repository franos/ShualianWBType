package com.zy.sualianwb.test;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.zy.sualianwb.R;
import com.zy.sualianwb.base.BaseActivity;
import com.zy.sualianwb.util.WIFIConfig;
import com.zy.sualianwb.util.WifiAutoConnectManager;
import com.zy.sualianwb.util.WifiUtil;

import java.util.List;

/**
 * Created by zz on 16/1/10.
 */
public class TestWifi extends BaseActivity {
    WIFIConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_wifi);
        config = WIFIConfig.getInstants(this);

    }

    public void link(View view) {
        WifiUtil util = WifiUtil.getUtil();
        util.init(this);
        util.ScanfWifi();

        WifiAutoConnectManager wifiAutoConnectManager = new WifiAutoConnectManager(config.getLocalWifiManager(), this);

        List<ScanResult> scanResults = config.getScanResults();
        for (ScanResult result : scanResults) {
            Log.i("scanRes", result.toString());
        }
        wifiAutoConnectManager.connect("SOTER_Only","23456789", WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);

    }
}
