package com.zy.sualianwb.util;

import android.provider.Settings;
import android.view.WindowManager;

import com.zy.sualianwb.base.BaseActivity;

/**
 * Created by zz on 16/1/1.
 */
public class DeviceUtil {

    public static void turnLight(BaseActivity context) {

        // 根据当前进度改变亮度
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, 255);
        int tmpInt = Settings.System.getInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, -1);
        WindowManager.LayoutParams wl = context.getWindow().getAttributes();

        float tmpFloat = (float) tmpInt / 255;
        if (tmpFloat > 0 && tmpFloat <= 1) {
            wl.screenBrightness = tmpFloat;
        }
        context.getWindow().setAttributes(wl);
    }

}
