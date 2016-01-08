package com.zy.sualianwb.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ShareUitl {

	public synchronized static void writeString(String dbName,String key,String value,Context context){
			SharedPreferences sp = context.getSharedPreferences(dbName, Context.MODE_PRIVATE);
			Editor edit = sp.edit();
			edit.putString(key, value);
			edit.commit();
	}
	public synchronized static String getString(String dbName,String key,Context context){
		SharedPreferences sp = context.getSharedPreferences(dbName, Context.MODE_PRIVATE);
		return sp.getString(key, null);
	}
	public synchronized static void delete(String dbName,String key,Context context){
		SharedPreferences sp = context.getSharedPreferences(dbName, Context.MODE_PRIVATE);
		sp.edit().clear().commit();
	}
	
	public synchronized static void writeString(String key,String value,Context context){
		writeString("superclass_cache", key, value, context);
	}
	public synchronized static String getString(String key,Context context){
		return getString("superclass_cache", key, context);
	}
	
}
