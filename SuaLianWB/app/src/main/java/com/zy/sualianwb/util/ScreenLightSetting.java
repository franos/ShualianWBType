package com.zy.sualianwb.util;

import android.content.Context;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;

/**
 * Created by zz on 15/12/27.
 */
public class ScreenLightSetting {


    /**
     * 获得当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    private static int getScreenMode(Context context){
        int screenMode=0;
        try{
            screenMode = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        }
        catch (Exception localException){

        }
        return screenMode;
    }

    /**
     * 获得当前屏幕亮度值 0--255
     */
    private static int getScreenBrightness(Context context){
        int screenBrightness=255;
        try{
            screenBrightness = Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        }
        catch (Exception localException){

        }
        return screenBrightness;
    }
    /**
     * 设置当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    private static void setScreenMode(int paramInt,Context context){
        try{
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        }catch (Exception localException){
            localException.printStackTrace();
        }
    }
    /**
     * 设置当前屏幕亮度值 0--255
     */
    private static void saveScreenBrightness(int paramInt,Context context){
        try{
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, paramInt);
        }
        catch (Exception localException){
            localException.printStackTrace();
        }
    }
    /**
     * 保存当前的屏幕亮度值，并使之生效
     */
    private static void setScreenBrightness(int paramInt,Activity context){
        Window localWindow = context.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        float f = paramInt / 255.0F;
        localLayoutParams.screenBrightness = f;
        localWindow.setAttributes(localLayoutParams);
    }

}
