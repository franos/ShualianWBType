package com.zy.sualianwb.util;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;

/**
 * Created by zz on 16/1/10.
 */
public class WIFIConfig {
    private List<WifiConfiguration> wifiConfigList;
    private static WifiManager localWifiManager;
    private static WIFIConfig config;
    static String TAG = "WIFIConfig";

    public static WIFIConfig getInstants(Context context) {
        if (config == null) {
            synchronized (WIFIConfig.class) {
                if (null == config) {
                    config = new WIFIConfig();
                    localWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                }
            }
        }
        return config;
    }

    //开启WIFI
    public void WifiOpen() {
        if (!localWifiManager.isWifiEnabled()) {
            localWifiManager.setWifiEnabled(true);
        }
    }

    //扫描wifi
    public void WifiStartScan() {
        localWifiManager.startScan();
    }

    //得到Scan结果
    public List<ScanResult> getScanResults() {
        return localWifiManager.getScanResults();//得到扫描结果
    }

    //得到Wifi配置好的信息
    public List<WifiConfiguration> getConfiguration() {
        //得到配置好的网络信息
        wifiConfigList = localWifiManager.getConfiguredNetworks();
        for (int i = 0; i < wifiConfigList.size(); i++) {
            Log.i(TAG, wifiConfigList.get(i).SSID);
            Log.i(TAG, String.valueOf(wifiConfigList.get(i).networkId));
        }
        return wifiConfigList;
    }

    //判定指定WIFI是否已经配置好,依据WIFI的地址BSSID,返回NetId
    public int IsConfiguration(String SSID) {
        Log.i(TAG, String.valueOf(wifiConfigList.size()));
        for (int i = 0; i < wifiConfigList.size(); i++) {
            Log.i(wifiConfigList.get(i).SSID, String.valueOf(wifiConfigList.get(i).networkId));
            if (wifiConfigList.get(i).SSID.equals(SSID)) {//地址相同
                return wifiConfigList.get(i).networkId;
            }
        }
        return -1;
    }

    //添加指定WIFI的配置信息,原列表不存在此SSID
    public int AddWifiConfig(List<ScanResult> wifiList, String ssid, String pwd) {
        int wifiId = -1;
        for (int i = 0; i < wifiList.size(); i++) {
            ScanResult wifi = wifiList.get(i);
            if (wifi.SSID.equals(ssid)) {
                Log.i(TAG, "equals");
                WifiConfiguration wifiCong = new WifiConfiguration();
                wifiCong.SSID = "\"" + wifi.SSID + "\"";//\"转义字符，代表"
                wifiCong.preSharedKey = "\"" + pwd + "\"";//WPA-PSK密码
                wifiCong.hiddenSSID = false;
                wifiCong.status = WifiConfiguration.Status.ENABLED;
                wifiId = localWifiManager.addNetwork(wifiCong);//将配置好的特定WIFI密码信息添加,添加完成后默认是不激活状态，成功返回ID，否则为-1
                if (wifiId != -1) {
                    return wifiId;
                }
            }
        }
        return wifiId;
    }

    //连接指定Id的WIFI
    public boolean ConnectWifi(int wifiId) {
        for (int i = 0; i < wifiConfigList.size(); i++) {
            WifiConfiguration wifi = wifiConfigList.get(i);
            if (wifi.networkId == wifiId) {
                while (!(localWifiManager.enableNetwork(wifiId, true))) {//激活该Id，建立连接
                    //status:0--已经连接，1--不可连接，2--可以连接
                    Log.i(TAG, "status " + String.valueOf(wifiConfigList.get(wifiId).status));
                }
                return true;
            }
        }
        return false;
    }

    public static WifiManager getLocalWifiManager() {
        return localWifiManager;
    }
}
