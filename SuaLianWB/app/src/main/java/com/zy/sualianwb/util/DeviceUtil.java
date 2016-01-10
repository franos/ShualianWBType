package com.zy.sualianwb.util;

import android.provider.Settings;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.zy.sualianwb.Constants;
import com.zy.sualianwb.base.BaseActivity;
import com.zy.sualianwb.module.CrtTime;

import java.io.IOException;
import java.sql.Timestamp;

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


    public static long checkoutTime() throws IOException {
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
        long l = trueBeijinTime - trueLocalTimeStamp;
        return l;

    }

}
