package com.zy.sualianwb.util;

import android.content.Context;
import android.util.Log;

public class L {

	public static boolean debug = true;
	
	private static final String TAG = "JuSheTuanCommon";
	
	public static void i(String tag, String log) {
		if (debug) {
			Log.i(tag, log);
		}
	}
	
	public static void i(String msg) {
		if (debug) {
			Log.i(TAG, (msg == null ? "null" : msg));
		}
	}

	public static void i(Class<?> clazz, String log) {
		if (debug) {
			Log.i("------" + clazz.getSimpleName() + "-------", log);
		}
	}

	public static void d(Class<?> clazz, String log) {
		if (debug) {
			Log.d(clazz.getSimpleName(), log);
		}
	}

	public static void setDebug(boolean isDebug) {
//		debug = isDebug;
	}
	
	public static void to(Context context, String s) {
		ViewUtil.showSingleToast(context,s);
	}

	public static void e(String string, String string2)
	{
			if(debug){
				Log.e(string, string2);
			}
	}

	public static void e(Throwable throwable) {
		if(debug) {
			Log.e(TAG, Log.getStackTraceString(throwable));
		}
	}
	
}
