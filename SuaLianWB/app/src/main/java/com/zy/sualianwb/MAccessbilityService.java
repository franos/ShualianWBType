package com.zy.sualianwb;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by zz on 16/1/3.
 */
public class MAccessbilityService extends AccessibilityService {
    String TAG = "MAccessbilityService";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.i(TAG, "onAccessibilityEvent");
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        if (null != rootInActiveWindow) {
            CharSequence className = rootInActiveWindow.getClassName();

            Log.i(TAG, "WINDOW CLASS NAME=" + className);
            List<AccessibilityNodeInfo> insall = rootInActiveWindow.findAccessibilityNodeInfosByText("安装");
            showInfo(insall);
            rootInActiveWindow.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            List<AccessibilityNodeInfo> open = rootInActiveWindow.findAccessibilityNodeInfosByText("应用程序已安装");
//            if (null == open) {
//                Log.w(TAG, "open is null");
//                return;
//            }
//            showInfo(open);


            Log.i(TAG, "onClick");
        } else {
            Log.w(TAG, "rootInActiveWindow is null");
        }

        AccessibilityNodeInfo source = event.getSource();
//        Log.i(TAG, "info " + source.toString());
        if (null != source) {

            source.findAccessibilityNodeInfosByText("");


            List<AccessibilityNodeInfo> accessibilityNodeInfosByText = source.findAccessibilityNodeInfosByText("安装");
            if (accessibilityNodeInfosByText == null) {
                Log.w(TAG, "accessibilityNodeInfosByText is null");
                return;
            }
            Log.i(TAG, "source");
            List<AccessibilityNodeInfo> update = source.findAccessibilityNodeInfosByText("Update now");
            if (null == update) {
                Log.w(TAG, "update is null");
            } else {
                for (AccessibilityNodeInfo info : update) {
                    Log.i(TAG, "find info update");
                    info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }

            showInfo(accessibilityNodeInfosByText);
//            List<AccessibilityNodeInfo> open = rootInActiveWindow.findAccessibilityNodeInfosByText("打开");
//            if (null == open) {
//                Log.w(TAG, "open info is null");
//                return;
//            }
//            showInfo(open);
        } else {
            Log.w(TAG, "source is null");
        }

    }

    private void showInfo(List<AccessibilityNodeInfo> accessibilityNodeInfosByText) {
        for (AccessibilityNodeInfo info : accessibilityNodeInfosByText) {
            CharSequence text = info.getText();
            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Log.i(TAG, "text-->" + text + " className-->" + info.getClassName()
            );
        }
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        Log.i(TAG, "onKeyEvent" + event);
        return super.onKeyEvent(event);
    }

    @Override
    public void onInterrupt() {
        Log.i(TAG, "onInterrupt");
    }

    @Override
    protected void onServiceConnected() {
        Log.i(TAG, "onServiceConnected");

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        return super.onUnbind(intent);

    }
}
