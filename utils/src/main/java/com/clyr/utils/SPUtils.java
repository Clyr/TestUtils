package com.clyr.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences 保存获取
 * */
public final class SPUtils {
	
	private final static String name = "config";
	private final static int mode = Context.MODE_PRIVATE;
	
	/**
	 * 保存
	 * @param context
	 * @param key
	 * @param value
	 */
	@SuppressLint("ApplySharedPref")
	public static void saveBoolean(Context context, String key, boolean value){
		if(context==null){
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
	@SuppressLint("ApplySharedPref")
	public static void saveInt(Context context, String key, int value){
		if(context==null){
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		Editor edit = sp.edit();
		edit.putInt(key, value);
		edit.commit();
	}
	@SuppressLint("ApplySharedPref")
	public static void saveString(Context context, String key, String value){
		if(context==null){
			return;
		}
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	
	/**
	 * 获取
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(Context context, String key, boolean defValue){
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp.getBoolean(key, defValue);
	}
	
	public static int getInt(Context context, String key, int defValue){
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp.getInt(key, defValue);
	}
	
	public static String getString(Context context, String key, String defValue){
		SharedPreferences sp = context.getSharedPreferences(name, mode);
		return sp.getString(key, defValue);
	}

}

