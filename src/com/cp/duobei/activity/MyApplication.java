package com.cp.duobei.activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.cp.duobei.dao.Constant;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.IoUtils.CopyListener;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

public class MyApplication extends Application {
//	static public boolean isWifiMode(Context context){
//		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
//		return sp.getBoolean("key_wifi", false);
//	}
	public static boolean isWifiMode = false;
	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
		if(!Constant.STORE_PATH.exists()){
			Constant.STORE_PATH.mkdirs();
		}
		getMode();
	}
	private void getMode() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		isWifiMode = sp.getBoolean("key_wifi", false);
	}
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		//new File("/data/data/"+context.getPackageName()+"/uil/"),
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
//				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				//配置保存路径,文件名字生成器
				.diskCache(new UnlimitedDiscCache(Constant.STORE_PATH_UIL, 
						new File("/data/data/"+context.getPackageName()+"/uil/"), 
						new Md5FileNameGenerator()))//使用MD5生成名字,不设则默认为hash(UnlimitedDiscCache父类BaseDiscCache中指定)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
//				.memoryCache(memoryCache)
//				Default value - LruMemoryCache with limited memory cache size (size = 1/8 of available app memory)
//				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}
