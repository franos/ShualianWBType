package com.zy.sualianwb.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

import java.util.ArrayList;
import java.util.List;

public class WifiUtil {
	private static WifiInfo mWifiInfo;
	public static  WifiManager mWifiManager;
	public static WifiAutoConnectManager mWifiAutoConnectManager;
	private static Context mContext;
	public static boolean isInit=false;
	private static WifiLock wifiLock;
	private ConnectivityManager cManager;
	private NetworkInfo netInfo;
	
	/**
	 * 搜索到的wifi信息列表
	 */
	public List<ScanResult> mScanfWifiList;
	/**
	 * 无密码的wifi的名称列表
	 */
	public ArrayList<String> mWifiName;
	private ScanResult mscanResult;
	/**
	 * 当前连接wifi的名称
	 */
	private String mWifiID = null;
	private static WifiUtil util;
	
	
	public static void init(Context context){
		if(!isInit){
		mContext=context.getApplicationContext();
		initWifi();
		isInit=true;
		}
	}
	
	public static WifiUtil getUtil(){
		if(util==null){
			util=new WifiUtil();
		}
		return util;
	}
	
	private WifiUtil(){

	}

	/**
	 * 打开手机的WLAN为搜索WiFi做准备
	 */
	public void OpenWLAN(){
		if(!mWifiManager.isWifiEnabled()){
			mWifiManager.setWifiEnabled(true);
		}
	}

	/**
	 * 关掉WLAN

	 */
	public void CloseWlAN(){
		if(mWifiManager.isWifiEnabled()){
			mWifiManager.setWifiEnabled(false);
		}
	}
	
	public int getWifiIp(){
		mWifiInfo=mWifiManager.getConnectionInfo();
		return  mWifiInfo.getIpAddress();
	}

	/**
	 * 判断wifi状态
	 * @return
	 */
	public boolean ifConnect(){
		cManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		netInfo = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return netInfo.isConnected();
	}
	/**
	 * 根据WiFi名连接上wifi
	 * @param WIFIID
	 */
	public void ConnectToWifi(Context context,String WIFIID){
		mWifiAutoConnectManager = new WifiAutoConnectManager(mWifiManager, context);
		mWifiAutoConnectManager.disconnectWifi(mWifiAutoConnectManager.getNetworkId());
		cManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		netInfo = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//		mWifiAutoConnectManager.connect(WIFIID, "",
//				WifiCipherType.WIFICIPHER_NOPASS);
		System.out.println("echo try to connect to"+WIFIID);
	}
	
	public boolean isDevice(String WIFIID){
		mWifiInfo = mWifiManager.getConnectionInfo();
		mWifiInfo.getNetworkId();
		System.out.println("echo------netId"+mWifiInfo.getNetworkId());
		return false;
	}

	/**
	 * 把所有wifi禁掉
	 */
	public void AvoidAllWifiConnected(){
		StopConnectWifi();
		mWifiManager.disconnect();
		mWifiInfo = null;
	}
	
	/**
	 * 解除wifi禁用
	 */
	public void ReleaseWifiAvoid(boolean otherRelease){
		mWifiManager.enableNetwork(mWifiAutoConnectManager.getNetworkId(), otherRelease);
		mWifiInfo = mWifiManager.getConnectionInfo();
	}
	
	
	
	/**
	 * 断开当前WiFi
	 */
	public void StopConnectWifi( ){
		mWifiAutoConnectManager.disconnectWifi(mWifiAutoConnectManager.
				getNetworkId());
	}
	/**
	 * 搜索附近WIFI

	 */
	public void ScanfWifi(){
		if(mWifiManager.isWifiEnabled()){
			mWifiManager.startScan();

		}else{
			OpenWLAN();
			ScanfWifi();
		}
	}

	public void getScanResult(){
		mScanfWifiList = mWifiManager.getScanResults();
		setWifiList();
		System.out.println("getScanResult-----");
	}

	/**
	 * 设置wifi列表，只存放无密码wifi
	 */
	private void setWifiList() {
		mWifiName = new ArrayList<String>();
		for(int i =0; i<mScanfWifiList.size();i++){
			mscanResult = mScanfWifiList.get(i);
			if(mscanResult.capabilities.contains("WPA")){
				continue;
			}
			if(mscanResult.capabilities.contains("WEP")){
				continue;
			}
			mWifiName.add(mscanResult.SSID);
			System.out.println("echowifi-----"+mscanResult.SSID);
		}
		System.out.println("echowifilistSize-----"+mScanfWifiList.size());
	}

	/**
	 * 获取WIFI列表
	 * @return
	 */
	public ArrayList<String> getWifiList(){
		return mWifiName;
	}

	/**
	 * 设置当前连着的wifi的名称
	 */
	public void setWifiID(){
		mWifiInfo = mWifiManager.getConnectionInfo();
		mWifiID = mWifiInfo != null ? mWifiInfo.getSSID() : null;
	}

	/**
	 * 获得当前连接的wifi名称
	 * @return
	 */
	public String getWifiID(){
		setWifiID();
		return mWifiID;
	}
	
	/**
	 * 设置wifi锁
	 */
	public void setWifiLock(){
		if(wifiLock.isHeld()){
			return;
		}
		wifiLock.acquire();
	}
	
	/**
	 * 解除wifi锁
	 */
	public void unLockWifi(){
		if(wifiLock.isHeld()){
			wifiLock.release();
		}
	}
	
	private static void initWifi() {
		mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		mWifiAutoConnectManager = new WifiAutoConnectManager(mWifiManager, 
				mContext);
		mWifiInfo= mWifiManager.getConnectionInfo();
		wifiLock = mWifiManager.createWifiLock("echo");
	}
}
