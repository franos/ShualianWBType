package com.zy.sualianwb;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import com.zy.sualianwb.util.WIFIConfig;
import com.zy.sualianwb.util.WifiAutoConnectManager;

public class WifiLinkService extends Service {
    private static final String TAG = "WifiLinkService";
    private Thread linkWifiThread;
    private boolean shouldReconnect;
    private boolean shouldCheckWifi = true;
    private static final String WIFI_NAME = "SOTER_Only";
    private static final String WIFI_PASSWORD = "23456789";



    public WifiLinkService() {
        Log.i(TAG, "WifiLinkService constuct create");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "WifiLinkService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "WifiLinkService onStartCommand");

        if (linkWifiThread == null) {
            linkWifiThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        shouldReconnect = false;
                        WifiAutoConnectManager wifiAutoConnectManager = new WifiAutoConnectManager(WifiLinkService.this);
                        WIFIConfig.getInstants(WifiLinkService.this);
                        WifiManager localWifiManager = WIFIConfig.getLocalWifiManager();

                        while (shouldCheckWifi) {

                            shouldReconnect = shouldReConnect(localWifiManager);

                            Log.i(TAG, "shouldReconnect " + shouldReconnect);
                            if (shouldReconnect) {

                                wifiAutoConnectManager.connect(WIFI_NAME, WIFI_PASSWORD, WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA);
                            }

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        release();
                    }

                }

            };
            linkWifiThread.start();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void release() {
        shouldCheckWifi = false;
        shouldReconnect = false;
        linkWifiThread = null;
    }


    private boolean shouldReConnect(WifiManager localWifiManager) {


        WifiInfo connectionInfo = localWifiManager.getConnectionInfo();

        Log.i(TAG, " current connect Info =" + connectionInfo);
        if (null == connectionInfo) {
            Log.w(TAG, "connectionInfo is null");
            return true;
        }

        String ssid = connectionInfo.getSSID();
        Log.i(TAG, "ssid=" + ssid);
        if (!ssid.equals("\"" + WIFI_NAME + "\"")) {
            Log.w(TAG, "wifi name :" + ssid + " is not SOTER_Only, try to reconnect!");
            localWifiManager.disconnect();
            return true;
        }

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        release();
    }
}
